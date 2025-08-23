package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ArrayFillTestTest2 extends AbstractLangTest {

    /**
     * Tests that ArrayFill.fill() returns null when the input boolean array is null.
     * This is the documented behavior, ensuring the method is null-safe.
     */
    @Test
    void fillShouldReturnNullForNullBooleanArray() {
        // Arrange: Define a null array. The value to fill with is irrelevant in this case.
        final boolean[] nullArray = null;

        // Act: Call the method under test with the null array.
        final boolean[] result = ArrayFill.fill(nullArray, true);

        // Assert: Verify that the result is null, as expected.
        assertNull(result);
    }
}