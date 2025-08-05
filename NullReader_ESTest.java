package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.EOFException;
import java.io.IOException;
import org.apache.commons.io.input.NullReader;

/**
 * Test suite for NullReader class.
 * NullReader is a lightweight Reader implementation that emulates reading from a stream
 * of a specified size without actually processing real data.
 */
public class NullReaderTest {

    // Test Constants
    private static final long SMALL_SIZE = 7L;
    private static final long MEDIUM_SIZE = 959L;
    private static final long LARGE_SIZE = 3453L;
    private static final int BUFFER_SIZE = 4;

    // ===========================================
    // Constructor and Basic Properties Tests
    // ===========================================

    @Test
    public void testDefaultConstructor() throws IOException {
        NullReader reader = new NullReader();
        
        assertEquals("Default size should be 0", 0L, reader.getSize());
        assertEquals("Initial position should be 0", 0L, reader.getPosition());
        assertTrue("Should support mark by default", reader.markSupported());
    }

    @Test
    public void testConstructorWithSize() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        
        assertEquals("Size should match constructor parameter", MEDIUM_SIZE, reader.getSize());
        assertEquals("Initial position should be 0", 0L, reader.getPosition());
        assertTrue("Should support mark by default", reader.markSupported());
    }

    @Test
    public void testConstructorWithAllParameters() throws IOException {
        boolean markSupported = false;
        boolean throwEofException = true;
        
        NullReader reader = new NullReader(MEDIUM_SIZE, markSupported, throwEofException);
        
        assertEquals("Size should match constructor parameter", MEDIUM_SIZE, reader.getSize());
        assertEquals("Mark support should match constructor parameter", markSupported, reader.markSupported());
    }

    @Test
    public void testSingletonInstance() {
        NullReader instance = NullReader.INSTANCE;
        assertNotNull("Singleton instance should not be null", instance);
        assertEquals("Singleton should have size 0", 0L, instance.getSize());
    }

    // ===========================================
    // Single Character Read Tests
    // ===========================================

    @Test
    public void testReadSingleCharacter() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        
        int character = reader.read();
        
        assertEquals("Should return 0 (space character)", 0, character);
        assertEquals("Position should advance by 1", 1L, reader.getPosition());
    }

    @Test
    public void testReadSingleCharacterWithNegativeSize() throws IOException {
        NullReader reader = new NullReader(-476L);
        
        int character = reader.read();
        
        assertEquals("Should return 0 even with negative size", 0, character);
        assertEquals("Position should advance by 1", 1L, reader.getPosition());
    }

    @Test
    public void testReadPastEndWithEofException() throws IOException {
        NullReader reader = new NullReader(SMALL_SIZE, true, true); // throwEofException = true
        
        // Read to the end
        reader.skip(SMALL_SIZE);
        
        try {
            reader.read();
            fail("Should throw EOFException when reading past end");
        } catch (EOFException e) {
            // Expected behavior
        }
    }

    @Test
    public void testReadPastEndOfZeroSizeReaderWithInstance() throws IOException {
        try {
            NullReader.INSTANCE.read();
            fail("Should throw IOException when reading from zero-size reader");
        } catch (IOException e) {
            assertTrue("Should mention 'Read after end of file'", 
                      e.getMessage().contains("Read after end of file"));
        }
    }

    // ===========================================
    // Array Read Tests
    // ===========================================

    @Test
    public void testReadIntoCharArray() throws IOException {
        NullReader reader = new NullReader(LARGE_SIZE);
        char[] buffer = new char[BUFFER_SIZE];
        
        int bytesRead = reader.read(buffer);
        
        assertEquals("Should read entire buffer", BUFFER_SIZE, bytesRead);
        assertEquals("Position should advance by buffer size", BUFFER_SIZE, reader.getPosition());
    }

    @Test
    public void testReadIntoEmptyArray() throws IOException {
        NullReader reader = new NullReader(LARGE_SIZE);
        char[] emptyBuffer = new char[0];
        
        int bytesRead = reader.read(emptyBuffer);
        
        assertEquals("Should read 0 bytes from empty array", 0, bytesRead);
        assertEquals("Position should not change", 0L, reader.getPosition());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        char[] buffer = new char[BUFFER_SIZE];
        int offset = 0;
        int length = 2;
        
        int bytesRead = reader.read(buffer, offset, length);
        
        assertEquals("Should read requested length", length, bytesRead);
        assertEquals("Position should advance by length", length, reader.getPosition());
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        char[] buffer = new char[BUFFER_SIZE];
        
        int bytesRead = reader.read(buffer, 0, -10);
        
        assertEquals("Should handle negative length", -10, bytesRead);
        assertEquals("Position should reflect negative read", -10L, reader.getPosition());
    }

    @Test
    public void testReadNullArray() throws IOException {
        NullReader reader = new NullReader();
        
        try {
            reader.read(null);
            fail("Should throw NullPointerException for null array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testReadPastEndOfArrayWithEofException() throws IOException {
        NullReader reader = new NullReader(1L, false, true); // size=1, throwEofException=true
        char[] buffer = new char[1];
        
        // First read should succeed
        reader.read(buffer);
        
        // Second read should throw EOFException
        try {
            reader.read(buffer, 0, 1);
            fail("Should throw EOFException when reading past end");
        } catch (EOFException e) {
            // Expected behavior
        }
    }

    // ===========================================
    // Skip Operation Tests
    // ===========================================

    @Test
    public void testSkipCharacters() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        long skipAmount = 100L;
        
        long skipped = reader.skip(skipAmount);
        
        assertEquals("Should skip requested amount", skipAmount, skipped);
        assertEquals("Position should advance by skip amount", skipAmount, reader.getPosition());
    }

    @Test
    public void testSkipZeroCharacters() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE);
        
        long skipped = reader.skip(0L);
        
        assertEquals("Should skip 0 characters", 0L, skipped);
        assertEquals("Position should not change", 0L, reader.getPosition());
    }

    @Test
    public void testSkipMoreThanSize() throws IOException {
        NullReader reader = new NullReader(1L);
        
        long skipped = reader.skip(1000L);
        
        assertEquals("Should only skip to end of stream", 1L, skipped);
        assertEquals("Position should be at end", 1L, reader.getPosition());
    }

    @Test
    public void testSkipWithEofException() throws IOException {
        NullReader reader = new NullReader(0, true, true); // size=0, throwEofException=true
        
        try {
            reader.skip(10L);
            fail("Should throw EOFException when skipping past end");
        } catch (EOFException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSkipNegativeAmount() throws IOException {
        NullReader reader = new NullReader();
        reader.read(); // Move past end
        
        try {
            reader.skip(-1L);
            fail("Should throw IOException when skipping negative amount past end");
        } catch (IOException e) {
            assertTrue("Should mention 'Skip after end of file'", 
                      e.getMessage().contains("Skip after end of file"));
        }
    }

    @Test
    public void testSkipWithNegativeSize() throws IOException {
        NullReader reader = new NullReader(-3294L, false, false);
        
        long skipped = reader.skip(-100L);
        
        assertEquals("Should return -1 for negative skip on negative size", -1L, skipped);
    }

    // ===========================================
    // Mark and Reset Tests
    // ===========================================

    @Test
    public void testMarkAndReset() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE, true, true); // markSupported=true
        char[] buffer = new char[BUFFER_SIZE];
        
        // Read some data and mark position
        reader.read(buffer);
        reader.mark(100);
        long markedPosition = reader.getPosition();
        
        // Read more data
        reader.read(buffer);
        
        // Reset should return to marked position
        reader.reset();
        assertEquals("Should reset to marked position", markedPosition, reader.getPosition());
    }

    @Test
    public void testResetWithoutMark() throws IOException {
        NullReader reader = new NullReader(MEDIUM_SIZE, true, true);
        
        try {
            reader.reset();
            fail("Should throw IOException when reset without mark");
        } catch (IOException e) {
            assertTrue("Should mention 'No position has been marked'", 
                      e.getMessage().contains("No position has been marked"));
        }
    }

    @Test
    public void testMarkNotSupported() throws IOException {
        NullReader reader = new NullReader(-329L, false, true); // markSupported=false
        
        try {
            reader.mark(100);
            fail("Should throw UnsupportedOperationException when mark not supported");
        } catch (UnsupportedOperationException e) {
            assertTrue("Should mention 'mark/reset not supported'", 
                      e.getMessage().contains("mark/reset not supported"));
        }
    }

    @Test
    public void testResetNotSupported() throws IOException {
        NullReader reader = new NullReader(-1388L, false, false); // markSupported=false
        
        try {
            reader.reset();
            fail("Should throw UnsupportedOperationException when reset not supported");
        } catch (UnsupportedOperationException e) {
            assertTrue("Should mention 'mark/reset not supported'", 
                      e.getMessage().contains("mark/reset not supported"));
        }
    }

    @Test
    public void testMarkSupported() {
        NullReader supportedReader = new NullReader(100L, true, false);
        NullReader unsupportedReader = new NullReader(100L, false, false);
        
        assertTrue("Should support mark when configured", supportedReader.markSupported());
        assertFalse("Should not support mark when configured", unsupportedReader.markSupported());
    }

    // ===========================================
    // Edge Cases and Error Conditions
    // ===========================================

    @Test
    public void testNegativeSize() throws IOException {
        long negativeSize = -1935L;
        NullReader reader = new NullReader(negativeSize, true, true);
        
        assertEquals("Should handle negative size", negativeSize, reader.getSize());
        
        char[] buffer = new char[0];
        int bytesRead = reader.read(buffer);
        assertEquals("Should read negative amount with negative size", (int)negativeSize, bytesRead);
    }

    @Test
    public void testProcessCharMethod() throws IOException {
        NullReader reader = new NullReader(-476L);
        
        int processedChar = reader.processChar();
        
        assertEquals("processChar should return 0", 0, processedChar);
    }

    @Test
    public void testCloseOperation() throws IOException {
        NullReader reader = new NullReader(-995L);
        
        reader.close();
        
        assertEquals("Position should remain 0 after close", 0L, reader.getPosition());
        assertEquals("Size should remain unchanged after close", -995L, reader.getSize());
    }

    @Test
    public void testComplexReadScenario() throws IOException {
        NullReader reader = new NullReader(-1935L, true, true);
        char[] buffer = new char[0];
        
        // First read with empty buffer
        reader.read(buffer, 1095, 1095);
        
        // Second read should trigger EOF
        try {
            reader.read(buffer);
            fail("Should throw EOFException on second read");
        } catch (EOFException e) {
            // Expected behavior
        }
    }

    // ===========================================
    // Integration Tests
    // ===========================================

    @Test
    public void testReadWriteCycle() throws IOException {
        NullReader reader = new NullReader(10L, true, false);
        
        // Read single character
        assertEquals("First read should return 0", 0, reader.read());
        assertEquals("Position should be 1", 1L, reader.getPosition());
        
        // Read array
        char[] buffer = new char[5];
        int bytesRead = reader.read(buffer);
        assertEquals("Should read 5 characters", 5, bytesRead);
        assertEquals("Position should be 6", 6L, reader.getPosition());
        
        // Skip remaining
        long skipped = reader.skip(4L);
        assertEquals("Should skip 4 characters", 4L, skipped);
        assertEquals("Position should be 10", 10L, reader.getPosition());
    }
}