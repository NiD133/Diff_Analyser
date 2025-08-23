package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

/**
 * Tests for the equals() method in the MeterNeedle class and its subclasses.
 */
public class MeterNeedleEqualityTest {

    /**
     * Verifies that two needle instances are not equal if one has a different
     * highlight paint property.
     */
    @Test
    public void equals_shouldReturnFalse_whenHighlightPaintDiffers() {
        // Arrange: Create two identical PointerNeedle instances.
        // By default, their highlightPaint is null, so they are equal.
        PointerNeedle needle1 = new PointerNeedle();
        PointerNeedle needle2 = new PointerNeedle();
        assertTrue("Needles should be equal initially", needle1.equals(needle2));

        // Act: Change the highlight paint on only one of the needles.
        needle2.setHighlightPaint(Color.RED);

        // Assert: The needles should no longer be considered equal.
        assertFalse("Needles should not be equal after one's highlight paint is changed", needle1.equals(needle2));
    }

    /**
     * Verifies that two different types of MeterNeedle are not considered equal,
     * even if their properties are otherwise identical.
     *
     * This test case clarifies another potential ambiguity from the original code,
     * which compared a ShipNeedle to a PointerNeedle.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingDifferentNeedleTypes() {
        // Arrange
        MeterNeedle shipNeedle = new ShipNeedle();
        MeterNeedle pointerNeedle = new PointerNeedle();

        // Act & Assert
        assertFalse("Instances of different needle subclasses should not be equal", shipNeedle.equals(pointerNeedle));
    }
}