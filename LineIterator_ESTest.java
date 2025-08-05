/*
 * Test suite for LineIterator class from Apache Commons IO
 * Tests the functionality of iterating over lines in a Reader
 */

package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.apache.commons.io.LineIterator;

public class LineIteratorTest {

    // ========== Basic Line Reading Tests ==========
    
    @Test
    public void testNextLine_SingleLineContent_ReturnsCorrectLine() throws Throwable {
        // Given: A reader with single line content
        StringReader reader = new StringReader("_MA");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Reading the next line
        String actualLine = iterator.nextLine();
        
        // Then: Should return the expected content
        assertEquals("_MA", actualLine);
    }

    @Test
    public void testNext_SingleLineContent_ReturnsCorrectLine() throws Throwable {
        // Given: A reader with single line content
        StringReader reader = new StringReader("p)");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Reading the next line using next() method
        String actualLine = iterator.next();
        
        // Then: Should return the expected content
        assertEquals("p)", actualLine);
    }

    @Test
    public void testNext_EmptyLineContent_ReturnsEmptyString() throws Throwable {
        // Given: A reader with just a newline character (empty line)
        StringReader reader = new StringReader("\n");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Reading the next line
        String actualLine = iterator.next();
        
        // Then: Should return empty string
        assertEquals("", actualLine);
    }

    @Test
    public void testNextLine_EmptyLineWithBufferedReader_ReturnsEmptyString() throws Throwable {
        // Given: A buffered reader with just a newline character
        StringReader stringReader = new StringReader("\n");
        BufferedReader bufferedReader = new BufferedReader(stringReader, 1048);
        LineIterator iterator = new LineIterator(bufferedReader);
        
        // When: Checking if has next and reading the line
        iterator.hasNext();
        String actualLine = iterator.nextLine();
        
        // Then: Should return empty string
        assertEquals("", actualLine);
    }

    // ========== HasNext() Method Tests ==========

    @Test
    public void testHasNext_EmptyReader_ReturnsFalse() throws Throwable {
        // Given: An empty string reader
        StringReader reader = new StringReader("");
        BufferedReader bufferedReader = new BufferedReader(reader, 2302);
        LineIterator iterator = new LineIterator(bufferedReader);
        
        // When: Checking if has next
        boolean hasNext = iterator.hasNext();
        
        // Then: Should return false
        assertFalse(hasNext);
    }

    @Test
    public void testHasNext_ReaderWithContent_ReturnsTrue() throws Throwable {
        // Given: A reader with content
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Checking if has next multiple times
        iterator.hasNext();
        boolean hasNext = iterator.hasNext();
        
        // Then: Should consistently return true
        assertTrue(hasNext);
    }

    @Test
    public void testHasNext_AfterCloseQuietly_ReturnsFalse() throws Throwable {
        // Given: A reader with content that gets closed
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Closing the iterator quietly and checking hasNext
        LineIterator.closeQuietly(iterator);
        boolean hasNext = iterator.hasNext();
        
        // Then: Should return false
        assertFalse(hasNext);
    }

    // ========== Line Validation Tests ==========

    @Test
    public void testIsValidLine_ValidContent_ReturnsTrue() throws Throwable {
        // Given: A reader with content
        StringReader reader = new StringReader("w*N4EtL4abL*9i`");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Validating the line content
        boolean isValid = iterator.isValidLine("w*N4EtL4abL*9i`");
        
        // Then: Should return true (default implementation always returns true)
        assertTrue(isValid);
    }

    // ========== Resource Management Tests ==========

    @Test
    public void testClose_BufferedReader_ClosesSuccessfully() throws Throwable {
        // Given: A buffered reader with content
        StringReader stringReader = new StringReader("remove not supported");
        BufferedReader bufferedReader = new BufferedReader(stringReader, 2745);
        LineIterator iterator = new LineIterator(bufferedReader);
        
        // When: Closing the iterator
        iterator.close();
        
        // Then: Should close without throwing exceptions
        // (No assertion needed - test passes if no exception is thrown)
    }

    // ========== Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullReader_ThrowsNullPointerException() throws Throwable {
        // Given: A null reader
        Reader nullReader = null;
        
        // When: Creating LineIterator with null reader
        // Then: Should throw NullPointerException
        new LineIterator(nullReader);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextLine_EmptyReader_ThrowsNoSuchElementException() throws Throwable {
        // Given: An empty reader
        StringReader reader = new StringReader("");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Trying to read next line from empty reader
        // Then: Should throw NoSuchElementException
        iterator.nextLine();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_ExhaustedReader_ThrowsNoSuchElementException() throws Throwable {
        // Given: A reader that has been exhausted by another iterator
        StringReader reader = new StringReader("p)");
        LineIterator firstIterator = new LineIterator(reader);
        firstIterator.hasNext(); // This exhausts the reader
        LineIterator secondIterator = new LineIterator(reader);
        
        // When: Trying to read from exhausted reader
        // Then: Should throw NoSuchElementException
        secondIterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_Always_ThrowsUnsupportedOperationException() throws Throwable {
        // Given: Any LineIterator instance
        StringReader reader = new StringReader("");
        LineIterator iterator = new LineIterator(reader);
        
        // When: Calling remove method
        // Then: Should throw UnsupportedOperationException
        iterator.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextLine_ClosedReader_ThrowsIllegalStateException() throws Throwable {
        // Given: A closed reader
        StringReader reader = new StringReader("remove Kot supported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        
        // When: Trying to read from closed reader
        // Then: Should throw IllegalStateException
        iterator.nextLine();
    }

    @Test(expected = IllegalStateException.class)
    public void testNext_ClosedReader_ThrowsIllegalStateException() throws Throwable {
        // Given: A closed reader
        StringReader reader = new StringReader("remove Kot spported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        
        // When: Trying to read from closed reader using next()
        // Then: Should throw IllegalStateException
        iterator.next();
    }

    @Test(expected = IllegalStateException.class)
    public void testHasNext_ClosedReader_ThrowsIllegalStateException() throws Throwable {
        // Given: A closed reader
        StringReader reader = new StringReader("remove not supported");
        reader.close();
        LineIterator iterator = new LineIterator(reader);
        
        // When: Checking hasNext on closed reader
        // Then: Should throw IllegalStateException
        iterator.hasNext();
    }
}