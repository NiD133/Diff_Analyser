package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that the toString() method executes successfully on a large array
     * without timing out and produces a string in the expected format.
     *
     * This test is a more robust version of an auto-generated test that used a large
     * array, likely to probe for performance issues or OutOfMemoryErrors. Instead of
     * relying on implicit crashes, this version explicitly checks for a valid output format.
     */
    @Test(timeout = 4000)
    public void toString_onLargeArray_completesSuccessfullyWithExpectedFormat() {
        // Arrange: Create a large array. A large size is used to ensure the
        // implementation is efficient and doesn't cause issues with memory or performance
        // under reasonable conditions.
        final int largeArraySize = 3813;
        AtomicDoubleArray largeArray = new AtomicDoubleArray(largeArraySize);

        // Act: Generate the string representation of the array.
        String result = largeArray.toString();

        // Assert: Verify the output is a non-null string with the correct
        // array-like format (i.e., starts with '[' and ends with ']').
        // A full string comparison is impractical for such a large array, but these
        // checks confirm the basic structure is correct. The @Test timeout also
        // implicitly verifies that the operation is performant enough.
        assertNotNull("The result of toString() should not be null.", result);
        assertTrue(
                "String representation should start with '['.",
                result.startsWith("[")
        );
        assertTrue(
                "String representation should end with ']'.",
                result.endsWith("]")
        );
    }

    /**
     * Verifies that toString() produces the correct format for a typical array.
     */
    @Test
    public void toString_returnsCorrectStringRepresentation() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[] {1.0, 2.5, -3.0});
        String expectedString = "[1.0, 2.5, -3.0]";

        // Act
        String actualString = array.toString();

        // Assert
        org.junit.Assert.assertEquals(expectedString, actualString);
    }

    /**
     * Verifies that toString() produces the correct format for an empty array.
     */
    @Test
    public void toString_onEmptyArray_returnsEmptyBrackets() {
        // Arrange
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(0);
        String expectedString = "[]";

        // Act
        String actualString = emptyArray.toString();

        // Assert
        org.junit.Assert.assertEquals(expectedString, actualString);
    }
}