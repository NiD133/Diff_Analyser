import org.junit.jupiter.api.Test; // Use JUnit 5 annotations
import java.io.IOException;
import java.io.StringReader;
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

// A more descriptive class name, indicating the purpose of the test.
class BoundedReaderCornerCaseTest {

    @Test
    void testReadBeyondBounds() throws IOException {
        // Arrange:  Create a StringReader and a BoundedReader with a negative bound.
        String testString = "CU6^Ejr;7S;Ndl FK8";
        StringReader stringReader = new StringReader(testString);

        // Create a BoundedReader with a negative limit. This will likely result in unexpected behavior.
        BoundedReader boundedReaderWithNegativeLimit = new BoundedReader(stringReader, -1821);

        // Create another BoundedReader wrapped around the one with the negative limit, limited to a small number of characters.
        BoundedReader boundedReader = new BoundedReader(boundedReaderWithNegativeLimit, 1);

        // Act: Attempt to read a character from the nested BoundedReader.
        int result = boundedReader.read();

        // Assert:  Expect that reading will return -1, indicating the end of the stream or an error due to the negative bound.
        assertEquals(-1, result, "Reading from a BoundedReader with a negative bound and further limited should return -1.");
    }
}