package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    private static final byte[] TEST_DATA = "0123456789".getBytes(UTF_8);

    /**
     * Splits a byte array into chunks of a specified size. The last chunk may be smaller.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int idx = 0; idx < input.length; idx += chunkSize) {
            final int end = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, end));
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Asserts that a channel behaves as expected for a given data set and read buffer size.
     * This method reads the entire channel content and verifies its properties.
     *
     * @param channel        The channel to test.
     * @param expectedData   The byte array the channel is expected to contain.
     * @param readBufferSize The size of the buffer to use for each read operation.
     */
    private void assertChannelBehavesCorrectly(final SeekableByteChannel channel, final byte[] expectedData, final int readBufferSize) throws IOException {
        final String context = "readBufferSize=" + readBufferSize;
        channel.position(0); // Reset for each test run

        assertTrue(channel.isOpen(), context);
        assertEquals(expectedData.length, channel.size(), context);
        assertEquals(0, channel.position(), context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Reading into a zero-capacity buffer should return 0. " + context);

        // Read all content from the channel
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedData.length == 0 ? 1 : expectedData.length);
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
        }

        resultBuffer.flip();
        final byte[] actualData = new byte[resultBuffer.remaining()];
        resultBuffer.get(actualData);

        assertArrayEquals(expectedData, actualData, "Channel content should match expected data. " + context);
    }

    @Nested
    @DisplayName("Read Tests")
    class ReadTests {

        static Stream<Arguments> readScenarios() {
            return Stream.of(
                Arguments.of("as a single large chunk", TEST_DATA.length),
                Arguments.of("in chunks of size 1", 1),
                Arguments.of("in chunks of size 3", 3),
                Arguments.of("with a chunk size larger than the data", TEST_DATA.length + 5)
            );
        }

        @ParameterizedTest(name = "should read data correctly when content is split {0}")
        @MethodSource("readScenarios")
        void shouldReadDataCorrectlyWhenContentIsSplit(final String description, final int componentChannelSize) throws IOException {
            final byte[][] chunks = splitIntoChunks(TEST_DATA, componentChannelSize);
            try (SeekableByteChannel channel = createMultiChannel(chunks)) {
                // Test with various read buffer sizes to cover different read patterns
                for (int readBufferSize = 1; readBufferSize <= TEST_DATA.length + 5; readBufferSize++) {
                    assertChannelBehavesCorrectly(channel, TEST_DATA, readBufferSize);
                }
            }
        }

        @Test
        void shouldBehaveCorrectlyWhenEmpty() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[][]{{}, {}})) {
                final ByteBuffer buf = ByteBuffer.allocate(10);
                assertTrue(channel.isOpen());
                assertEquals(0, channel.size());
                assertEquals(0, channel.position());
                assertEquals(-1, channel.read(buf), "Reading an empty channel should return -1");

                // Position beyond the end and try to read
                channel.position(5);
                assertEquals(-1, channel.read(buf), "Reading from beyond the end should return -1");
            }
        }
    }

    @Nested
    @DisplayName("Write Tests")
    class WriteTests {

        @Test
        void truncateShouldThrowNonWritableChannelException() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[][]{TEST_DATA})) {
                assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
            }
        }

        @Test
        void writeShouldThrowNonWritableChannelException() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[][]{TEST_DATA})) {
                assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.wrap(TEST_DATA)));
            }
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        void shouldBeClosedAfterClosing() throws IOException {
            final SeekableByteChannel channel = createMultiChannel(new byte[][]{TEST_DATA});
            assertTrue(channel.isOpen());
            channel.close();
            assertFalse(channel.isOpen());
        }

        @Test
        void operationsShouldThrowExceptionAfterClose() throws IOException {
            final SeekableByteChannel channel = createMultiChannel(new byte[][]{TEST_DATA});
            channel.close();

            final ByteBuffer buf = ByteBuffer.allocate(10);
            assertThrows(ClosedChannelException.class, () -> channel.read(buf));
            assertThrows(ClosedChannelException.class, () -> channel.position(1));
            assertThrows(ClosedChannelException.class, channel::size);
        }

        @Test
        void closeShouldCloseAllUnderlyingChannels() throws IOException {
            final CloseTrackingChannel c1 = new CloseTrackingChannel(TEST_DATA);
            final CloseTrackingChannel c2 = new CloseTrackingChannel(TEST_DATA);

            try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(c1, c2)) {
                // The channel is used inside the try-with-resources block
            }

            assertTrue(c1.isClosed(), "First underlying channel should be closed");
            assertTrue(c2.isClosed(), "Second underlying channel should be closed");
        }

        @Test
        void closeShouldPropagateExceptionAndStillCloseOtherChannels() {
            final ThrowingSeekableByteChannel throwingChannel = new ThrowingSeekableByteChannel();
            final CloseTrackingChannel trackingChannel = new CloseTrackingChannel(TEST_DATA);

            final SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(throwingChannel, trackingChannel);

            assertThrows(IOException.class, multiChannel::close, "Should propagate IOException from underlying channel");
            assertTrue(trackingChannel.isClosed(), "Should close subsequent channels even if a prior one threw an exception");
        }
    }

    /** A channel that tracks whether it has been closed. */
    private static class CloseTrackingChannel extends SeekableInMemoryByteChannel {
        private boolean closed = false;

        CloseTrackingChannel(final byte[] data) {
            super(data);
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    /** A channel that always throws an IOException on close. */
    private static class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean open = true;

        @Override
        public void close() throws IOException {
            open = false;
            throw new IOException("Simulated close failure");
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public long position() {
            return 0;
        }

        @Override
        public SeekableByteChannel position(final long newPosition) {
            return this;
        }

        @Override
        public int read(final ByteBuffer dst) {
            return -1;
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public SeekableByteChannel truncate(final long size) {
            throw new NonWritableChannelException();
        }

        @Override
        public int write(final ByteBuffer src) {
            throw new NonWritableChannelException();
        }
    }
}