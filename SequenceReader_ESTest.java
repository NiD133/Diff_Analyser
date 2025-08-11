package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.input.SequenceReader;

/**
 * Test suite for SequenceReader class.
 * SequenceReader combines multiple Reader instances into a single sequential reader.
 */
public class SequenceReader_ESTest {

    // ========== Skip Operation Tests ==========
    
    @Test(timeout = 4000)
    public void testSkipCharactersAcrossMultipleReaders() throws Throwable {
        // Given: Two string readers with known content lengths
        StringReader firstReader = new StringReader("!DROK>c"); // 7 characters
        StringReader secondReader = new StringReader("org.apache.commons.io.filefilter.CanExecuteFileFilter"); // 53 characters
        
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(firstReader);
        readers.add(secondReader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Skip more characters than available (204 > 60 total)
        long skippedCount = sequenceReader.skip(204L);
        
        // Then: Should skip all available characters (7 + 53 = 60)
        assertEquals("Should skip all 60 available characters", 60L, skippedCount);
    }

    // ========== Read Operation Tests ==========
    
    @Test(timeout = 4000)
    public void testReadFromEmptySequence() throws Throwable {
        // Given: Empty sequence reader
        ArrayDeque<StringReader> emptyReaders = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(emptyReaders);
        
        // When: Try to read into buffer
        char[] buffer = new char[7];
        int bytesRead = sequenceReader.read(buffer, 1, 0);
        
        // Then: Should return -1 (EOF)
        assertEquals("Reading from empty sequence should return EOF", -1, bytesRead);
    }
    
    @Test(timeout = 4000)
    public void testReadSingleCharacterFromSequence() throws Throwable {
        // Given: Sequence with one reader containing "directoryFilter"
        StringReader reader = new StringReader("directoryFilter");
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(reader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Read single character
        int character = sequenceReader.read();
        
        // Then: Should return first character 'd' (ASCII 100)
        assertEquals("Should read first character 'd'", 100, character);
    }
    
    @Test(timeout = 4000)
    public void testReadIntoBufferFromSequence() throws Throwable {
        // Given: Sequence with reader containing "directoryFilter"
        StringReader reader = new StringReader("directoryFilter");
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(reader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Read one character into buffer at position 1
        char[] buffer = new char[3];
        int bytesRead = sequenceReader.read(buffer, 1, 1);
        
        // Then: Should read 'd' into position 1
        assertArrayEquals("Buffer should contain 'd' at position 1", 
                         new char[] {'\u0000', 'd', '\u0000'}, buffer);
        assertEquals("Should read exactly 1 character", 1, bytesRead);
    }
    
    @Test(timeout = 4000)
    public void testReadFromEmptyStringReader() throws Throwable {
        // Given: Sequence with empty string reader
        StringReader emptyReader = new StringReader("");
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(emptyReader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Try to read single character
        int character = sequenceReader.read();
        
        // Then: Should return EOF
        assertEquals("Reading from empty string should return EOF", -1, character);
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testReadWithNullBuffer() throws Throwable {
        // Given: Empty sequence reader
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When/Then: Reading with null buffer should throw NullPointerException
        try {
            sequenceReader.read(null, -1, -1);
            fail("Expected NullPointerException for null buffer");
        } catch (NullPointerException e) {
            assertEquals("Exception should mention 'cbuf'", "cbuf", e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testReadWithInvalidBufferBounds() throws Throwable {
        // Given: Empty sequence reader
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When/Then: Invalid offset should throw IndexOutOfBoundsException
        char[] buffer = new char[7];
        try {
            sequenceReader.read(buffer, -1, -1);
            fail("Expected IndexOutOfBoundsException for invalid bounds");
        } catch (IndexOutOfBoundsException e) {
            assertTrue("Exception should mention array bounds", 
                      e.getMessage().contains("Array Size=7, offset=-1, length=-1"));
        }
    }
    
    @Test(timeout = 4000)
    public void testReadWithOffsetExceedingBufferSize() throws Throwable {
        // Given: Empty sequence reader and small buffer
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When/Then: Offset exceeding buffer size should throw IndexOutOfBoundsException
        char[] smallBuffer = new char[0];
        try {
            sequenceReader.read(smallBuffer, 136209934, 136209934);
            fail("Expected IndexOutOfBoundsException for offset exceeding buffer size");
        } catch (IndexOutOfBoundsException e) {
            assertTrue("Exception should mention array bounds", 
                      e.getMessage().contains("Array Size=0, offset=136209934, length=136209934"));
        }
    }
    
    @Test(timeout = 4000)
    public void testReadFromClosedReader() throws Throwable {
        // Given: Array with same StringReader used twice (will be closed after first use)
        StringReader sharedReader = new StringReader("");
        Reader[] readers = new Reader[2];
        readers[0] = sharedReader;
        readers[1] = sharedReader; // Same instance - will be closed
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When/Then: Reading from closed reader should throw IOException
        char[] buffer = new char[3];
        try {
            sequenceReader.read(buffer, 1, 1);
            fail("Expected IOException when reading from closed reader");
        } catch (IOException e) {
            assertEquals("Should indicate stream is closed", "Stream closed", e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testReadFromUnconnectedPipedReader() throws Throwable {
        // Given: Unconnected PipedReader
        PipedReader pipedReader = new PipedReader();
        Reader[] readers = new Reader[1];
        readers[0] = pipedReader;
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When/Then: Reading from unconnected pipe should throw IOException
        try {
            sequenceReader.read();
            fail("Expected IOException for unconnected pipe");
        } catch (IOException e) {
            assertEquals("Should indicate pipe not connected", "Pipe not connected", e.getMessage());
        }
    }

    // ========== Constructor Tests ==========
    
    @Test(timeout = 4000)
    public void testConstructorWithNullReaderArray() throws Throwable {
        // When/Then: Null reader array should throw NullPointerException
        try {
            new SequenceReader((Reader[]) null);
            fail("Expected NullPointerException for null reader array");
        } catch (NullPointerException e) {
            // Expected - constructor should validate input
        }
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithNullIterable() throws Throwable {
        // When/Then: Null iterable should throw NullPointerException
        try {
            new SequenceReader((Iterable<? extends Reader>) null);
            fail("Expected NullPointerException for null iterable");
        } catch (NullPointerException e) {
            assertEquals("Exception should mention 'readers'", "readers", e.getMessage());
        }
    }

    // ========== Concurrent Modification Tests ==========
    
    @Test(timeout = 4000)
    public void testConcurrentModificationDuringRead() throws Throwable {
        // Given: ArrayDeque with readers, modified during iteration
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        StringReader reader = new StringReader("");
        readers.add(reader);
        readers.add(reader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Modify collection after creating SequenceReader but before reading
        readers.add(reader);
        
        // Then: Should throw ConcurrentModificationException
        try {
            sequenceReader.read();
            fail("Expected ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected - collection was modified during iteration
        }
    }
    
    @Test(timeout = 4000)
    public void testConcurrentModificationDuringClose() throws Throwable {
        // Given: Vector with readers, modified during close operation
        Vector<StringReader> readers = new Vector<>();
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Modify collection after creating SequenceReader
        StringReader reader = new StringReader("_cA{/)2>I@4NJ(");
        readers.add(reader);
        
        // Then: Close operation should throw ConcurrentModificationException
        try {
            sequenceReader.close();
            fail("Expected ConcurrentModificationException during close");
        } catch (ConcurrentModificationException e) {
            // Expected - collection was modified during iteration
        }
    }
    
    @Test(timeout = 4000)
    public void testConcurrentModificationWithSubList() throws Throwable {
        // Given: ArrayList sublist that becomes invalid when parent is modified
        ArrayList<StringReader> parentList = new ArrayList<>();
        List<StringReader> subList = parentList.subList(0, 0); // Empty sublist
        
        // When: Modify parent list, making sublist invalid
        StringReader reader = new StringReader("Array Size=");
        parentList.add(reader);
        
        // Then: Creating SequenceReader with invalid sublist should throw ConcurrentModificationException
        try {
            new SequenceReader(subList);
            fail("Expected ConcurrentModificationException with modified sublist");
        } catch (ConcurrentModificationException e) {
            // Expected - sublist became invalid when parent was modified
        }
    }

    // ========== Close Operation Tests ==========
    
    @Test(timeout = 4000)
    public void testCloseSequenceReader() throws Throwable {
        // Given: Sequence with readers
        StringReader reader = new StringReader("T37");
        ArrayDeque<StringReader> readers = new ArrayDeque<>();
        readers.add(reader);
        readers.add(reader);
        
        SequenceReader sequenceReader = new SequenceReader(readers);
        
        // When: Close the sequence reader
        sequenceReader.close();
        
        // Then: Should complete without exception
        // (Close operation should close all underlying readers)
    }
}