package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that min(float, float, float) returns the correct value
     * when all three inputs are identical.
     */
    @Test
    public void testMinFloatWithThreeEqualValuesShouldReturnThatValue() {
        // Arrange
        final float value = 0.0F;
        final float expected = 0.0F;

        // Act
        final float actual = IEEE754rUtils.min(value, value, value);

        // Assert
        // The delta is set to 0.0F for an exact match.
        assertEquals(expected, actual, 0.0F);
    }
}