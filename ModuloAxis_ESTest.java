import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.axis.ModuloAxis;
import org.jfree.data.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test suite for the ModuloAxis class, focusing on clarity and maintainability.
 */
class ModuloAxisRefactoredTest {

    private ModuloAxis axis;
    private Rectangle2D.Double plotArea;

    @BeforeEach
    void setUp() {
        // Arrange: A common setup for many tests.
        // A ModuloAxis is often used for compass-like dials, so a 0-360 degree range is a good default.
        Range compassRange = new Range(0.0, 360.0);
        axis = new ModuloAxis("Angle", compassRange);
        plotArea = new Rectangle2D.Double(0, 0, 400, 400);
    }

    @Test
    void constructor_shouldSetDefaultDisplayRange() {
        // Assert: The default display range is from 270 to 90 degrees,
        // which is typical for a compass where North (0/360) is at the top.
        assertEquals(270.0, axis.getDisplayStart(), "Default display start should be 270.0");
        assertEquals(90.0, axis.getDisplayEnd(), "Default display end should be 90.0");
    }

    @Test
    void setDisplayRange_shouldUpdateDisplayStartAndEnd() {
        // Act
        axis.setDisplayRange(0.0, 180.0);

        // Assert
        assertEquals(0.0, axis.getDisplayStart());
        assertEquals(180.0, axis.getDisplayEnd());
    }

    @Test
    void setDisplayRange_shouldHandleWrappedRange() {
        // Act: Set a range that wraps around the 360/0 point.
        axis.setDisplayRange(350.0, 10.0);

        // Assert
        assertEquals(350.0, axis.getDisplayStart());
        assertEquals(10.0, axis.getDisplayEnd());
    }

    @Test
    void valueToJava2D_shouldMapValueToCoordinate() {
        // Arrange: Display the full 0-360 range on a 360-pixel wide area for simple mapping.
        axis.setDisplayRange(0.0, 360.0);
        plotArea.setRect(0, 0, 360, 100); // 1 pixel = 1 degree

        // Act
        double coordinate = axis.valueToJava2D(90.0, plotArea, RectangleEdge.BOTTOM);

        // Assert: Value 90.0 should map to coordinate 90.0.
        assertEquals(90.0, coordinate, 0.001);
    }

    @Test
    void valueToJava2D_shouldMapValueToCoordinateWhenInverted() {
        // Arrange
        axis.setDisplayRange(0.0, 360.0);
        axis.setInverted(true);
        plotArea.setRect(0, 0, 360, 100);

        // Act
        double coordinate = axis.valueToJava2D(90.0, plotArea, RectangleEdge.BOTTOM);

        // Assert: On an inverted axis, 90.0 should map to the opposite side.
        // Total width 360 - 90 = 270.
        assertEquals(270.0, coordinate, 0.001);
    }

    @Test
    void valueToJava2D_shouldHandleWrappedDisplayRange() {
        // Arrange: Display is from 270 to 90 degrees, wrapping at 360/0.
        // This covers 180 degrees of the total range (270-360 and 0-90).
        // The plot area is 180 pixels wide to maintain a 1-to-1 mapping.
        axis.setDisplayRange(270.0, 90.0);
        plotArea.setRect(0, 0, 180, 100);

        // Act
        // Value 315 is in the first part of the wrapped range (270-360).
        double coordinate1 = axis.valueToJava2D(315.0, plotArea, RectangleEdge.BOTTOM);
        // Value 45 is in the second part of the wrapped range (0-90).
        double coordinate2 = axis.valueToJava2D(45.0, plotArea, RectangleEdge.BOTTOM);

        // Assert
        // 315 is 45 units from 270, so it should map to coordinate 45.
        assertEquals(45.0, coordinate1, 0.001);
        // 45 is 45 units from 0, and follows the first 90-unit section (360-270).
        // So it should map to 90 + 45 = 135.
        assertEquals(135.0, coordinate2, 0.001);
    }

    @Test
    void valueToJava2D_shouldReturnNaN_whenValueIsOutsideWrappedDisplayRange() {
        // Arrange: Display from 300 to 60 degrees. Values between 60 and 300 are not visible.
        axis.setDisplayRange(300.0, 60.0);
        double valueNotInRange = 180.0;

        // Act
        double coordinate = axis.valueToJava2D(valueNotInRange, plotArea, RectangleEdge.LEFT);

        // Assert: The method should return NaN for values that are not currently displayed.
        assertTrue(Double.isNaN(coordinate));
    }

    @Test
    void java2DToValue_shouldMapCoordinateToValue() {
        // Arrange
        axis.setDisplayRange(0.0, 360.0);
        plotArea.setRect(0, 0, 360, 100);

        // Act
        double value = axis.java2DToValue(90.0, plotArea, RectangleEdge.BOTTOM);

        // Assert
        assertEquals(90.0, value, 0.001);
    }

    @Test
    void java2DToValue_shouldMapCoordinateToValueWhenInverted() {
        // Arrange
        axis.setDisplayRange(0.0, 360.0);
        axis.setInverted(true);
        plotArea.setRect(0, 0, 360, 100);

        // Act
        double value = axis.java2DToValue(90.0, plotArea, RectangleEdge.BOTTOM);

        // Assert: Coordinate 90 on an inverted axis maps to value 270.
        assertEquals(270.0, value, 0.001);
    }

    @Test
    void resizeRange_shouldExpandRangeAroundCenter() {
        // Arrange: A simple 100-unit axis.
        axis.setRange(0, 100);
        axis.setDisplayRange(0, 100);

        // Act: Resize by 200% (double the range).
        axis.resizeRange(2.0);

        // Assert: The range should double, centered on the original midpoint (50).
        // New range length is 200. Center is 50. So, 50 - 100 to 50 + 100.
        assertEquals(-50.0, axis.getLowerBound(), 0.001);
        assertEquals(150.0, axis.getUpperBound(), 0.001);
    }

    @Test
    void resizeRange_shouldThrowException_whenRangeIsNull() {
        // Arrange
        ModuloAxis axisWithNullRange = new ModuloAxis("Test", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            axisWithNullRange.resizeRange(2.0);
        }, "Resizing with a null fixedRange should throw an exception.");
    }

    @Test
    void equals_shouldReturnTrueForIdenticalObjects() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0, 100));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0, 100));

        // Act & Assert
        assertTrue(axis1.equals(axis2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentDisplayRange() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0, 100));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0, 100));
        axis2.setDisplayRange(10, 90);

        // Act & Assert
        assertFalse(axis1.equals(axis2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentFixedRange() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0, 100));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0, 200));

        // Act & Assert
        assertFalse(axis1.equals(axis2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentObjectType() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0, 100));
        String notAnAxis = "I am not an axis";

        // Act & Assert
        assertFalse(axis1.equals(notAnAxis));
    }
}