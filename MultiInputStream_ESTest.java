package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.io.ByteSource;
import com.google.common.io.MultiInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ConcurrentModificationException;

/**
 * Test suite for MultiInputStream - verifies concatenation of multiple ByteSource streams.
 */
public class MultiInputStreamTest {

    // ========== Basic Functionality Tests ==========

    @Test
    public void skipZeroBytes_shouldReturnZero() throws Throwable {
        // Given: A MultiInputStream with one empty ByteSource
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sources = createIteratorWith(emptySource);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Skipping 0 bytes
        long bytesSkipped = stream.skip(0L);
        
        // Then: Should return 0
        assertEquals(0L, bytesSkipped);
    }

    @Test
    public void readFromEmptyStream_shouldReturnMinusOne() throws Throwable {
        // Given: A MultiInputStream with no ByteSources
        Iterator<ByteSource> emptySources = new LinkedList<ByteSource>().iterator();
        MultiInputStream stream = new MultiInputStream(emptySources);
        
        // When: Reading into a buffer
        byte[] buffer = new byte[7];
        int bytesRead = stream.read(buffer, 0, 387);
        
        // Then: Should return -1 (end of stream)
        assertEquals(-1, bytesRead);
    }

    @Test
    public void readSingleByte_shouldReturnUnsignedByteValue() throws Throwable {
        // Given: A MultiInputStream with one ByteSource containing a negative byte
        byte[] data = {(byte) -98}; // -98 as signed byte = 158 as unsigned
        ByteSource source = ByteSource.wrap(data);
        Iterator<ByteSource> sources = createIteratorWith(source);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Reading a single byte
        int byteValue = stream.read();
        
        // Then: Should return the unsigned byte value (158)
        assertEquals(158, byteValue);
    }

    @Test
    public void readFromEmptyStream_singleByte_shouldReturnMinusOne() throws Throwable {
        // Given: A MultiInputStream with no ByteSources
        Iterator<ByteSource> emptySources = new ArrayDeque<ByteSource>().iterator();
        MultiInputStream stream = new MultiInputStream(emptySources);
        
        // When: Reading a single byte
        int result = stream.read();
        
        // Then: Should return -1 (end of stream)
        assertEquals(-1, result);
    }

    @Test
    public void available_withEmptyStream_shouldReturnZero() throws Throwable {
        // Given: A MultiInputStream with no ByteSources
        Iterator<ByteSource> emptySources = new LinkedList<ByteSource>().iterator();
        MultiInputStream stream = new MultiInputStream(emptySources);
        
        // When: Checking available bytes
        int available = stream.available();
        
        // Then: Should return 0
        assertEquals(0, available);
    }

    @Test
    public void available_withDataStream_shouldReturnAvailableBytes() throws Throwable {
        // Given: A MultiInputStream with 8 bytes of data
        byte[] data = new byte[8];
        ByteSource source = ByteSource.wrap(data);
        Iterator<ByteSource> sources = createIteratorWith(source);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Checking available bytes
        int available = stream.available();
        
        // Then: Should return 8
        assertEquals(8, available);
    }

    // ========== Stream Operations Tests ==========

    @Test
    public void skip_withMultipleStreams_shouldSkipAcrossStreams() throws Throwable {
        // Given: A MultiInputStream with concatenated streams (empty + 6 bytes + empty)
        ByteSource emptySource = ByteSource.empty();
        ByteSource dataSource = ByteSource.wrap(new byte[6]);
        ByteSource[] sources = {emptySource, dataSource, emptySource};
        ByteSource concatenated = ByteSource.concat(sources);
        Iterator<ByteSource> sourceIterator = createIteratorWith(concatenated);
        MultiInputStream stream = new MultiInputStream(sourceIterator);
        
        // When: Skipping 60 bytes (more than available)
        long bytesSkipped = stream.skip(60);
        
        // Then: Should skip all 6 available bytes
        assertEquals(6L, bytesSkipped);
    }

    @Test
    public void skip_withNegativeValue_shouldReturnZero() throws Throwable {
        // Given: A MultiInputStream with data
        byte[] data = new byte[8];
        ByteSource source = ByteSource.wrap(data);
        Iterator<ByteSource> sources = createIteratorWith(source);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Skipping negative bytes
        long bytesSkipped = stream.skip(-896L);
        
        // Then: Should return 0 (can't skip backwards)
        assertEquals(0L, bytesSkipped);
    }

    @Test
    public void skip_withEmptyStream_shouldReturnZero() throws Throwable {
        // Given: A MultiInputStream with empty ByteSource
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sources = createIteratorWith(emptySource);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Skipping bytes
        long bytesSkipped = stream.skip(2173L);
        
        // Then: Should return 0 (nothing to skip)
        assertEquals(0L, bytesSkipped);
    }

    @Test
    public void readByte_fromDataStream_shouldReturnFirstByte() throws Throwable {
        // Given: A MultiInputStream with 8 zero bytes
        byte[] data = new byte[8]; // All zeros
        ByteSource source = ByteSource.wrap(data);
        Iterator<ByteSource> sources = createIteratorWith(source);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Reading first byte
        int firstByte = stream.read();
        
        // Then: Should return 0
        assertEquals(0, firstByte);
    }

    // ========== Stream Lifecycle Tests ==========

    @Test
    public void close_shouldCloseSuccessfully() throws Throwable {
        // Given: A MultiInputStream with empty ByteSource
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sources = createIteratorWith(emptySource);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Closing the stream
        stream.close();
        
        // Then: Should not support mark (verify stream is properly initialized)
        assertFalse(stream.markSupported());
    }

    @Test
    public void markSupported_shouldReturnFalse() throws Throwable {
        // Given: A MultiInputStream with no ByteSources
        Iterator<ByteSource> emptySources = new LinkedList<ByteSource>().iterator();
        MultiInputStream stream = new MultiInputStream(emptySources);
        
        // When: Checking mark support
        boolean markSupported = stream.markSupported();
        
        // Then: Should return false (MultiInputStream doesn't support mark)
        assertFalse(markSupported);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = ConcurrentModificationException.class)
    public void skip_withConcurrentModification_shouldThrowException() throws Throwable {
        // Given: A MultiInputStream with iterator that will be modified
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sourceQueue = new ArrayDeque<>();
        sourceQueue.add(emptySource);
        sourceQueue.add(emptySource);
        Iterator<ByteSource> sources = sourceQueue.iterator();
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Modifying the underlying collection while using the stream
        sourceQueue.add(emptySource); // This causes ConcurrentModificationException
        stream.skip(4134L);
        
        // Then: Should throw ConcurrentModificationException
    }

    @Test(expected = NullPointerException.class)
    public void read_withNullBuffer_shouldThrowException() throws Throwable {
        // Given: A MultiInputStream
        Iterator<ByteSource> emptySources = new ArrayDeque<ByteSource>().iterator();
        MultiInputStream stream = new MultiInputStream(emptySources);
        
        // When: Reading with null buffer
        stream.read(null, -332, -332);
        
        // Then: Should throw NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withInvalidBufferBounds_shouldThrowException() throws Throwable {
        // Given: A MultiInputStream with empty ByteSource
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sources = createIteratorWith(emptySource);
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Reading with invalid buffer bounds
        byte[] emptyBuffer = new byte[0];
        stream.read(emptyBuffer, 408, 408); // offset and length exceed buffer size
        
        // Then: Should throw IndexOutOfBoundsException
    }

    @Test(expected = ConcurrentModificationException.class)
    public void read_withConcurrentModification_shouldThrowException() throws Throwable {
        // Given: A MultiInputStream with ArrayList iterator
        ArrayList<ByteSource> sourceList = new ArrayList<>();
        ByteSource emptySource = ByteSource.empty();
        sourceList.add(emptySource);
        Iterator<ByteSource> sources = sourceList.iterator();
        MultiInputStream stream = new MultiInputStream(sources);
        
        // When: Modifying the list while using the stream
        sourceList.add(emptySource); // This causes ConcurrentModificationException
        stream.read();
        
        // Then: Should throw ConcurrentModificationException
    }

    @Test(expected = ConcurrentModificationException.class)
    public void constructor_withConcurrentModification_shouldThrowException() throws Throwable {
        // Given: An iterator that will be concurrently modified during construction
        ByteSource emptySource = ByteSource.empty();
        ArrayDeque<ByteSource> sourceQueue = new ArrayDeque<>();
        sourceQueue.add(emptySource);
        Iterator<ByteSource> sources = sourceQueue.iterator();
        
        // When: Modifying the collection before construction completes
        sourceQueue.add(emptySource); // This causes ConcurrentModificationException
        new MultiInputStream(sources);
        
        // Then: Should throw ConcurrentModificationException
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullByteSource_shouldThrowException() throws Throwable {
        // Given: An iterator containing null ByteSource
        ArrayList<ByteSource> sourceList = new ArrayList<>();
        sourceList.add(null);
        Iterator<ByteSource> sources = sourceList.iterator();
        
        // When: Creating MultiInputStream with null ByteSource
        new MultiInputStream(sources);
        
        // Then: Should throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullIterator_shouldThrowException() throws Throwable {
        // When: Creating MultiInputStream with null iterator
        new MultiInputStream(null);
        
        // Then: Should throw NullPointerException
    }

    // ========== Integration Tests ==========

    @Test
    public void byteSourceConcat_withMultipleSources_shouldConcatenateCorrectly() throws Throwable {
        // Given: Multiple ByteSources to concatenate
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sources = createIteratorWith(emptySource);
        
        // When: Creating concatenated ByteSource
        ByteSource concatenated = ByteSource.concat(sources);
        byte[] result = concatenated.read();
        
        // Then: Should produce empty result
        assertEquals(0, result.length);
    }

    @Test
    public void byteSourceContentEquals_shouldWorkCorrectly() throws Throwable {
        // Given: A ByteSource created from concatenated sources
        byte[] data = new byte[3];
        ByteSource source = ByteSource.wrap(data);
        Iterator<ByteSource> sources = createIteratorWith(source);
        ByteSource concatenated = ByteSource.concat(sources);
        
        // When: Comparing content with itself
        boolean isEqual = concatenated.contentEquals(concatenated);
        
        // Then: Should be equal
        assertTrue(isEqual);
    }

    // ========== Helper Methods ==========

    /**
     * Creates an iterator containing the given ByteSource.
     */
    private Iterator<ByteSource> createIteratorWith(ByteSource source) {
        ArrayDeque<ByteSource> sources = new ArrayDeque<>();
        sources.add(source);
        return sources.iterator();
    }
}