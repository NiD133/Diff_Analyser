package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that max(float, float) returns the second argument when it is the larger value.
     */
    @Test
    public void maxFloat_shouldReturnSecondArgument_whenItIsLarger() {
        // Arrange
        final float smallerValue = 1.0F;
        final float largerValue = 1190.8F;
        final float expectedMax = 1190.8F;

        // Act
        final float actualMax = IEEE754rUtils.max(smallerValue, largerValue);

        // Assert
        assertEquals(expectedMax, actualMax, 0.0f);
    }
}