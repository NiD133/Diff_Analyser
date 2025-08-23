package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SeekableInMemoryByteChannelTest {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    @Test
    void testCloseIsIdempotent() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            assertFalse(channel.isOpen(), "Channel should be closed after first close");
            channel.close();
            assertFalse(channel.isOpen(), "Channel should remain closed after second close");
        }
    }

    @Test
    void testReadingFromPositionBeyondEndReturnsEOF() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.position(2);
            assertEquals(2, channel.position(), "Position should be set to 2");
            ByteBuffer buffer = ByteBuffer.allocate(5);
            assertEquals(-1, channel.read(buffer), "Reading beyond end should return EOF");
        }
    }

    @Test
    void testReadContentsCorrectly() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            ByteBuffer buffer = ByteBuffer.allocate(testData.length);
            int bytesRead = channel.read(buffer);
            assertEquals(testData.length, bytesRead, "Should read all bytes");
            assertArrayEquals(testData, buffer.array(), "Read data should match test data");
            assertEquals(testData.length, channel.position(), "Position should be at the end of data");
        }
    }

    @Test
    void testReadWithLargerBuffer() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            ByteBuffer buffer = ByteBuffer.allocate(testData.length + 1);
            int bytesRead = channel.read(buffer);
            assertEquals(testData.length, bytesRead, "Should read all bytes");
            assertArrayEquals(testData, Arrays.copyOf(buffer.array(), testData.length), "Read data should match test data");
            assertEquals(testData.length, channel.position(), "Position should be at the end of data");
        }
    }

    @Test
    void testReadFromSpecificPosition() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            channel.position(5L);
            int bytesRead = channel.read(buffer);
            assertEquals(4L, bytesRead, "Should read 4 bytes from position 5");
            assertEquals("data", new String(buffer.array(), UTF_8), "Read data should match 'data'");
            assertEquals(testData.length, channel.position(), "Position should be at the end of data");
        }
    }

    @Test
    void testSetPosition() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            long posAtFour = channel.position(4L).position();
            long posAtEnd = channel.position(testData.length).position();
            long posPastEnd = channel.position(testData.length + 1L).position();
            assertEquals(4L, posAtFour, "Position should be set to 4");
            assertEquals(channel.size(), posAtEnd, "Position should be at the end of data");
            assertEquals(testData.length + 1L, posPastEnd, "Position should be set past the end");
        }
    }

    @Test
    void testPositionAfterTruncate() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            channel.position(testData.length);
            channel.truncate(4L);
            assertEquals(4L, channel.position(), "Position should be set to 4 after truncate");
            assertEquals(4L, channel.size(), "Size should be 4 after truncate");
        }
    }

    @Test
    void testEOFWhenPositionAtEnd() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            ByteBuffer buffer = ByteBuffer.allocate(testData.length);
            channel.position(testData.length + 1);
            int bytesRead = channel.read(buffer);
            assertEquals(0L, buffer.position(), "Buffer position should be 0");
            assertEquals(-1, bytesRead, "Reading beyond end should return EOF");
            assertEquals(-1, channel.read(buffer), "Reading again should still return EOF");
        }
    }

    @Test
    void testExceptionOnReadClosedChannel() {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(1)), "Reading from closed channel should throw exception");
    }

    @Test
    void testExceptionOnWriteClosedChannel() {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        assertThrows(ClosedChannelException.class, () -> channel.write(ByteBuffer.allocate(1)), "Writing to closed channel should throw exception");
    }

    @Test
    void testExceptionOnInvalidPosition() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IOException.class, () -> channel.position(Integer.MAX_VALUE + 1L), "Setting position beyond limit should throw exception");
        }
    }

    @Test
    void testExceptionOnInvalidTruncateSize() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.truncate(Integer.MAX_VALUE + 1L), "Truncating to invalid size should throw exception");
        }
    }

    @Test
    void testTruncateContents() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            channel.truncate(4);
            byte[] truncatedData = Arrays.copyOf(channel.array(), (int) channel.size());
            assertEquals("Some", new String(truncatedData, UTF_8), "Truncated data should match 'Some'");
        }
    }

    @Test
    void testWriteData() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            ByteBuffer buffer = ByteBuffer.wrap(testData);
            int bytesWritten = channel.write(buffer);
            assertEquals(testData.length, bytesWritten, "Should write all bytes");
            assertArrayEquals(testData, Arrays.copyOf(channel.array(), (int) channel.size()), "Written data should match test data");
            assertEquals(testData.length, channel.position(), "Position should be at the end of data");
        }
    }

    @Test
    void testWriteDataAfterPositionSet() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            ByteBuffer buffer = ByteBuffer.wrap(testData);
            ByteBuffer expectedData = ByteBuffer.allocate(testData.length + 5).put(testData, 0, 5).put(testData);
            channel.position(5L);
            int bytesWritten = channel.write(buffer);
            assertEquals(testData.length, bytesWritten, "Should write all bytes");
            assertArrayEquals(expectedData.array(), Arrays.copyOf(channel.array(), (int) channel.size()), "Written data should match expected data");
            assertEquals(testData.length + 5, channel.position(), "Position should be at the end of written data");
        }
    }

    @Test
    void testClosedChannelExceptionOnPositionSetClosedChannel() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.position(0), "Setting position on closed channel should throw exception");
        }
    }

    @Test
    void testIllegalArgumentExceptionOnTruncateNegativeSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.truncate(-1), "Truncating to negative size should throw exception");
        }
    }

    @Test
    void testIOExceptionOnNegativePosition() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IOException.class, () -> channel.position(-1), "Setting negative position should throw exception");
        }
    }

    @Test
    void testTruncateDoesNotChangeSmallPosition() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            channel.position(1);
            channel.truncate(testData.length - 1);
            assertEquals(testData.length - 1, channel.size(), "Size should be reduced");
            assertEquals(1, channel.position(), "Position should remain unchanged");
        }
    }

    @Test
    void testTruncateMovesPositionWhenNewSizeIsSmaller() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            channel.position(2 * testData.length);
            channel.truncate(testData.length + 1);
            assertEquals(testData.length, channel.size(), "Size should be reduced");
            assertEquals(testData.length + 1, channel.position(), "Position should be adjusted");
        }
    }

    @Test
    void testTruncateMovesPositionWhenShrinkingBeyondPosition() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            channel.position(4);
            channel.truncate(3);
            assertEquals(3, channel.size(), "Size should be reduced");
            assertEquals(3, channel.position(), "Position should be adjusted");
        }
    }

    @Test
    void testTruncateToLargerSizeDoesNotChange() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            assertEquals(testData.length, channel.size(), "Initial size should match test data length");
            channel.truncate(testData.length + 1);
            assertEquals(testData.length, channel.size(), "Size should remain unchanged");
            ByteBuffer buffer = ByteBuffer.allocate(testData.length);
            assertEquals(testData.length, channel.read(buffer), "Should read all bytes");
            assertArrayEquals(testData, Arrays.copyOf(buffer.array(), testData.length), "Read data should match test data");
        }
    }

    @Test
    void testTruncateToCurrentSizeDoesNotChange() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
            assertEquals(testData.length, channel.size(), "Initial size should match test data length");
            channel.truncate(testData.length);
            assertEquals(testData.length, channel.size(), "Size should remain unchanged");
            ByteBuffer buffer = ByteBuffer.allocate(testData.length);
            assertEquals(testData.length, channel.read(buffer), "Should read all bytes");
            assertArrayEquals(testData, Arrays.copyOf(buffer.array(), testData.length), "Read data should match test data");
        }
    }

    @Test
    @Disabled("we deliberately violate the spec")
    void testClosedChannelExceptionOnPositionReadClosedChannel() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.position();
        }
    }

    @Test
    @Disabled("we deliberately violate the spec")
    void testClosedChannelExceptionOnSizeReadClosedChannel() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.size();
        }
    }

    @Test
    @Disabled("we deliberately violate the spec")
    void testClosedChannelExceptionOnTruncateClosedChannel() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.truncate(0);
        }
    }

    @Test
    void testWritingToPositionAfterEndGrowsChannel() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.position(2);
            assertEquals(2, channel.position(), "Position should be set to 2");
            ByteBuffer buffer = ByteBuffer.wrap(testData);
            assertEquals(testData.length, channel.write(buffer), "Should write all bytes");
            assertEquals(testData.length + 2, channel.size(), "Size should grow to accommodate new data");

            channel.position(2);
            ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
            channel.read(readBuffer);
            assertArrayEquals(testData, Arrays.copyOf(readBuffer.array(), testData.length), "Read data should match written data");
        }
    }
}