import org.junit.jupiter.api.Test; // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5
import java.io.StringReader;
import org.apache.commons.io.LineIterator;

class LineIteratorSimpleTest {  // Renamed class for clarity

    @Test
    void testNextLineReturnsSingleLine() throws Exception { // Renamed test method for clarity
        // Arrange: Create a StringReader with a single line of text.
        String testString = "This is a single line of text.";
        StringReader stringReader = new StringReader(testString);

        // Act: Create a LineIterator and retrieve the next line.
        LineIterator lineIterator = new LineIterator(stringReader);
        String line = lineIterator.nextLine();

        // Assert: Verify that the retrieved line matches the original text.
        assertEquals(testString, line, "The line read from the iterator should match the input string.");

        // Ensure no more lines are available
        assertFalse(lineIterator.hasNext(), "The iterator should have no more lines after reading the first one.");

        // Clean up the iterator.  Important to release resources.
        LineIterator.closeQuietly(lineIterator);

        stringReader.close();

    }

    @Test
    void testHasNextWithNoMoreLines() throws Exception{
        String testString = "Just one Line";
        StringReader stringReader = new StringReader(testString);
        LineIterator lineIterator = new LineIterator(stringReader);
        lineIterator.nextLine();

        assertFalse(lineIterator.hasNext(), "The line iterator should return false");
        LineIterator.closeQuietly(lineIterator);
        stringReader.close();


    }

    @Test
    void testCloseQuietlyNullCheck() {
        // Test to make sure closeQuietly handles null without throwing error.
        LineIterator.closeQuietly(null); // Should not throw an exception
    }
}