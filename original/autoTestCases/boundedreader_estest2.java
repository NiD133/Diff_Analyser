import org.junit.jupiter.api.Test;  // Use JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions
import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderTest {  // More descriptive class name

    @Test
    public void testReadWithMark() throws IOException { // More descriptive method name
        String testString = "QN?";
        StringReader stringReader = new StringReader(testString);
        BoundedReader boundedReader = new BoundedReader(stringReader, 2680);

        // Read the first character ('Q')
        int firstChar = boundedReader.read();
        assertEquals('Q', firstChar, "First character should be Q (ASCII 81)");

        // Mark the current position (after reading 'Q')
        boundedReader.mark(2680); // Mark is used for reset, the limit here doesn't really matter

        // Read the next character ('N')
        int secondChar = boundedReader.read();
        assertEquals('N', secondChar, "Second character should be N (ASCII 78)");
    }
}