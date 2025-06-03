import org.junit.jupiter.api.Test; // Use JUnit 5 annotation
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

/**
 * Test case to verify the equality of different Byte Order Marks (BOMs).
 * This test checks if two distinct Byte Order Marks, UTF-16LE and UTF-16BE,
 * are considered equal using the `equals()` method.
 */
public class ByteOrderMarkEqualityTest {

    @Test
    void testUTF16LE_equals_UTF16BE_shouldReturnFalse() {
        // Arrange: Define two distinct Byte Order Mark constants.
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;  // Little Endian
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;  // Big Endian

        // Act: Compare the two BOMs using the equals() method.
        boolean areEqual = utf16LE.equals(utf16BE);

        // Assert: Verify that the equals() method returns false,
        // indicating that the two BOMs are not equal.
        assertFalse(areEqual, "UTF-16LE and UTF-16BE should not be considered equal.");
    }
}