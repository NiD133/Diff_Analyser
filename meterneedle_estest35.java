package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Paint;

/**
 * This test class focuses on the MeterNeedle's property setters.
 *
 * Note: The original test was auto-generated and had a non-descriptive name (test34).
 * It also tested properties (size, rotation) that were unrelated to the method being
 * called (setOutlinePaint), making its purpose unclear. This revised test focuses on
 * correctly verifying the behavior of the setOutlinePaint() method.
 */
public class MeterNeedle_ESTestTest35 extends MeterNeedle_ESTest_scaffolding {

    /**
     * Verifies that calling setOutlinePaint() with a null value correctly
     * updates the needle's outline paint property.
     */
    @Test
    public void setOutlinePaint_withNullValue_updatesOutlinePaintProperty() {
        // Arrange: Create a concrete instance of MeterNeedle.
        ShipNeedle needle = new ShipNeedle();

        // Act: Set the outline paint to null.
        needle.setOutlinePaint(null);

        // Assert: Verify that the outline paint property is now null.
        assertNull("The outline paint should be null after being set to null.",
                   needle.getOutlinePaint());
    }
}