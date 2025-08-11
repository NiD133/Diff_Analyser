package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Unit tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    @Test(expected = NullPointerException.class)
    public void constructor_withNullReader_throwsNullPointerException() {
        // Act
        new LineIterator(null);
    }

    @Test
    public void iteration_withMultipleLines_returnsAllLinesInOrder() {
        // Arrange
        String content = "Line 1\nLine 2\r\nLine 3";
        try (LineIterator iterator = new LineIterator(new StringReader(content))) {
            // Act & Assert
            assertTrue(iterator.hasNext());
            assertEquals("Line 1", iterator.next());

            assertTrue(iterator.hasNext());
            assertEquals("Line 2", iterator.next());

            assertTrue(iterator.hasNext());
            assertEquals("Line 3", iterator.next());

            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void hasNext_onEmptyReader_returnsFalse() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader(""))) {
            // Act & Assert
            assertFalse("hasNext() should return false for an empty reader", iterator.hasNext());
        }
    }

    @Test
    public void hasNext_whenLineAvailable_isIdempotent() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader("A single line"))) {
            // Act & Assert
            assertTrue("First call to hasNext() should be true", iterator.hasNext());
            assertTrue("Second call to hasNext() should also be true", iterator.hasNext());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void next_onEmptyReader_throwsNoSuchElementException() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader(""))) {
            // Act
            iterator.next(); // Should throw
        }
    }

    @Test
    public void next_whenNoMoreLines_throwsNoSuchElementException() {
        // Arrange
        LineIterator iterator = new LineIterator(new StringReader("The only line"));
        iterator.next(); // Consume the single line

        // Act & Assert
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void next_withOnlyNewline_returnsEmptyString() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader("\n"))) {
            // Act & Assert
            assertTrue(iterator.hasNext());
            assertEquals("A single newline should be treated as one empty line", "", iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextLine_withSingleLine_returnsTheLine() {
        // Arrange
        String line = "A single line";
        try (LineIterator iterator = new LineIterator(new StringReader(line))) {
            // Act
            String result = iterator.nextLine();
            // Assert
            assertEquals(line, result);
        }
    }

    @Test(expected = NoSuchElementException.class)
    @SuppressWarnings("deprecation")
    public void nextLine_onEmptyReader_throwsNoSuchElementException() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader(""))) {
            // Act
            iterator.nextLine(); // Should throw
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove_always_throwsUnsupportedOperationException() {
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader("any content"))) {
            // Act
            iterator.remove(); // Should throw
        }
    }

    @Test
    public void close_canBeCalledMultipleTimesWithoutError() {
        // Arrange
        LineIterator iterator = new LineIterator(new StringReader("some data"));
        try {
            // Act
            iterator.close();
            iterator.close(); // Should not throw
        } catch (IOException e) {
            fail("Closing the iterator multiple times should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void closeQuietly_marksIteratorAsFinished() {
        // Arrange
        LineIterator iterator = new LineIterator(new StringReader("some data"));
        assertTrue("Iterator should have a line before close", iterator.hasNext());

        // Act
        LineIterator.closeQuietly(iterator);

        // Assert
        // After close(), hasNext() returns false because the 'finished' flag is set.
        // It does not attempt to read from the now-closed stream.
        assertFalse("Iterator should not have a line after close", iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void hasNext_onExternallyClosedReader_throwsIllegalStateException() throws IOException {
        // Arrange: Create an iterator but close the underlying reader directly.
        Reader reader = new StringReader("some data");
        LineIterator iterator = new LineIterator(reader);
        reader.close();

        // Act: hasNext() attempts to read from the closed stream, causing an IOException
        // which is wrapped in an IllegalStateException.
        iterator.hasNext(); // Should throw
    }

    @Test(expected = IllegalStateException.class)
    public void next_onExternallyClosedReader_throwsIllegalStateException() throws IOException {
        // Arrange: Create an iterator but close the underlying reader directly.
        Reader reader = new StringReader("some data");
        LineIterator iterator = new LineIterator(reader);
        reader.close();

        // Act: next() also attempts to read, which fails on the closed stream.
        iterator.next(); // Should throw
    }

    @Test
    public void isValidLine_byDefault_returnsTrueForAllLines() {
        // This test verifies the default behavior of the protected method isValidLine().
        // Arrange
        try (LineIterator iterator = new LineIterator(new StringReader(""))) {
            // Act & Assert
            assertTrue("isValidLine should return true for a non-empty string", iterator.isValidLine("any string"));
            assertTrue("isValidLine should return true for an empty string", iterator.isValidLine(""));
            assertTrue("isValidLine should return true for a null value", iterator.isValidLine(null));
        }
    }
}