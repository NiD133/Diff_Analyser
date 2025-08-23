package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 * This test class focuses on the behavior of the fill methods with null inputs.
 */
class ArrayFillTest extends AbstractLangTest {

    @Test
    void fillShouldReturnNullForNullShortArray() {
        // Arrange
        final short[] inputArray = null;
        // The fill value is arbitrary but required by the method signature.
        final short fillValue = 1;

        // Act
        final short[] result = ArrayFill.fill(inputArray, fillValue);

        // Assert
        assertNull(result, "Calling fill with a null array should return null.");
    }
}