package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that min(float, float, float) returns the input value
     * when all three arguments are identical.
     */
    @Test
    public void testMinFloat_shouldReturnInput_whenAllValuesAreEqual() {
        // Arrange
        final float value = 4616.2134F;
        final float expectedMin = 4616.2134F;
        final float delta = 0.01F;

        // Act
        final float actualMin = IEEE754rUtils.min(value, value, value);

        // Assert
        assertEquals(expectedMin, actualMin, delta);
    }
}