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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    private static final byte[] DATA_PART_1 = "Apache ".getBytes(UTF_8);
    private static final byte[] DATA_PART_2 = "Commons ".getBytes(UTF_8);
    private static final byte[] DATA_PART_3 = "Compress".getBytes(UTF_8);
    private static final byte[] FULL_DATA = "Apache Commons Compress".getBytes(UTF_8);

    //
    // Test Cases
    //

    @Test
    void shouldHaveSizeOfZeroForEmptyChannel() throws IOException {
        // An empty channel can be created from no channels or from empty channels
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            assertEmptyChannelBehavior(channel);
        }
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel
            .forSeekableByteChannels(createInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY), createInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY))) {
            assertEmptyChannelBehavior(channel);
        }
    }

    @Test
    void shouldCalculateCorrectSizeFromUnderlyingChannels() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1, DATA_PART_2, DATA_PART_3)) {
            long expectedSize = (long) DATA_PART_1.length + DATA_PART_2.length + DATA_PART_3.length;
            assertEquals(expectedSize, channel.size());
        }
    }

    @Test
    void shouldReadSequentiallyAcrossAllChannels() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1, DATA_PART_2, DATA_PART_3)) {
            final byte[] result = new byte[FULL_DATA.length];
            final ByteBuffer buffer = ByteBuffer.wrap(result);

            int bytesRead = channel.read(buffer);

            assertEquals(FULL_DATA.length, bytesRead);
            assertArrayEquals(FULL_DATA, result);
            assertEquals(-1, channel.read(ByteBuffer.allocate(1)), "Should return EOF when fully read");
        }
    }

    @Test
    void shouldSetPositionAndReadFromCorrectOffset() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1, DATA_PART_2, DATA_PART_3)) {
            // Position into the middle of the second channel ("Commons ")
            long newPosition = DATA_PART_1.length + 2; // "om"
            channel.position(newPosition);
            assertEquals(newPosition, channel.position());

            ByteBuffer buffer = ByteBuffer.allocate(2);
            channel.read(buffer);
            assertArrayEquals("om".getBytes(UTF_8), buffer.array());
            assertEquals(newPosition + 2, channel.position(), "Position should advance after read");
        }
    }

    @Test
    void shouldAllowSettingPositionBeyondSize() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1)) {
            long positionBeyondSize = channel.size() + 5;
            channel.position(positionBeyondSize);
            assertEquals(positionBeyondSize, channel.position());

            // Reading from a position beyond the size should result in EOF
            assertEquals(-1, channel.read(ByteBuffer.allocate(1)));
        }
    }

    @Test
    void shouldThrowExceptionOnWrite() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1)) {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
        }
    }

    @Test
    void shouldThrowExceptionOnTruncate() throws IOException {
        try (SeekableByteChannel channel = createMultiChannel(DATA_PART_1)) {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1L));
        }
    }

    @Test
    void shouldCloseAllUnderlyingChannels() throws IOException {
        SeekableInMemoryByteChannel ch1 = createInMemoryChannel(DATA_PART_1);
        SeekableInMemoryByteChannel ch2 = createInMemoryChannel(DATA_PART_2);

        try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(ch1, ch2)) {
            assertTrue(ch1.isOpen(), "Channel 1 should be open initially");
            assertTrue(ch2.isOpen(), "Channel 2 should be open initially");
            multiChannel.close();
            assertFalse(multiChannel.isOpen(), "Multi-channel should be closed");
            assertFalse(ch1.isOpen(), "Channel 1 should be closed by multi-channel");
            assertFalse(ch2.isOpen(), "Channel 2 should be closed by multi-channel");
        }
    }

    @Test
    @DisplayName("close() should propagate exceptions but still attempt to close all underlying channels")
    void closeShouldPropagateExceptionAfterClosingAll() {
        SeekableInMemoryByteChannel ch1 = createInMemoryChannel(DATA_PART_1);
        ThrowingSeekableByteChannel ch2 = new ThrowingSeekableByteChannel();
        SeekableInMemoryByteChannel ch3 = createInMemoryChannel(DATA_PART_3);

        try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(ch1, ch2, ch3)) {
            assertThrows(IOException.class, multiChannel::close, "IOException from ch2 should be propagated");
        }

        assertFalse(ch1.isOpen(), "Channel 1 should be closed even if a later channel throws");
        assertFalse(ch2.isOpen(), "The throwing channel should be marked as closed");
        assertFalse(ch3.isOpen(), "Channel 3 should be closed even if a prior channel throws");
    }

    @ParameterizedTest
    @MethodSource("getTestDataForIntegrityCheck")
    @DisplayName("Comprehensive read integrity check with various chunk and buffer sizes")
    void comprehensiveReadIntegrityTest(final byte[] testData) throws IOException {
        runComprehensiveReadTest(testData);
    }

    //
    // Helper Methods and Classes
    //

    /**
     * Provides test data for the comprehensive integrity check.
     */
    private static Stream<byte[]> getTestDataForIntegrityCheck() {
        return Stream.of(
            "Apache Commons Compress".getBytes(UTF_8),
            "a".getBytes(UTF_8),
            new byte[0]
        );
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from byte arrays.
     */
    private SeekableByteChannel createMultiChannel(final byte[]... arrays) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[arrays.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = createInMemoryChannel(arrays[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a {@link SeekableInMemoryByteChannel} from a byte array.
     */
    private SeekableInMemoryByteChannel createInMemoryChannel(final byte[] arr) {
        return new SeekableInMemoryByteChannel(arr);
    }

    /**
     * Splits a byte array into chunks of a given size.
     */
    private byte[][] group(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int idx = 0; idx < input.length; idx += chunkSize) {
            int end = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, end));
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Asserts the behavior of an empty channel.
     */
    private void assertEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buf));

        // Position can be set, but read should still return EOF
        channel.position(5);
        assertEquals(-1, channel.read(buf));

        channel.close();
        assertFalse(channel.isOpen());
        assertThrows(ClosedChannelException.class, () -> channel.read(buf));
        assertThrows(ClosedChannelException.class, () -> channel.position(100));
    }

    /**
     * This is the original, exhaustive test logic, preserved for its thoroughness.
     * It tests reading by splitting the input data into various chunk sizes to form the
     * MultiReadOnlySeekableByteChannel, and then reading it back with various buffer sizes.
     *
     * @param expected the full data that the channel should contain.
     */
    private void runComprehensiveReadTest(final byte[] expected) throws IOException {
        // Test with different underlying channel sizes
        for (int channelSize = 1; channelSize <= expected.length; channelSize++) {
            // Sanity check that the test logic works for a single in-memory channel
            try (SeekableByteChannel single = createInMemoryChannel(expected)) {
                runReadTestWithVariousBufferSizes(expected, single);
            }
            // The actual test against our MultiReadOnlySeekableByteChannel instance
            try (SeekableByteChannel multi = createMultiChannel(group(expected, channelSize))) {
                runReadTestWithVariousBufferSizes(expected, multi);
            }
        }
        // Also test with an empty expected array
        if (expected.length == 0) {
            try (SeekableByteChannel multi = createMultiChannel(group(expected, 1))) {
                runReadTestWithVariousBufferSizes(expected, multi);
            }
        }
    }

    private void runReadTestWithVariousBufferSizes(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        // Test with different read buffer sizes
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            assertChannelContentsMatch(expected, channel, readBufferSize);
        }
    }

    private void assertChannelContentsMatch(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "readBufferSize=" + readBufferSize;
        assertTrue(channel.isOpen(), context);
        assertEquals(expected.length, channel.size(), context);

        // Reset position for each run
        channel.position(0);
        assertEquals(0, channel.position(), context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Reading into an empty buffer should do nothing");

        // Read the entire channel content
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length == 0 ? 1 : expected.length);
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);
        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
        }

        resultBuffer.flip();
        final byte[] actual = new byte[resultBuffer.remaining()];
        resultBuffer.get(actual);
        assertArrayEquals(expected, actual, context);
    }

    /**
     * A test utility class that throws an IOException on close, used to verify
     * resource cleanup logic under failure conditions.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("Simulated close failure");
        }

        @Override
        public boolean isOpen() {
            return !closed;
        }

        @Override
        public long position() { return 0; }

        @Override
        public SeekableByteChannel position(final long newPosition) { return this; }

        @Override
        public int read(final ByteBuffer dst) { return -1; }

        @Override
        public long size() { return 0; }

        @Override
        public SeekableByteChannel truncate(final long size) { return this; }

        @Override
        public int write(final ByteBuffer src) { return 0; }
    }
}