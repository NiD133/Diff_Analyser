package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.Test;

/**
 * Understandable and maintainable tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "0123456789".getBytes(StandardCharsets.UTF_8);

    // --- Constructor Tests ---

    @Test
    public void defaultConstructor_shouldCreateEmptyChannel() {
        // Act
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Assert
        assertEquals("Position should be 0 for a new empty channel", 0L, channel.position());
        assertEquals("Size should be 0 for a new empty channel", 0L, channel.size());
        assertEquals("Backing array should be empty", 0, channel.array().length);
    }

    @Test
    public void byteArrayConstructor_shouldInitializeChannelWithGivenData() {
        // Arrange
        byte[] initialContent = Arrays.copyOf(TEST_DATA, TEST_DATA.length);

        // Act
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialContent);

        // Assert
        assertEquals("Position should be 0 initially", 0L, channel.position());
        assertEquals("Size should match the initial data length", initialContent.length, channel.size());
        assertArrayEquals("Backing array should contain the initial data", initialContent, channel.array());
    }

    @Test
    public void sizeConstructor_shouldCreateChannelWithAllocatedSize() {
        // Arrange
        int initialSize = 1024;

        // Act
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialSize);

        // Assert
        assertEquals("Position should be 0 initially", 0L, channel.position());
        assertEquals("Size should match the allocated size", initialSize, channel.size());
        assertEquals("Backing array length should match the allocated size", initialSize, channel.array().length);
    }

    @Test(expected = NullPointerException.class)
    public void byteArrayConstructor_shouldThrowException_forNullArray() {
        // Act
        new SeekableInMemoryByteChannel((byte[]) null);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void sizeConstructor_shouldThrowException_forNegativeSize() {
        // Act
        new SeekableInMemoryByteChannel(-1);
    }

    // --- State and Lifecycle Tests (isOpen, close) ---

    @Test
    public void isOpen_shouldReturnTrueForNewChannelAndFalseAfterClose() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Assert (before close)
        assertTrue("Channel should be open upon creation", channel.isOpen());

        // Act
        channel.close();

        // Assert (after close)
        assertFalse("Channel should be closed after calling close()", channel.isOpen());
    }

    @Test
    public void close_shouldBeIdempotent() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act
        channel.close();
        channel.close(); // Calling close again should not cause an error

        // Assert
        assertFalse("Channel should remain closed", channel.isOpen());
    }

    // --- Read Tests ---

    @Test
    public void read_shouldReadBytesAndAdvancePosition() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        ByteBuffer buffer = ByteBuffer.allocate(4);

        // Act
        int bytesRead = channel.read(buffer);

        // Assert
        assertEquals("Should read 4 bytes", 4, bytesRead);
        assertEquals("Position should advance by 4", 4L, channel.position());
        assertArrayEquals("Buffer should contain the first 4 bytes", "0123".getBytes(StandardCharsets.UTF_8), buffer.array());
    }

    @Test
    public void read_shouldReturnEOF_whenReadingPastTheEnd() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        channel.position(TEST_DATA.length); // Move position to the end
        ByteBuffer buffer = ByteBuffer.allocate(1);

        // Act
        int bytesRead = channel.read(buffer);

        // Assert
        assertEquals("Reading at the end of the channel should return -1 (EOF)", -1, bytesRead);
    }
    
    @Test
    public void read_shouldReadZeroBytes_whenBufferIsEmpty() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);

        // Act
        int bytesRead = channel.read(emptyBuffer);

        // Assert
        assertEquals("Reading into an empty buffer should read 0 bytes", 0, bytesRead);
        assertEquals("Position should not change", 0L, channel.position());
    }

    @Test(expected = ClosedChannelException.class)
    public void read_shouldThrowException_whenChannelIsClosed() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        channel.close();

        // Act
        channel.read(ByteBuffer.allocate(1));
    }

    // --- Write Tests ---

    @Test
    public void write_shouldAppendDataAndIncreaseSize() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(new byte[5]);
        channel.position(5); // Position at the end to append
        ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes(StandardCharsets.UTF_8));

        // Act
        int bytesWritten = channel.write(buffer);

        // Assert
        assertEquals("Should write 5 bytes", 5, bytesWritten);
        assertEquals("Size should increase to 10", 10L, channel.size());
        assertEquals("Position should be at the new end", 10L, channel.position());
        byte[] expected = new byte[10];
        System.arraycopy("abcde".getBytes(StandardCharsets.UTF_8), 0, expected, 5, 5);
        assertArrayEquals("Appended data should be at the end of the channel's array", expected, channel.array());
    }

    @Test
    public void write_shouldOverwriteData_whenPositionIsInTheMiddle() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        channel.position(4); // Position to overwrite "45"
        ByteBuffer buffer = ByteBuffer.wrap("--".getBytes(StandardCharsets.UTF_8));

        // Act
        int bytesWritten = channel.write(buffer);

        // Assert
        assertEquals("Should write 2 bytes", 2, bytesWritten);
        assertEquals("Position should advance by 2", 6L, channel.position());
        assertEquals("Size should remain unchanged", TEST_DATA.length, channel.size());
        assertArrayEquals("Data should be overwritten", "0123--6789".getBytes(StandardCharsets.UTF_8), channel.array());
    }

    @Test(expected = ClosedChannelException.class)
    public void write_shouldThrowException_whenChannelIsClosed() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();

        // Act
        channel.write(ByteBuffer.wrap(TEST_DATA));
    }

    // --- Position Tests ---

    @Test
    public void position_shouldSetAndGetPosition() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);

        // Act
        channel.position(5L);

        // Assert
        assertEquals("Position should be updated to 5", 5L, channel.position());
    }

    @Test(expected = IOException.class)
    public void position_shouldThrowException_forNegativePosition() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act
        channel.position(-1L);
    }

    @Test(expected = ClosedChannelException.class)
    public void position_shouldThrowException_whenChannelIsClosed() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();

        // Act
        channel.position(1L);
    }

    // --- Truncate Tests ---

    @Test
    public void truncate_shouldReduceSizeAndAdjustPosition() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        channel.position(8L); // Position is within original size, but past new truncated size

        // Act
        channel.truncate(5L);

        // Assert
        assertEquals("Size should be truncated to 5", 5L, channel.size());
        assertEquals("Position should be adjusted to the new size", 5L, channel.position());
    }

    @Test
    public void truncate_shouldNotIncreaseSize_forLargerNewSize() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        long originalSize = channel.size();

        // Act
        channel.truncate(originalSize + 100);

        // Assert
        assertEquals("Size should not change when truncating to a larger size", originalSize, channel.size());
    }
    
    @Test
    public void truncate_shouldNotThrowException_whenChannelIsClosed() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA);
        channel.close();

        // Act & Assert (no exception should be thrown)
        channel.truncate(5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void truncate_shouldThrowException_forNegativeSize() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        // Act
        channel.truncate(-1L);
    }
}