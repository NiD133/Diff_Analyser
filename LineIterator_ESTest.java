package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Readable tests for LineIterator focusing on the public contract and typical usage.
 *
 * Notes:
 * - Tests use StringReader/BufferedReader for predictability.
 * - Each test follows Arrange-Act-Assert for clarity.
 * - Deprecated APIs are used only where needed and marked accordingly.
 */
public class LineIteratorTest {

    private static LineIterator iteratorOf(final String content) {
        return new LineIterator(new StringReader(content));
    }

    @Test
    public void next_returnsSingleLineFromReader() {
        // Arrange
        final LineIterator it = iteratorOf("hello");

        // Act
        final String line = it.next();

        // Assert
        assertEquals("hello", line);
        assertFalse("Iterator should be finished after consuming the only line", it.hasNext());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextLine_returnsSingleLineFromReader() {
        // Arrange
        final LineIterator it = iteratorOf("abc");

        // Act
        final String line = it.nextLine();

        // Assert
        assertEquals("abc", line);
        assertFalse(it.hasNext());
    }

    @Test
    public void isValidLine_defaultImplementationReturnsTrue() {
        // Arrange
        final LineIterator it = iteratorOf("anything");

        // Act
        final boolean valid = it.isValidLine("anything");

        // Assert
        assertTrue(valid);
    }

    @Test
    public void hasNext_onEmptyReader_returnsFalse() {
        // Arrange
        final LineIterator it = iteratorOf("");

        // Act + Assert
        assertFalse(it.hasNext());
    }

    @Test
    public void next_onEmptyReader_throwsNoSuchElementException() {
        // Arrange
        final LineIterator it = iteratorOf("");

        // Act + Assert
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextLine_onEmptyReader_throwsNoSuchElementException() {
        // Arrange
        final LineIterator it = iteratorOf("");

        // Act + Assert
        assertThrows(NoSuchElementException.class, it::nextLine);
    }

    @Test
    public void hasNext_calledTwiceWithoutAdvancing_remainsTrue() {
        // Arrange
        final LineIterator it = iteratorOf("one line");

        // Act
        final boolean first = it.hasNext();
        final boolean second = it.hasNext(); // still true because we haven't consumed the cached line via next()

        // Assert
        assertTrue(first);
        assertTrue(second);
    }

    @Test
    public void newlineOnly_inputReturnsEmptyStringAsLine_withNext() {
        // Arrange
        final LineIterator it = iteratorOf("\n");

        // Act
        final String firstLine = it.next();

        // Assert
        assertEquals("A single newline represents an empty line", "", firstLine);
        assertFalse("After consuming the only line, iterator must be finished", it.hasNext());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void newlineOnly_inputReturnsEmptyStringAsLine_withNextLine() {
        // Arrange
        final LineIterator it = iteratorOf("\n");

        // Prime internal state; not required but mirrors typical usage
        assertTrue(it.hasNext());

        // Act
        final String firstLine = it.nextLine();

        // Assert
        assertEquals("", firstLine);
        assertFalse(it.hasNext());
    }

    @Test
    public void constructor_nullReader_throwsNPEWithParameterName() {
        // Arrange
        final Reader nullReader = null;

        // Act
        final NullPointerException npe = assertThrows(NullPointerException.class, () -> new LineIterator(nullReader));

        // Assert
        // The implementation uses Objects.requireNonNull(reader, "reader")
        assertEquals("reader", npe.getMessage());
    }

    @Test
    public void hasNext_onClosedUnderlyingReader_throwsIllegalStateExceptionWithIOExceptionCause() throws IOException {
        // Arrange: close the underlying reader before constructing the iterator
        final StringReader reader = new StringReader("data");
        reader.close();
        final LineIterator it = new LineIterator(reader);

        // Act + Assert
        final IllegalStateException ex = assertThrows(IllegalStateException.class, it::hasNext);
        assertTrue("Expected IOException cause", ex.getCause() instanceof IOException);
    }

    @Test
    public void next_onClosedUnderlyingReader_throwsIllegalStateExceptionWithIOExceptionCause() throws IOException {
        // Arrange: close the underlying reader before constructing the iterator
        final StringReader reader = new StringReader("data");
        reader.close();
        final LineIterator it = new LineIterator(reader);

        // Act + Assert
        final IllegalStateException ex = assertThrows(IllegalStateException.class, it::next);
        assertTrue("Expected IOException cause", ex.getCause() instanceof IOException);
    }

    @Test
    public void close_canBeCalledMultipleTimes_andIteratorIsFinished() throws Exception {
        // Arrange
        final LineIterator it = iteratorOf("line");

        // Act: Close twice to verify idempotency
        it.close();
        it.close();

        // Assert
        assertFalse("After close(), iterator must be finished", it.hasNext());
        assertThrows("Calling next() after close should behave like empty iterator",
                NoSuchElementException.class, it::next);
    }

    @Test
    public void close_onBufferedReaderInstanceIsSupported() throws Exception {
        // Arrange
        final BufferedReader br = new BufferedReader(new StringReader("line"));
        final LineIterator it = new LineIterator(br);

        // Act + Assert: Should not throw
        it.close();
    }

    @Test
    public void remove_isUnsupported() {
        // Arrange
        final LineIterator it = iteratorOf("");

        // Act + Assert
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void closeQuietly_closesIterator_andHasNextReturnsFalse() {
        // Arrange
        final LineIterator it = iteratorOf("x");

        // Act
        LineIterator.closeQuietly(it);

        // Assert
        assertFalse(it.hasNext());
    }
}