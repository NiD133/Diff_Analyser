package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Test suite for GroupedRandomAccessSource class.
 * Tests the functionality of combining multiple RandomAccessSource objects
 * into a single contiguous data source.
 */
public class GroupedRandomAccessSourceTest {

    // Test data constants
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final byte[] SINGLE_BYTE_ARRAY = new byte[1];
    private static final byte[] SMALL_ARRAY = new byte[5];
    private static final byte[] MEDIUM_ARRAY = new byte[11];
    
    // Helper method to create a simple grouped source with two sources
    private GroupedRandomAccessSource createSimpleGroupedSource(int firstSize, int secondSize) {
        RandomAccessSource[] sources = new RandomAccessSource[2];
        sources[0] = new ArrayRandomAccessSource(new byte[firstSize]);
        sources[1] = new GetBufferedRandomAccessSource(sources[0]);
        return new GroupedRandomAccessSource(sources);
    }

    @Test
    public void testReadDataFromMultipleSources() throws IOException {
        // Given: A grouped source with two 11-byte sources
        byte[] sourceData = new byte[11];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(sourceData),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(sourceData))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Reading 5 bytes starting from position 10
        byte[] buffer = new byte[11];
        int bytesRead = groupedSource.get(10L, buffer, 5, 5);
        
        // Then: Should successfully read 5 bytes
        assertEquals("Should read 5 bytes from grouped source", 5, bytesRead);
    }

    @Test
    public void testReadWithInvalidParameters() throws IOException {
        // Given: A grouped source with small arrays
        GroupedRandomAccessSource groupedSource = createSimpleGroupedSource(1, 1);
        
        // When: Attempting to read with negative offset and length
        byte[] buffer = new byte[1];
        int result = groupedSource.get(0L, buffer, -10, -10);
        
        // Then: Should return -1 indicating failure
        assertEquals("Invalid parameters should return -1", -1, result);
        assertEquals("Total length should be 2", 2L, groupedSource.length());
    }

    @Test
    public void testSourceManagement() throws IOException {
        // Given: A grouped source with empty arrays
        GroupedRandomAccessSource groupedSource = createSimpleGroupedSource(0, 0);
        ArrayRandomAccessSource testSource = new ArrayRandomAccessSource(EMPTY_ARRAY);
        
        // When: Notifying about source usage
        groupedSource.sourceInUse(testSource);
        
        // Then: Should handle source management without errors
        assertEquals("Empty grouped source should have length 0", 0L, groupedSource.length());
    }

    @Test
    public void testLengthCalculation() throws IOException {
        // Test case 1: Empty sources
        GroupedRandomAccessSource emptySource = createSimpleGroupedSource(0, 0);
        assertEquals("Empty sources should have total length 0", 0L, emptySource.length());
        
        // Test case 2: Multiple identical sources
        byte[] data = new byte[5];
        ArrayRandomAccessSource baseSource = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(baseSource);
        RandomAccessSource[] sources = {bufferedSource, bufferedSource, bufferedSource};
        GroupedRandomAccessSource multiSource = new GroupedRandomAccessSource(sources);
        
        assertEquals("Three 5-byte sources should have total length 15", 15L, multiSource.length());
    }

    @Test
    public void testComplexSourceCombination() throws IOException {
        // Given: A complex combination of different source types
        byte[] data = new byte[11];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(bufferedSource, -2024L, -2024L);
        
        RandomAccessSource[] sources = {
            arraySource, bufferedSource, windowSource, bufferedSource, 
            bufferedSource, bufferedSource, arraySource, bufferedSource, bufferedSource
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Getting total length
        long totalLength = groupedSource.length();
        
        // Then: Should calculate correct total (including negative window)
        assertEquals("Complex source combination should calculate correct length", -1936L, totalLength);
    }

    @Test
    public void testSourceIndexCalculation() throws IOException {
        // Given: A grouped source with window sources
        byte[] data = new byte[1];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(data);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, 7L, 7L);
        IndependentRandomAccessSource independentSource = new IndependentRandomAccessSource(windowSource);
        
        RandomAccessSource[] sources = {independentSource, windowSource};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Finding source index for position 7
        int sourceIndex = groupedSource.getStartingSourceIndex(7L);
        
        // Then: Should return correct source index
        assertEquals("Position 7 should be in source index 1", 1, sourceIndex);
        assertEquals("Total length should be 14", 14L, groupedSource.length());
    }

    @Test
    public void testReadSpecificByte() throws IOException {
        // Given: A grouped source with known data
        byte[] data = new byte[4];
        data[2] = (byte) 3; // Set specific value at index 2
        
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        RandomAccessSource[] sources = {arraySource, bufferedSource, arraySource, arraySource, bufferedSource};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Reading byte at position 2
        int byteValue = groupedSource.get(2L);
        
        // Then: Should return the correct byte value
        assertEquals("Should read correct byte value", 3, byteValue);
        assertEquals("Total length should be 20", 20L, groupedSource.length());
    }

    @Test(expected = NullPointerException.class)
    public void testReadAfterClose() throws IOException {
        // Given: A closed grouped source
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[3])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        groupedSource.close();
        
        // When: Attempting to read after close
        // Then: Should throw NullPointerException
        groupedSource.get(1L, new byte[3], 1, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterCloseWithWindowSource() throws IOException {
        // Given: A closed grouped source with window source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[1]);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, 7L, 7L);
        IndependentRandomAccessSource independentSource = new IndependentRandomAccessSource(windowSource);
        
        RandomAccessSource[] sources = {independentSource, windowSource};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        groupedSource.close();
        
        // When: Attempting to read after close
        // Then: Should throw IllegalStateException
        groupedSource.get(1L, new byte[1], 1, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferIndex() throws IOException {
        // Given: A grouped source
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[7])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Attempting to read with invalid buffer index
        // Then: Should throw ArrayIndexOutOfBoundsException
        groupedSource.get(1L, new byte[7], 5239, 1);
    }

    @Test(expected = IOException.class)
    public void testReadFromUnmappedChannel() throws IOException {
        // Given: A grouped source with unmapped channel
        MappedChannelRandomAccessSource mappedSource = new MappedChannelRandomAccessSource(null, 2L, 2L);
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[7]);
        IndependentRandomAccessSource independentSource = new IndependentRandomAccessSource(mappedSource);
        
        RandomAccessSource[] sources = {mappedSource, mappedSource, arraySource, independentSource};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Attempting to read from unmapped channel
        // Then: Should throw IOException
        groupedSource.get(2L, new byte[7], 109, 43);
    }

    @Test
    public void testReadBeyondBounds() throws IOException {
        // Given: A grouped source with limited data
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[3])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Reading beyond available data
        int result = groupedSource.get(12L);
        
        // Then: Should return -1
        assertEquals("Reading beyond bounds should return -1", -1, result);
        assertEquals("Source length should be 3", 3L, groupedSource.length());
    }

    @Test
    public void testPartialRead() throws IOException {
        // Given: A grouped source with 5 bytes
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[5])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Attempting to read 3 bytes starting from position 3 (only 2 bytes available)
        byte[] buffer = new byte[5];
        int bytesRead = groupedSource.get(3L, buffer, 3, 3);
        
        // Then: Should read only available bytes
        assertEquals("Should read only 2 available bytes", 2, bytesRead);
    }

    @Test
    public void testValidRead() throws IOException {
        // Given: A grouped source with data
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[2])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        
        // When: Reading first byte
        int byteValue = groupedSource.get(0L);
        
        // Then: Should return valid byte (0 for initialized array)
        assertEquals("Should read first byte successfully", 0, byteValue);
        assertEquals("Source length should be 2", 2L, groupedSource.length());
    }

    @Test
    public void testSourceReleaseNotification() throws IOException {
        // Given: A grouped source
        GroupedRandomAccessSource groupedSource = createSimpleGroupedSource(0, 0);
        ArrayRandomAccessSource testSource = new ArrayRandomAccessSource(EMPTY_ARRAY);
        
        // When: Notifying about source release
        groupedSource.sourceReleased(testSource);
        
        // Then: Should handle notification without errors
        assertEquals("Should maintain correct length after source release", 0L, groupedSource.length());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArray() {
        // When: Creating grouped source with null array
        // Then: Should throw NullPointerException
        new GroupedRandomAccessSource(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConstructorWithEmptyArray() {
        // When: Creating grouped source with empty array
        // Then: Should throw ArrayIndexOutOfBoundsException
        new GroupedRandomAccessSource(new RandomAccessSource[0]);
    }
}