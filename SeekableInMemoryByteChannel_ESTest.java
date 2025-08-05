package org.apache.commons.compress.utils;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;

/**
 * Test suite for SeekableInMemoryByteChannel functionality.
 * Tests cover construction, reading, writing, positioning, truncation, and error conditions.
 */
public class SeekableInMemoryByteChannelTest {

    private SeekableInMemoryByteChannel channel;

    // ========== Construction Tests ==========

    @Test
    public void shouldCreateEmptyChannelWithDefaultConstructor() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        
        assertEquals("Empty channel should have size 0", 0L, channel.size());
        assertEquals("Empty channel should have position 0", 0L, channel.position());
        assertTrue("New channel should be open", channel.isOpen());
        assertArrayEquals("Empty channel should have empty array", new byte[]{}, channel.array());
    }

    @Test
    public void shouldCreateChannelWithSpecifiedSize() throws Exception {
        final int expectedSize = 122;
        channel = new SeekableInMemoryByteChannel(expectedSize);
        
        assertEquals("Channel should have specified size", expectedSize, channel.size());
        assertEquals("New channel should start at position 0", 0L, channel.position());
        assertTrue("New channel should be open", channel.isOpen());
    }

    @Test
    public void shouldCreateChannelFromByteArray() throws Exception {
        byte[] initialData = {1, 2, 3, 4, 5};
        channel = new SeekableInMemoryByteChannel(initialData);
        
        assertEquals("Channel size should match array length", initialData.length, channel.size());
        assertEquals("New channel should start at position 0", 0L, channel.position());
        assertArrayEquals("Channel array should match initial data", initialData, channel.array());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCreatedWithNullArray() {
        new SeekableInMemoryByteChannel((byte[]) null);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void shouldThrowExceptionWhenCreatedWithNegativeSize() {
        new SeekableInMemoryByteChannel(-1);
    }

    // ========== Reading Tests ==========

    @Test
    public void shouldReadDataFromChannel() throws Exception {
        byte[] initialData = new byte[3];
        channel = new SeekableInMemoryByteChannel(initialData);
        ByteBuffer buffer = ByteBuffer.allocate(2);
        
        int bytesRead = channel.read(buffer);
        
        assertEquals("Should read requested bytes", 2, bytesRead);
        assertEquals("Position should advance by bytes read", 2L, channel.position());
    }

    @Test
    public void shouldReturnMinusOneWhenReadingBeyondEndOfChannel() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        int bytesRead = channel.read(buffer);
        
        assertEquals("Should return -1 when reading beyond end", -1, bytesRead);
    }

    @Test
    public void shouldReadZeroBytesFromEmptyBuffer() throws Exception {
        byte[] initialData = {1, 2, 3, 4, 5};
        channel = new SeekableInMemoryByteChannel(initialData);
        channel.position(1L);
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);
        
        int bytesRead = channel.read(emptyBuffer);
        
        assertEquals("Should read 0 bytes from empty buffer", 0, bytesRead);
        assertEquals("Position should not change", 1L, channel.position());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenReadingWithNullBuffer() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.read(null);
    }

    @Test(expected = ClosedChannelException.class)
    public void shouldThrowExceptionWhenReadingFromClosedChannel() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        channel.read(buffer);
    }

    // ========== Writing Tests ==========

    @Test
    public void shouldWriteDataToChannel() throws Exception {
        byte[] initialData = new byte[1];
        channel = new SeekableInMemoryByteChannel(initialData);
        ByteBuffer buffer = ByteBuffer.allocate(86);
        
        int bytesWritten = channel.write(buffer);
        
        assertEquals("Should write all buffer data", 86, bytesWritten);
        assertEquals("Channel should expand to accommodate data", 86L, channel.size());
    }

    @Test
    public void shouldWriteZeroBytesFromEmptyBuffer() throws Exception {
        byte[] initialData = new byte[5];
        channel = new SeekableInMemoryByteChannel(initialData);
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);
        
        int bytesWritten = channel.write(emptyBuffer);
        
        assertEquals("Should write 0 bytes from empty buffer", 0, bytesWritten);
        assertEquals("Position should not change", 0L, channel.position());
        assertEquals("Size should not change", 5L, channel.size());
    }

    @Test
    public void shouldWriteLargeAmountOfData() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        ByteBuffer largeBuffer = ByteBuffer.allocateDirect(1073741823); // Large buffer
        
        int bytesWritten = channel.write(largeBuffer);
        
        assertEquals("Should write all data from large buffer", 1073741823, bytesWritten);
        assertEquals("Position should advance by bytes written", 1073741823L, channel.position());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenWritingWithNullBuffer() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.write(null);
    }

    @Test(expected = ClosedChannelException.class)
    public void shouldThrowExceptionWhenWritingToClosedChannel() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        channel.write(buffer);
    }

    // ========== Positioning Tests ==========

    @Test
    public void shouldSetPositionToValidValue() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        
        SeekableByteChannel result = channel.position(0L);
        
        assertEquals("Should return same channel instance", channel, result);
        assertEquals("Position should be set correctly", 0L, channel.position());
    }

    @Test
    public void shouldReadFromPositionBeyondChannelSize() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        channel.position(Integer.MAX_VALUE);
        int bytesRead = channel.read(buffer);
        
        assertEquals("Should return -1 when reading beyond size", -1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenSettingNegativePosition() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.position(-429L);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenSettingPositionBeyondMaxValue() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.position(2147483669L); // Beyond Integer.MAX_VALUE
    }

    @Test(expected = ClosedChannelException.class)
    public void shouldThrowExceptionWhenSettingPositionOnClosedChannel() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.close();
        channel.position(1073741828L);
    }

    // ========== Truncation Tests ==========

    @Test
    public void shouldTruncateChannelToSmallerSize() throws Exception {
        channel = new SeekableInMemoryByteChannel(1);
        
        SeekableByteChannel result = channel.truncate(0);
        
        assertEquals("Should return same channel instance", channel, result);
        assertEquals("Channel should be truncated to specified size", 0L, channel.size());
        assertEquals("Position should be adjusted if beyond new size", 0L, channel.position());
    }

    @Test
    public void shouldNotExpandChannelWhenTruncatingToLargerSize() throws Exception {
        byte[] initialData = new byte[3];
        channel = new SeekableInMemoryByteChannel(initialData);
        
        SeekableByteChannel result = channel.truncate(Integer.MAX_VALUE);
        
        assertEquals("Channel size should remain unchanged", 3L, result.size());
        assertEquals("Position should remain unchanged", 0L, result.position());
    }

    @Test
    public void shouldTruncateToExactSize() throws Exception {
        channel = new SeekableInMemoryByteChannel(122);
        
        SeekableByteChannel result = channel.truncate(122);
        
        assertEquals("Position should remain at 0", 0L, result.position());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTruncatingToNegativeSize() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.truncate(-469L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTruncatingBeyondMaxSize() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.truncate(2147483648L); // Beyond Integer.MAX_VALUE
    }

    @Test
    public void shouldAllowTruncationOnClosedChannel() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        channel.close();
        
        // Should not throw exception - violates contract but is documented behavior
        channel.truncate(6051L);
        
        assertFalse("Channel should remain closed", channel.isOpen());
    }

    // ========== Channel State Tests ==========

    @Test
    public void shouldReportOpenStateCorrectly() throws Exception {
        channel = new SeekableInMemoryByteChannel();
        
        assertTrue("New channel should be open", channel.isOpen());
        
        channel.close();
        assertFalse("Closed channel should report as closed", channel.isOpen());
    }

    @Test
    public void shouldReturnCorrectArrayRepresentation() throws Exception {
        byte[] expectedData = {0, 0, 0, 0, 0, 0, 0, 0};
        channel = new SeekableInMemoryByteChannel(expectedData);
        
        byte[] actualData = channel.array();
        
        assertArrayEquals("Array should match internal data", expectedData, actualData);
    }

    // ========== Complex Scenarios ==========

    @Test
    public void shouldHandleReadWriteCycleCorrectly() throws Exception {
        channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        
        // Read to advance position
        channel.read(buffer);
        assertEquals("Position should advance after read", 1L, channel.position());
        
        // Write should return 0 as buffer has no remaining data
        int bytesWritten = channel.write(buffer);
        assertEquals("Should write 0 bytes from exhausted buffer", 0, bytesWritten);
        assertEquals("Position should remain unchanged", 1L, channel.position());
    }

    @Test
    public void shouldHandleWriteAfterReadCorrectly() throws Exception {
        channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        
        // Read data and clear buffer for writing
        channel.read(buffer);
        buffer.clear();
        
        int bytesWritten = channel.write(buffer);
        
        assertEquals("Should write 1 byte", 1, bytesWritten);
        assertEquals("Buffer should be fully consumed", 0, buffer.remaining());
    }
}