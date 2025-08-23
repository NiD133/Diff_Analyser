package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    private static final byte[] TEST_DATA = "0123456789".getBytes();

    //<editor-fold desc="Test Cases">

    @Test
    @DisplayName("forSeekableByteChannels should throw NullPointerException when given a null array")
    void forSeekableByteChannels_whenGivenNull_throwsException() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
    }

    @Test
    @DisplayName("A multi-channel composed of empty channels should behave as a single empty channel")
    void channelOfEmptyChannels_behavesAsEmpty() throws IOException {
        try (SeekableByteChannel emptyChannel1 = new SeekableInMemoryByteChannel();
             SeekableByteChannel emptyChannel2 = new SeekableInMemoryByteChannel();
             SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(emptyChannel1, emptyChannel2)) {
            assertChannelIsEmpty(multiChannel);
        }
    }

    @ParameterizedTest(name = "Sanity Check: Read from single channel with readBufferSize={1}")
    @MethodSource("singleChannelReadScenarios")
    @DisplayName("Read from a single in-memory channel should work correctly")
    void read_fromSingleChannel_sanityCheck(final byte[] expectedContent, final int readBufferSize) throws IOException {
        try (SeekableByteChannel singleChannel = new SeekableInMemoryByteChannel(expectedContent)) {
            assertChannelContentIsReadable(expectedContent, singleChannel, readBufferSize);
        }
    }

    @ParameterizedTest(name = "channelChunkSize={1}, readBufferSize={2}")
    @MethodSource("multiChannelReadScenarios")
    @DisplayName("Read from a multi-channel should return the correctly concatenated content")
    void read_fromMultiChannel_returnsConcatenatedContent(final byte[] expectedContent, final int channelChunkSize, final int readBufferSize) throws IOException {
        final byte[][] chunks = groupBytes(expectedContent, channelChunkSize);
        try (SeekableByteChannel multiChannel = createMultiChannel(chunks)) {
            assertChannelContentIsReadable(expectedContent, multiChannel, readBufferSize);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Assertion Helpers">

    /**
     * Asserts that a channel is empty and behaves correctly when closed.
     *
     * @param channel The channel to check.
     * @throws IOException If an I/O error occurs.
     */
    private void assertChannelIsEmpty(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);

        assertTrue(channel.isOpen(), "Channel should be open initially");
        assertEquals(0, channel.size(), "Channel size should be 0");
        assertEquals(0, channel.position(), "Channel position should be 0");
        assertEquals(-1, channel.read(buf), "Reading from an empty channel should return -1");

        // Attempting to read past the end should also return -1
        channel.position(5);
        assertEquals(-1, channel.read(buf), "Reading from a non-zero position in an empty channel should return -1");

        channel.close();
        assertFalse(channel.isOpen(), "Channel should be closed");

        // Operations on a closed channel should throw ClosedChannelException
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "Reading from a closed channel should throw exception");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "Positioning a closed channel should throw exception");
    }

    /**
     * Asserts that the given channel contains the expected content. It reads the channel
     * with a specific buffer size and verifies the content and read behavior.
     *
     * @param expectedContent The byte array the channel is expected to contain.
     * @param channel         The channel to test.
     * @param readBufferSize  The size of the buffer to use for reading.
     * @throws IOException If an I/O error occurs.
     */
    private void assertChannelContentIsReadable(final byte[] expectedContent, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        // 1. Assert initial state
        assertTrue(channel.isOpen(), "Channel should be open");
        assertEquals(expectedContent.length, channel.size(), "Channel size should match expected content length");
        channel.position(0);
        assertEquals(0, channel.position(), "Position should be reset to 0");

        // 2. Read content and verify greedy read behavior
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);
        long totalBytesRead = 0;

        while (true) {
            final int bytesRead = channel.read(readBuffer);
            if (bytesRead == -1) {
                break;
            }
            totalBytesRead += bytesRead;

            // A well-behaved read should be "greedy" and fill the buffer if there's more data available.
            if (channel.position() < channel.size()) {
                assertEquals(readBufferSize, bytesRead,
                    () -> "Read should fill the buffer if more data is available in the channel. Total read so far: " + totalBytesRead);
            }

            readBuffer.flip();
            final byte[] chunk = new byte[readBuffer.remaining()];
            readBuffer.get(chunk);
            outputStream.write(chunk);
            readBuffer.clear();
        }

        // 3. Assert final content
        assertArrayEquals(expectedContent, outputStream.toByteArray(),
            () -> String.format("Channel content should match expected content for readBufferSize=%d", readBufferSize));
    }

    //</editor-fold>

    //<editor-fold desc="Test Data Providers">

    /**
     * Provides scenarios for testing a single channel with various buffer sizes.
     */
    static Stream<Arguments> singleChannelReadScenarios() {
        return IntStream.rangeClosed(1, TEST_DATA.length + 5)
            .mapToObj(readBufferSize -> Arguments.of(TEST_DATA, readBufferSize));
    }

    /**
     * Provides scenarios for testing a multi-channel, varying both the way the
     * content is chunked across channels and the buffer size used for reading.
     */
    static Stream<Arguments> multiChannelReadScenarios() {
        return IntStream.rangeClosed(1, TEST_DATA.length) // Vary the size of each channel chunk
            .boxed()
            .flatMap(channelChunkSize -> IntStream.rangeClosed(1, TEST_DATA.length + 5) // Vary the read buffer size
                .mapToObj(readBufferSize -> Arguments.of(TEST_DATA, channelChunkSize, readBufferSize)));
    }

    //</editor-fold>

    //<editor-fold desc="Test Factory Helpers">

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     *
     * @param chunks Each byte array represents the content of an underlying channel.
     * @return A multi-channel concatenating the content of the chunks.
     */
    private SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Splits a byte array into smaller chunks of a specified size.
     *
     * @param input     The byte array to split.
     * @param chunkSize The maximum size of each chunk.
     * @return An array of byte arrays representing the chunks.
     */
    private byte[][] groupBytes(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int idx = 0; idx < input.length; idx += chunkSize) {
            final int end = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, end));
        }
        return groups.toArray(new byte[0][]);
    }

    //</editor-fold>
}