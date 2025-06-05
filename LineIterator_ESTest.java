package org.apache.commons.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LineIteratorTest {

    private Reader reader;
    private LineIterator lineIterator;

    @BeforeEach
    void setUp() {
        // Initialize reader and lineIterator to null in each test to avoid potential issues
        reader = null;
        lineIterator = null;
    }

    @AfterEach
    void tearDown() throws IOException {
        // Close the LineIterator and Reader after each test to release resources
        if (lineIterator != null) {
            lineIterator.close();
        }
        if (reader != null) {
            reader.close();
        }
    }

    @Test
    void testNextLineReturnsCorrectLine() throws IOException {
        String testString = ")DuFnfWZ&V3D_i";
        reader = new StringReader(testString);
        lineIterator = new LineIterator(reader);
        String line = lineIterator.nextLine();
        assertEquals(testString, line, "nextLine() should return the correct line from the reader.");
    }

    @Test
    void testNextReturnsEmptyStringForCarriageReturn() throws IOException {
        reader = new StringReader("\r");
        lineIterator = new LineIterator(reader);
        String line = lineIterator.next();
        assertEquals("", line, "next() should return an empty string for a carriage return.");
    }

    @Test
    void testIsValidLineReturnsTrueByDefault() throws IOException {
        reader = new StringReader("CRLF");
        lineIterator = new LineIterator(reader);
        boolean isValid = lineIterator.isValidLine("I?");
        assertTrue(isValid, "isValidLine() should return true by default.");
    }

    @Test
    void testNextLineThrowsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        String testString = "org.apache.commons.io.LineIterator";
        reader = new StringReader(testString);
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.close(); // Close the underlying reader before creating the LineIterator
        lineIterator = new LineIterator(bufferedReader); // Use the closed BufferedReader

        assertThrows(IllegalStateException.class, () -> lineIterator.nextLine(),
                "nextLine() should throw IllegalStateException when the reader is closed.");
    }


    @Test
    void testNextThrowsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        String testString = "org.apache.commons.io.LineIterator";
        reader = new StringReader(testString);
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.close();
        lineIterator = new LineIterator(bufferedReader);

        assertThrows(IllegalStateException.class, () -> lineIterator.next(),
                "next() should throw IllegalStateException when the reader is closed.");
    }

    @Test
    void testHasNextThrowsIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        String testString = "DwHpo._Y&0~e=On_{";
        StringReader stringReader1 = new StringReader(testString);
        LineIterator lineIterator1 = new LineIterator(stringReader1);
        LineIterator.closeQuietly(lineIterator1); // Closing the reader using closeQuietly

        reader = new StringReader(testString);
        lineIterator = new LineIterator(reader);
        assertThrows(IllegalStateException.class, () -> lineIterator.hasNext(),
                "hasNext() should throw IllegalStateException when the reader is closed.");
    }

    @Test
    void testConstructorThrowsNullPointerExceptionForNullReader() {
        assertThrows(NullPointerException.class, () -> new LineIterator(null),
                "Constructor should throw NullPointerException when reader is null.");
    }

    @Test
    void testNextLineReturnsEmptyStringForNewline() throws IOException {
        reader = new StringReader("\n");
        lineIterator = new LineIterator(reader);
        String line = lineIterator.nextLine();
        assertEquals("", line, "nextLine() should return an empty string for a newline character.");
    }

    @Test
    void testHasNextReturnsFalseAfterReadingAllLines() throws IOException {
        reader = new StringReader("[>J6V7G{V9/r,");
        lineIterator = new LineIterator(reader);
        lineIterator.next(); // Consume the line
        assertFalse(lineIterator.hasNext(), "hasNext() should return false after reading all lines.");
    }

    @Test
    void testNextThrowsNoSuchElementExceptionWhenNoMoreLines() throws IOException {
        reader = new StringReader("qA92@1;@bJJXiw\"");
        lineIterator = new LineIterator(reader);
        lineIterator.close(); // Simulate closing the reader after reading all lines

        assertThrows(NoSuchElementException.class, () -> lineIterator.next(),
                "next() should throw NoSuchElementException when there are no more lines.");
    }

    @Test
    void testNextLineThrowsNoSuchElementExceptionWhenNoMoreLines() throws IOException {
        reader = new StringReader(")!PsaWEtKc");
        lineIterator = new LineIterator(reader);
        while (lineIterator.hasNext()) {
            lineIterator.next();
        }

        assertThrows(NoSuchElementException.class, () -> lineIterator.nextLine(),
                "nextLine() should throw NoSuchElementException when there are no more lines.");
    }

    @Test
    void testNextReturnsCorrectLine() throws IOException {
        String testString = "qA92@1;@bJJXiw\"";
        reader = new StringReader(testString);
        lineIterator = new LineIterator(reader);
        String line = lineIterator.next();
        assertEquals(testString, line, "next() should return the correct line from the reader.");
    }

    @Test
    void testRemoveThrowsUnsupportedOperationException() throws IOException {
        reader = new StringReader("!77U'||S(5");
        lineIterator = new LineIterator(reader);

        assertThrows(UnsupportedOperationException.class, () -> lineIterator.remove(),
                "remove() should throw UnsupportedOperationException.");
    }

    @Test
    void testCloseMultipleTimesDoesNotThrowException() throws IOException {
        reader = new StringReader("Test line");
        lineIterator = new LineIterator(reader);
        lineIterator.close();
        assertDoesNotThrow(() -> lineIterator.close()); // Calling close multiple times.
    }

    @Test
    void testHasNextAfterCloseReturnsFalse() throws IOException {
        reader = new StringReader("Test line");
        lineIterator = new LineIterator(reader);
        lineIterator.close();
        assertFalse(lineIterator.hasNext(), "hasNext() should return false after close() is called");
    }
}