package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 * This class name has been corrected from ArrayFillTestTest4 to ArrayFillTest
 * for clarity and standard naming conventions.
 */
class ArrayFillTest extends AbstractLangTest {

    @Test
    @DisplayName("fill() should return null when the input byte array is null")
    void fillWithNullByteArrayShouldReturnNull() {
        // Arrange: Define a null array. The fill value is arbitrary but required by the method signature.
        final byte[] nullArray = null;
        final byte fillValue = 1;

        // Act: Call the method under test with the null array.
        final byte[] result = ArrayFill.fill(nullArray, fillValue);

        // Assert: Verify that the result is null, as per the method's contract for null inputs.
        assertNull(result, "The result of filling a null array should be null.");
    }
}