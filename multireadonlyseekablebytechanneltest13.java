package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 *
 * <p>The tests follow a comprehensive strategy by verifying the channel's behavior
 * against various data chunking schemes and read buffer sizes.</p>
 */
public class MultiReadOnlySeekableByteChannelTest {

    private static final byte[] TEST_DATA = "0123456789".getBytes(StandardCharsets.US_ASCII);

    //<editor-fold desc="Tests for MultiReadOnlySeekableByteChannel">

    @Test
    void multiChannelFromNoChannelsShouldBeEmpty() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            assertEmptyChannelBehavior(channel);
        }
    }

    @Test
    void multiChannelFromMultipleEmptyChannelsShouldBeEmpty() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            createInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY),
            createInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY))) {
            assertEmptyChannelBehavior(channel);
        }
    }

    @Test
    void multiChannelWithContentIsReadCorrectly() throws IOException {
        // This test uses a comprehensive helper to check various data chunking
        // and read buffer size strategies.
        assertMultiChannelBehavesCorrectly(TEST_DATA);
    }

    //</editor-fold>

    //<editor-fold desc="Sanity checks for the test's utility channel (SeekableInMemoryByteChannel)">

    @Test
    void inMemoryChannelBehavesAsExpectedWhenEmpty() throws IOException {
        try (SeekableByteChannel channel = createInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY)) {
            assertEmptyChannelBehavior(channel);
        }
    }

    @Test
    void inMemoryChannelWithContentIsReadCorrectly() throws IOException {
        try (SeekableByteChannel channel = createInMemoryChannel(TEST_DATA)) {
            // Verifies the baseline behavior of the utility channel with various buffer sizes.
            assertChannelContentAndBehavior(TEST_DATA, channel);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Assertion Helper Methods">

    /**
     * A comprehensive check that tests a MultiReadOnlySeekableByteChannel created from the given data.
     * <p>
     * It splits the data into various chunk sizes, creates a multi-channel for each,
     * and then verifies its behavior against the expected full data set.
     *
     * @param expectedData The complete data set the channel should represent.
     */
    private void assertMultiChannelBehavesCorrectly(final byte[] expectedData) throws IOException {
        // Test splitting the data into chunks of different sizes (from 1 to full length).
        for (int chunkSize = 1; chunkSize <= expectedData.length; chunkSize++) {
            final byte[][] chunks = groupDataIntoChunks(expectedData, chunkSize);
            try (SeekableByteChannel multiChannel = createMultiChannelFromChunks(chunks)) {
                // For each chunking strategy, test with various read buffer sizes.
                assertChannelContentAndBehavior(expectedData, multiChannel);
            }
        }
    }

    /**
     * Verifies that a given channel's content and behavior match the expected data.
     * <p>
     * This check is run with multiple different read buffer sizes to ensure correctness
     * under various reading conditions.
     *
     * @param expectedData The data the channel is expected to contain.
     * @param channel      The channel to test.
     */
    private void assertChannelContentAndBehavior(final byte[] expectedData, final SeekableByteChannel channel) throws IOException {
        // Test with various read buffer sizes, including smaller, equal, and larger than the content.
        for (int readBufferSize = 1; readBufferSize <= expectedData.length + 5; readBufferSize++) {
            verifyChannelRead(expectedData, channel, readBufferSize);
        }
    }

    /**
     * The core verification logic for a given channel and a specific read buffer size.
     * <p>
     * Asserts channel properties (size, position) and reads the entire channel to verify
     * its content byte-by-byte. It also checks that the channel is read efficiently.
     *
     * @param expectedData   The data the channel is expected to contain.
     * @param channel        The channel to test.
     * @param readBufferSize The size of the buffer to use for each read() call.
     */
    private void verifyChannelRead(final byte[] expectedData, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "readBufferSize " + readBufferSize;
        assertTrue(channel.isOpen(), context);
        assertEquals(expectedData.length, channel.size(), context);

        // Reset position and read from the start.
        channel.position(0);
        assertEquals(0, channel.position(), context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Reading into a zero-sized buffer should return 0: " + context);

        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedData.length + 100); // A buffer large enough for all content and padding.
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            final int remainingBeforeFlip = readBuffer.remaining();
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();

            // If this isn't the final read, the buffer should have been completely filled.
            if (resultBuffer.position() < expectedData.length) {
                assertEquals(0, remainingBeforeFlip, "Buffer should be full on intermediate reads: " + context);
            }

            // The buffer's position should reflect the number of bytes read.
            if (bytesRead == -1) {
                assertEquals(0, readBuffer.position(), "Buffer position should be 0 after EOF: " + context);
            } else {
                assertEquals(bytesRead, readBuffer.position(), "Buffer position should match bytes read: " + context);
            }
        }

        resultBuffer.flip();
        final byte[] actualData = new byte[resultBuffer.remaining()];
        resultBuffer.get(actualData);
        assertArrayEquals(expectedData, actualData, context);
    }

    /**
     * Verifies the behavior of a channel that is expected to be empty.
     *
     * @param channel The empty channel to test.
     */
    private void assertEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());

        // Reading an empty channel should immediately return -1 (EOF).
        assertEquals(-1, channel.read(buf));

        // Positioning past the end of an empty channel is allowed, but reading should still be EOF.
        channel.position(5);
        assertEquals(-1, channel.read(buf));

        channel.close();
        assertFalse(channel.isOpen());

        // Operations on a closed channel should fail.
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "read() on a closed channel");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "position() on a closed channel");
    }

    //</editor-fold>

    //<editor-fold desc="Factory and Utility Methods">

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of data chunks.
     * Each chunk is first placed into its own {@link SeekableInMemoryByteChannel}.
     */
    private SeekableByteChannel createMultiChannelFromChunks(final byte[][] dataChunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[dataChunks.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = createInMemoryChannel(dataChunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /** Creates a {@link SeekableInMemoryByteChannel} with the given data. */
    private SeekableByteChannel createInMemoryChannel(final byte[] data) {
        return new SeekableInMemoryByteChannel(data);
    }

    /** Splits a byte array into a list of smaller byte arrays of a given size. */
    private byte[][] groupDataIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        int idx = 0;
        while (idx < input.length) {
            final int nextIdx = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, nextIdx));
            idx = nextIdx;
        }
        return groups.toArray(new byte[0][]);
    }

    //</editor-fold>
}