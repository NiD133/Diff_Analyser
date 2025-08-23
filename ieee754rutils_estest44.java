package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#max(float, float, float)} correctly returns the largest
     * of three distinct positive float values.
     */
    @Test
    public void testMaxFloatWithThreeArgumentsReturnsLargestValue() {
        // Arrange
        final float val1 = 2162.2F;
        final float val2 = 0.0F;
        final float val3 = 2780.809F;
        final float expectedMax = 2780.809F;

        // Act
        final float actualMax = IEEE754rUtils.max(val1, val2, val3);

        // Assert
        assertEquals(expectedMax, actualMax, 0.01F);
    }
}