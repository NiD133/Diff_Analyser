import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.StringReader;

public class LineIterator_ReadLineWithNewlineTest { // More descriptive class name

    @Test(timeout = 1000) // Timeout in milliseconds, reduced for simplicity, more common for JUnit tests
    public void testReadLineReturnsEmptyStringForNewline() throws Exception { // More descriptive test name
        // Arrange:  Create a StringReader with a single newline character.
        StringReader stringReader = new StringReader("\n");

        // Wrap it in a BufferedReader for efficient reading.
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Create a LineIterator to read lines from the BufferedReader.
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Act:  Read the first line.
        String line = lineIterator.nextLine();

        // Assert:  The line should be an empty string (because the newline is considered the end of the line).
        assertEquals("The first line should be an empty string", "", line);

        // Cleanup (important!): Release resources.  In a real test suite,
        // LineIterator often needs explicit closing.  This example is simple
        // enough that it's not strictly *required*, but it's good practice.
        LineIterator.closeQuietly(lineIterator); // Use Apache Commons IO to close quietly.
    }
}