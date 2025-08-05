package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.io.input.BoundedReader;

/**
 * Test suite for BoundedReader functionality.
 * BoundedReader limits the number of characters that can be read from an underlying reader.
 */
public class BoundedReaderTest {

    private static final String SAMPLE_TEXT = "Hello World";
    private static final String LONG_TEXT = "org.apache.commons.io.input.BoundedReader";

    // ========== Basic Reading Tests ==========

    @Test
    public void shouldLimitCharactersReadIntoArray() throws IOException {
        // Given: A reader with content longer than the limit
        StringReader sourceReader = new StringReader(LONG_TEXT);
        BoundedReader boundedReader = new BoundedReader(sourceReader, 3);
        
        // When: Reading into a character array
        char[] buffer = new char[7];
        int charsRead = boundedReader.read(buffer);
        
        // Then: Only the limited number of characters should be read
        assertEquals("Should read exactly 3 characters", 3, charsRead);
        assertArrayEquals("Should read first 3 characters only", 
            new char[] {'o', 'r', 'g', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    @Test
    public void shouldReadSingleCharacterWithinLimit() throws IOException {
        // Given: A bounded reader with limit of 1
        StringReader sourceReader = new StringReader("v4]>?/Q;dj|.O1#4");
        BoundedReader boundedReader = new BoundedReader(sourceReader, 1);
        
        // When: Reading a single character
        int character = boundedReader.read();
        
        // Then: Should return the first character
        assertEquals("Should read first character 'v'", 118, character); // ASCII value of 'v'
    }

    @Test
    public void shouldReadPartialArrayWithinBounds() throws IOException {
        // Given: A bounded reader with sufficient limit
        StringReader sourceReader = new StringReader(".''L5DuTEy{jV3");
        BoundedReader boundedReader = new BoundedReader(sourceReader, 1729);
        
        // When: Reading 1 character at offset 1
        char[] buffer = new char[8];
        int charsRead = boundedReader.read(buffer, 1, 1);
        
        // Then: Should read 1 character at the specified position
        assertEquals("Should read 1 character", 1, charsRead);
        assertArrayEquals("Should place character at offset 1", 
            new char[] {'\u0000', '.', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    // ========== End of Stream Tests ==========

    @Test
    public void shouldReturnMinusOneWhenEmptyStreamReached() throws IOException {
        // Given: An empty string reader
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1805);
        
        // When: Reading from empty stream
        int result = boundedReader.read();
        
        // Then: Should return -1 (EOF)
        assertEquals("Should return -1 for empty stream", -1, result);
    }

    @Test
    public void shouldReturnMinusOneWhenLimitReached() throws IOException {
        // Given: A bounded reader with negative limit
        BoundedReader boundedReader = new BoundedReader(null, -1);
        
        // When: Attempting to read
        int result = boundedReader.read();
        
        // Then: Should return -1 (no characters allowed)
        assertEquals("Should return -1 when limit is negative", -1, result);
    }

    @Test
    public void shouldHandleZeroLengthReadRequest() throws IOException {
        // Given: An empty string reader
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1);
        
        // When: Reading 0 characters (even with invalid offset)
        char[] buffer = new char[3];
        int charsRead = boundedReader.read(buffer, -743, 0);
        
        // Then: Should return 0 (no characters requested)
        assertEquals("Should return 0 when length is 0", 0, charsRead);
    }

    @Test
    public void shouldReturnMinusOneForNegativeLengthRead() throws IOException {
        // Given: An empty string reader
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1805);
        
        // When: Reading with negative length
        int charsRead = boundedReader.read(null, 1805, -1);
        
        // Then: Should return -1
        assertEquals("Should return -1 for negative length", -1, charsRead);
    }

    // ========== Mark and Reset Tests ==========

    @Test
    public void shouldMaintainConsistentReadAfterMark() throws IOException {
        // Given: An empty string reader that's been read once
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1805);
        int firstRead = boundedReader.read();
        
        // When: Marking and reading again
        boundedReader.mark(1268);
        int secondRead = boundedReader.read();
        
        // Then: Both reads should return the same value (-1)
        assertEquals("Reads should be consistent", firstRead, secondRead);
    }

    @Test
    public void shouldHandleMarkAndReadSequence() throws IOException {
        // Given: An empty string reader
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1805);
        
        // When: Reading, marking, then reading again
        boundedReader.read();
        boundedReader.mark(0);
        int result = boundedReader.read();
        
        // Then: Should return -1 (EOF)
        assertEquals("Should return -1 after mark", -1, result);
    }

    @Test
    public void shouldAllowResetOnUnmarkedStream() throws IOException {
        // Given: An empty string reader (unmarked)
        StringReader emptyReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1);
        
        // When & Then: Reset should not throw exception
        boundedReader.reset(); // Should complete without exception
    }

    @Test
    public void shouldReadAfterMarkAndReset() throws IOException {
        // Given: A reader with content and a mark set
        StringReader sourceReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(sourceReader, 1805);
        boundedReader.mark(1);
        boundedReader.read();
        
        // When: Reading after the mark
        int result = boundedReader.read();
        
        // Then: Should return -1 (EOF)
        assertEquals("Should return -1 after reading past mark", -1, result);
    }

    @Test
    public void shouldSkipCharactersCorrectly() throws IOException {
        // Given: A reader with 2 characters and a mark
        StringReader sourceReader = new StringReader("wa");
        BoundedReader boundedReader = new BoundedReader(sourceReader, 10);
        boundedReader.mark(1);
        
        // When: Skipping 10 characters (more than available)
        long skipped = boundedReader.skip(10);
        
        // Then: Should skip only available characters
        assertEquals("Should skip only 1 character due to mark limit", 1L, skipped);
    }

    // ========== Exception Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenResettingNullReader() throws IOException {
        // Given: A bounded reader with null underlying reader
        BoundedReader boundedReader = new BoundedReader(null, 1);
        
        // When & Then: Reset should throw NullPointerException
        boundedReader.reset();
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenResettingClosedStream() throws IOException {
        // Given: A closed string reader
        StringReader sourceReader = new StringReader(LONG_TEXT);
        BoundedReader boundedReader = new BoundedReader(sourceReader, 0);
        boundedReader.close();
        
        // When & Then: Reset should throw IOException
        boundedReader.reset();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenReadingFromNullReader() throws IOException {
        // Given: A bounded reader with null underlying reader
        BoundedReader boundedReader = new BoundedReader(null, 1);
        char[] buffer = new char[1];
        
        // When & Then: Read should throw NullPointerException
        boundedReader.read(buffer, 1, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowArrayIndexOutOfBoundsForInvalidArrayAccess() throws IOException {
        // Given: A reader with small buffer but large offset
        StringReader sourceReader = new StringReader("4s");
        BoundedReader boundedReader = new BoundedReader(sourceReader, 179);
        char[] emptyBuffer = new char[0];
        
        // When & Then: Should throw ArrayIndexOutOfBoundsException
        boundedReader.read(emptyBuffer, 179, 179);
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenReadingFromClosedStream() throws IOException {
        // Given: A closed string reader
        StringReader sourceReader = new StringReader("");
        sourceReader.close();
        BoundedReader boundedReader = new BoundedReader(sourceReader, 214);
        char[] buffer = new char[0];
        
        // When & Then: Should throw IOException
        boundedReader.read(buffer, 214, 214);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenReadingSingleCharFromNullReader() throws IOException {
        // Given: A bounded reader with null underlying reader
        BoundedReader boundedReader = new BoundedReader(null, 1);
        
        // When & Then: Should throw NullPointerException
        boundedReader.read();
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenReadingSingleCharFromClosedStream() throws IOException {
        // Given: A closed string reader
        StringReader sourceReader = new StringReader("");
        sourceReader.close();
        BoundedReader boundedReader = new BoundedReader(sourceReader, 202);
        
        // When & Then: Should throw IOException
        boundedReader.read();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenMarkingNullReader() throws IOException {
        // Given: A bounded reader with null underlying reader
        BoundedReader boundedReader = new BoundedReader(null, 4168);
        
        // When & Then: Should throw NullPointerException
        boundedReader.mark(4168);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForNegativeMarkLimit() throws IOException {
        // Given: A reader with negative bound
        StringReader sourceReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(sourceReader, -431);
        
        // When & Then: Should throw IllegalArgumentException for negative mark limit
        boundedReader.mark(-431);
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenMarkingClosedStream() throws IOException {
        // Given: A closed string reader
        StringReader sourceReader = new StringReader("");
        sourceReader.close();
        BoundedReader boundedReader = new BoundedReader(sourceReader, 198);
        
        // When & Then: Should throw IOException
        boundedReader.mark(198);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenClosingNullReader() throws IOException {
        // Given: A bounded reader with null underlying reader
        BoundedReader boundedReader = new BoundedReader(null, -1235);
        
        // When & Then: Should throw NullPointerException
        boundedReader.close();
    }
}