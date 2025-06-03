import org.apache.commons.io.LineIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LineIteratorNullReaderTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullReaderThrowsException() {
        // Arrange: No need to arrange, we are testing the constructor itself.

        // Act: Attempt to create a LineIterator with a null Reader.  This should throw an exception.
        try {
            new LineIterator(null); // This should throw the NullPointerException
            fail("Expected NullPointerException was not thrown."); // If it gets here, the test failed.
        } catch (NullPointerException e) {
            // Assert:  The exception is caught, verify the message is appropriate
            assertEquals("reader", e.getMessage());
            // Optionally print the stack trace for debugging
            //e.printStackTrace();  // Comment out in production code

            // Test passes because the expected exception was thrown.
        }
    }
}