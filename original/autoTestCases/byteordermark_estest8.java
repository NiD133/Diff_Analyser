import org.junit.jupiter.api.Test; // Using JUnit 5 for clarity and conciseness
import static org.junit.jupiter.api.Assertions.*; // More readable assertions

import org.apache.commons.io.ByteOrderMark;

public class ByteOrderMarkConstructorNullBytesTest {

    @Test
    void testConstructorWithNullBytesThrowsException() {
        // Arrange: Prepare for the test.  We're testing the constructor of ByteOrderMark
        //          when passed a null array for the byte representation.
        String charsetName = "pLt'"; // Arbitrary charset name

        // Act & Assert:  Execute the code under test and verify the outcome.
        //                  We expect a NullPointerException to be thrown.
        assertThrows(NullPointerException.class, () -> {
            new ByteOrderMark(charsetName, (int[]) null);
        }, "Expected a NullPointerException when constructing ByteOrderMark with null bytes.");
    }
}