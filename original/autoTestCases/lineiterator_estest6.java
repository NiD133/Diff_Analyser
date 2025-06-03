import org.junit.jupiter.api.Test;  // Using JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; //JUnit 5 Assertions
import java.io.StringReader;
import java.io.IOException;

import org.apache.commons.io.LineIterator;

class LineIteratorTest {

    @Test
    void testHasNextAfterCloseQuietlyOnAnotherIterator() throws IOException {
        // Arrange: Create a StringReader and two LineIterators based on it
        StringReader stringReader = new StringReader("Line 1\nLine 2\n");
        LineIterator iterator1 = new LineIterator(stringReader);
        LineIterator iterator2 = new LineIterator(stringReader);

        // Act: Close the second iterator quietly (without throwing exceptions)
        LineIterator.closeQuietly(iterator2);

        // Assert:  Check if the first iterator throws an exception when hasNext() is called.
        // The expectation is that closing iterator2 also closes the underlying StringReader.
        // Therefore, iterator1 should throw an IllegalStateException when trying to access
        // the closed StringReader via hasNext().
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            iterator1.hasNext();
        });

        // Optionally, assert the exception message:
        assertEquals("java.io.IOException: Stream closed", exception.getMessage());


    }
}