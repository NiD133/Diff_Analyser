package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Paint;

/**
 * This test class has been improved for understandability.
 * The original test was auto-generated and lacked clarity.
 */
public class MeterNeedle_ESTestTest11 {

    /**
     * Tests that the setOutlinePaint() method correctly stores the paint,
     * which can then be retrieved by getOutlinePaint().
     */
    @Test
    public void setOutlinePaint_ShouldSetAndReturnCorrectPaint() {
        // Arrange: Create a concrete instance of MeterNeedle and a distinct test color.
        // Using a simple, standard color avoids the original test's complexity
        // of creating a Swing component just to get a color.
        MeterNeedle needle = new PlumNeedle();
        Paint expectedPaint = Color.BLUE;

        // Act: Set the outline paint on the needle instance.
        needle.setOutlinePaint(expectedPaint);

        // Assert: Verify that the getter returns the exact paint object that was set.
        Paint actualPaint = needle.getOutlinePaint();
        assertEquals("The retrieved paint should match the one that was set.", expectedPaint, actualPaint);
    }
}