package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * An improved and more understandable test for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * This test verifies the behavior of the lengthToJava2D method when the axis
     * range has been significantly resized and the input data length is a large
     * negative number. The initial setup uses a ModuloAxis with a default display
     * range (270-90) that is far outside its fixed range [0, 1], which represents
     * a specific edge case.
     */
    @Test
    public void testLengthToJava2DWithResizedRangeAndNegativeLength() {
        // Arrange
        final double plotWidth = 500.0;
        final double plotHeight = 1.0;
        final double dataLength = (double) Integer.MIN_VALUE; // -2.147483648E9
        final double resizeFactor = 1005.89236;
        final double expectedJava2dLength = -4.013087995539962E12;
        final double delta = 0.01;

        // A ModuloAxis with a fixed range of [0, 1]. The default display range
        // is 270 to 90, which wraps around.
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        Rectangle plotArea = new Rectangle((int) plotWidth, (int) plotHeight);
        RectangleEdge axisEdge = RectangleEdge.BOTTOM;

        // Act
        // Resizing the range updates the internal displayStart and displayEnd values,
        // which affects the 'displayLength' used in the conversion. This also
        // sets the auto-range flag to false.
        axis.resizeRange(resizeFactor);
        double actualJava2dLength = axis.lengthToJava2D(dataLength, plotArea, axisEdge);

        // Assert
        // The resizeRange method should disable auto-ranging.
        assertFalse("Auto-range should be disabled after resizing.", axis.isAutoRange());
        
        // The primary assertion for the length conversion.
        assertEquals("The calculated Java2D length should match the expected value.",
                expectedJava2dLength, actualJava2dLength, delta);
    }
}