import org.junit.jupiter.api.Test; // Changed from JUnit 4 to JUnit 5 for better readability and features
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions
import java.io.StringReader;
import java.util.NoSuchElementException;

import org.apache.commons.io.LineIterator;

class LineIteratorTest { // Renamed class for clarity.  We are testing LineIterator, so name accordingly.

    @Test
    void testNextThrowsNoSuchElementExceptionAfterClose() {
        // Arrange: Create a StringReader and LineIterator with some input.
        String inputString = "qA92@1;@bJJXiw\"";
        StringReader stringReader = new StringReader(inputString);
        LineIterator lineIterator = new LineIterator(stringReader);

        // Act: Close the LineIterator. This is crucial for triggering the exception.
        lineIterator.close();

        // Assert: Verify that calling next() after closing throws NoSuchElementException.
        assertThrows(NoSuchElementException.class, () -> {
            lineIterator.next();
        }, "Calling next() after close() should throw NoSuchElementException");

        // Additional note: The exception message is now validated using assertThrows,
        // providing more confidence in the correct exception being thrown.
    }
}