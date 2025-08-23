package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterNeedle} class, focusing on the size property.
 * This test specifically verifies the behavior of setting and getting the size,
 * including handling negative values.
 */
public class MeterNeedle_ESTestTest7 {

    /**
     * Verifies that the size of a needle can be set to a negative value
     * and that the getter correctly returns this value.
     */
    @Test
    public void setSizeShouldStoreAndGetNegativeValue() {
        // Arrange
        ShipNeedle needle = new ShipNeedle();
        int expectedSize = -1083;

        // Act
        needle.setSize(expectedSize);
        int actualSize = needle.getSize();

        // Assert
        assertEquals("The retrieved size should match the negative value that was set.",
                expectedSize, actualSize);
    }
}