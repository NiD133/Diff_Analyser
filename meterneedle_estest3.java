package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MeterNeedle} class, focusing on its properties.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the setSize() method correctly updates the size property,
     * which can then be retrieved using getSize().
     */
    @Test
    public void setSizeShouldUpdateTheSizeProperty() {
        // Arrange: Create a concrete instance of MeterNeedle and define an expected size.
        // ShipNeedle is used here as a representative concrete subclass.
        ShipNeedle needle = new ShipNeedle();
        int expectedSize = 10;

        // Act: Set the size of the needle.
        needle.setSize(expectedSize);

        // Assert: Check if getSize() returns the value that was set.
        int actualSize = needle.getSize();
        assertEquals("The size should be updated after calling setSize.", expectedSize, actualSize);
    }
}