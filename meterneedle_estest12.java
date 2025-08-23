package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Paint;

/**
 * Tests for the {@link MeterNeedle} class and its subclasses.
 * This focuses on the highlight paint property.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the highlight paint can be set and retrieved correctly.
     * A call to setHighlightPaint should store the paint, and a subsequent
     * call to getHighlightPaint should return the same paint object.
     */
    @Test
    public void testSetAndGetHighlightPaint() {
        // Arrange: Create a needle instance and the paint to be set.
        // PlumNeedle is a concrete implementation of the abstract MeterNeedle.
        PlumNeedle needle = new PlumNeedle();
        Paint expectedPaint = Color.RED;

        // Act: Set the highlight paint on the needle.
        needle.setHighlightPaint(expectedPaint);
        Paint actualPaint = needle.getHighlightPaint();

        // Assert: The retrieved paint should be the same as the one set.
        assertEquals("The highlight paint returned by the getter should match the paint set by the setter.",
                expectedPaint, actualPaint);
    }
}