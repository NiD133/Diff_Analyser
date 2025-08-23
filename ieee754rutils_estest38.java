package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IEEE754rUtils}.
 */
class IEEE754rUtilsTest {

    @Test
    void maxFloatArrayShouldThrowExceptionForEmptyInput() {
        // Arrange: Create an empty float array.
        final float[] emptyArray = new float[0];

        // Act & Assert: Verify that calling max() with an empty array throws
        // an IllegalArgumentException with the expected message.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> IEEE754rUtils.max(emptyArray)
        );

        assertEquals("Array cannot be empty.", thrown.getMessage());
    }
}