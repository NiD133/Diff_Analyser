import org.junit.jupiter.api.Test; // Use JUnit 5 for clarity and modern features
import static org.junit.jupiter.api.Assertions.*; // Static imports for assertions

import java.io.StringReader;
import org.apache.commons.io.LineIterator;

class LineIteratorSimpleTest {  // More descriptive class name

    @Test
    void testNextReturnsLine() { // Clear method name describing the test's purpose
        String testString = "This is a line of text.\nThis is another line.";
        StringReader stringReader = new StringReader(testString);
        LineIterator lineIterator = new LineIterator(stringReader);

        try {
            String firstLine = lineIterator.next();
            assertEquals("This is a line of text.", firstLine, "The first line should be returned correctly.");

            String secondLine = lineIterator.next();
            assertEquals("This is another line.", secondLine, "The second line should be returned correctly.");

        } finally {
            LineIterator.closeQuietly(lineIterator); // Ensure resources are closed
        }
    }

    @Test
    void testHasNextReturnsFalseWhenNoMoreLines() {
        String testString = "Only one line here.";
        StringReader stringReader = new StringReader(testString);
        LineIterator lineIterator = new LineIterator(stringReader);

        try {
            lineIterator.next(); // Consume the line
            assertFalse(lineIterator.hasNext(), "hasNext() should return false after the last line is read.");
        } finally {
            LineIterator.closeQuietly(lineIterator);
        }
    }

    @Test
    void testRemoveUnsupported() {
        String testString = "Test line";
        StringReader stringReader = new StringReader(testString);
        LineIterator lineIterator = new LineIterator(stringReader);

        assertThrows(UnsupportedOperationException.class, () -> lineIterator.remove(), "remove() should throw UnsupportedOperationException");

        try {
            LineIterator.closeQuietly(lineIterator);
        } catch (Exception e) {
            // Handle the exception appropriately.  In general, exceptions thrown by closeQuietly should be logged.
            e.printStackTrace();
        }

    }

}