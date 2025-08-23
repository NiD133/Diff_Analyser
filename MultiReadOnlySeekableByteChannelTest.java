package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the MultiReadOnlySeekableByteChannel class.
 * This class tests the behavior of concatenating multiple SeekableByteChannels.
 */
class MultiReadOnlySeekableByteChannelTest {

    /**
     * A SeekableByteChannel implementation that throws an IOException on close.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed = false;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("Channel close error");
        }

        @Override
        public boolean isOpen() {
            return !closed;
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
            return -1; // Simulate end-of-file
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public SeekableByteChannel truncate(final long size) {
            return this;
        }

        @Override
        public int write(final ByteBuffer src) {
            return 0;
        }
    }

    /**
     * Verifies that the channel correctly reads the expected byte array.
     */
    private void verifyReadOperation(final byte[] expected) throws IOException {
        for (int channelSize = 1; channelSize <= expected.length; channelSize++) {
            try (SeekableByteChannel singleChannel = createSingleChannel(expected)) {
                verifyChannelRead(expected, singleChannel);
            }
            try (SeekableByteChannel multiChannel = createMultiChannel(splitIntoChunks(expected, channelSize))) {
                verifyChannelRead(expected, multiChannel);
            }
        }
    }

    private void verifyChannelRead(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        for (int bufferSize = 1; bufferSize <= expected.length + 5; bufferSize++) {
            verifyChannelReadWithBufferSize(expected, channel, bufferSize);
        }
    }

    private void verifyChannelReadWithBufferSize(final byte[] expected, final SeekableByteChannel channel, final int bufferSize) throws IOException {
        assertTrue(channel.isOpen(), "Buffer size: " + bufferSize);
        assertEquals(expected.length, channel.size(), "Buffer size: " + bufferSize);
        channel.position(0);
        assertEquals(0, channel.position(), "Buffer size: " + bufferSize);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Buffer size: " + bufferSize);

        ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length + 100);
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
        }

        resultBuffer.flip();
        byte[] resultArray = new byte[resultBuffer.remaining()];
        resultBuffer.get(resultArray);
        assertArrayEquals(expected, resultArray, "Buffer size: " + bufferSize);
    }

    private void verifyEmptyChannel(final SeekableByteChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buffer));

        channel.position(5);
        assertEquals(-1, channel.read(buffer));

        channel.close();
        assertFalse(channel.isOpen());

        assertThrows(ClosedChannelException.class, () -> channel.read(buffer));
        assertThrows(ClosedChannelException.class, () -> channel.position(100));
    }

    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        List<byte[]> chunks = new ArrayList<>();
        int index = 0;
        while (index + chunkSize <= input.length) {
            chunks.add(Arrays.copyOfRange(input, index, index + chunkSize));
            index += chunkSize;
        }
        if (index < input.length) {
            chunks.add(Arrays.copyOfRange(input, index, input.length));
        }
        return chunks.toArray(new byte[0][]);
    }

    private SeekableByteChannel createEmptyChannel() {
        return createSingleChannel(new byte[0]);
    }

    private SeekableByteChannel createMultiChannel(final byte[][] byteArrays) {
        SeekableByteChannel[] channels = Arrays.stream(byteArrays)
                .map(this::createSingleChannel)
                .toArray(SeekableByteChannel[]::new);
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    private SeekableByteChannel createSingleChannel(final byte[] byteArray) {
        return new SeekableInMemoryByteChannel(byteArray);
    }

    @Test
    void testNegativePositionThrowsException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel())) {
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
        }
    }

    @Test
    void testTruncateThrowsException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel())) {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
        }
    }

    @Test
    void testWriteThrowsException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel())) {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(10)));
        }
    }

    private SeekableByteChannel createTestChannel() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel());
    }

    @Test
    void testSingleByteRead() throws IOException {
        verifyReadOperation(new byte[]{0});
    }

    @Test
    void testStringRead() throws IOException {
        verifyReadOperation("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(UTF_8));
    }

    @Test
    void testCloseIsIdempotent() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertFalse(channel.isOpen());
            channel.close();
            assertFalse(channel.isOpen());
        }
    }

    @Test
    void testCloseAllChannelsAndThrowException() {
        SeekableByteChannel[] channels = {new ThrowingSeekableByteChannel(), new ThrowingSeekableByteChannel()};
        SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
        assertThrows(IOException.class, multiChannel::close);
        assertFalse(channels[0].isOpen());
        assertFalse(channels[1].isOpen());
    }

    @Test
    void testConstructorThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> new MultiReadOnlySeekableByteChannel(null));
    }

    @Test
    void testForFilesThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
    }

    @Test
    void testSingleElementReturnsIdentity() throws IOException {
        try (SeekableByteChannel emptyChannel = createEmptyChannel();
             SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(emptyChannel)) {
            assertSame(emptyChannel, multiChannel);
        }
    }

    @Test
    void testForSeekableByteChannelsThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
    }

    @Test
    void testReadFromPositionAfterEndReturnsEOF() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.position(2);
            assertEquals(2, channel.position());
            ByteBuffer readBuffer = ByteBuffer.allocate(5);
            assertEquals(-1, channel.read(readBuffer));
        }
    }

    @Test
    void testReferenceBehaviorForEmptyChannel() throws IOException {
        verifyEmptyChannel(createEmptyChannel());
    }

    @Test
    void testThrowsClosedChannelExceptionOnClosedChannelPositionSet() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.position(0));
        }
    }

    @Test
    void testThrowsClosedChannelExceptionOnClosedChannelSizeRead() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.size());
        }
    }

    @Test
    void testThrowsIOExceptionOnNegativePosition() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
        }
    }

    @Test
    void testTwoEmptyChannelsConcatenateAsEmptyChannel() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel())) {
            verifyEmptyChannel(channel);
        }
    }

    @Test
    void testGroupedByteArray() {
        assertArrayEquals(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7}},
                splitIntoChunks(new byte[]{1, 2, 3, 4, 5, 6, 7}, 3));
        assertArrayEquals(new byte[][]{{1, 2, 3}, {4, 5, 6}},
                splitIntoChunks(new byte[]{1, 2, 3, 4, 5, 6}, 3));
        assertArrayEquals(new byte[][]{{1, 2, 3}, {4, 5}},
                splitIntoChunks(new byte[]{1, 2, 3, 4, 5}, 3));
    }

    @Test
    @Disabled("Spec violation: position() on closed channel")
    void testPositionOnClosedChannelThrowsException() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            channel.position();
        }
    }
}