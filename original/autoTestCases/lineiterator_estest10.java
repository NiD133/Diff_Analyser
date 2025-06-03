import org.junit.jupiter.api.Test;  // Changed import for clarity and modernity
import static org.junit.jupiter.api.Assertions.*; // Updated assertions
import java.io.StringReader;
import java.util.NoSuchElementException;

public class LineIteratorBasicTest { // Renamed class for clarity

    @Test
    public void testIteratingThroughSingleLine() { // More descriptive test name
        String inputString = "This is a single line.";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);

        assertTrue(iterator.hasNext());
        assertEquals("This is a single line.", iterator.nextLine());
        assertFalse(iterator.hasNext());

        // Expect NoSuchElementException if nextLine() is called again
        assertThrows(NoSuchElementException.class, iterator::nextLine);

        LineIterator.closeQuietly(iterator); // Demonstrate closing the iterator.
    }

    @Test
    public void testIteratingThroughMultipleLines() { // More descriptive test name
        String inputString = "First line.\nSecond line.\nThird line.";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);

        assertTrue(iterator.hasNext());
        assertEquals("First line.", iterator.nextLine());

        assertTrue(iterator.hasNext());
        assertEquals("Second line.", iterator.nextLine());

        assertTrue(iterator.hasNext());
        assertEquals("Third line.", iterator.nextLine());

        assertFalse(iterator.hasNext());

        // Expect NoSuchElementException if nextLine() is called again
        assertThrows(NoSuchElementException.class, iterator::nextLine);

        LineIterator.closeQuietly(iterator); // Demonstrate closing the iterator.
    }


    @Test
    public void testEmptyString() { // More descriptive test name
        String inputString = "";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::nextLine);

        LineIterator.closeQuietly(iterator); // Demonstrate closing the iterator.
    }

    @Test
    public void testLineEndingWithNoNewline() { // More descriptive test name
        String inputString = "This is a line";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);

        assertTrue(iterator.hasNext());
        assertEquals("This is a line", iterator.nextLine());
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::nextLine);

        LineIterator.closeQuietly(iterator); // Demonstrate closing the iterator.
    }

    @Test
    public void testConsecutiveNewlines() { // More descriptive test name
        String inputString = "\n\nLine\n\n";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);

        assertTrue(iterator.hasNext());
        assertEquals("", iterator.nextLine());

        assertTrue(iterator.hasNext());
        assertEquals("", iterator.nextLine());

        assertTrue(iterator.hasNext());
        assertEquals("Line", iterator.nextLine());

        assertTrue(iterator.hasNext());
        assertEquals("", iterator.nextLine());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::nextLine);

        LineIterator.closeQuietly(iterator); // Demonstrate closing the iterator.
    }


    @Test
    public void testCloseQuietly() {
        String inputString = "Test line";
        StringReader reader = new StringReader(inputString);
        LineIterator iterator = new LineIterator(reader);
        LineIterator.closeQuietly(iterator);
        // No exceptions expected.  This test mainly ensures compilation and no runtime errors.
        //  A better test would use a mock reader and verify that close() was actually called.

    }

    // More tests can be added to cover other edge cases, like very long lines, different line endings, etc.
}