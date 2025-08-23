package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    private static final double DELTA = 1e-9;

    /**
     * Tests that the default constructor initializes all properties to their
     * expected default values. This provides a clear specification of the
     * default state.
     */
    @Test
    public void defaultConstructor_shouldSetDefaultValues() {
        // Arrange
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert
        assertEquals(RectangleAnchor.CENTER, position.getCategoryAnchor());
        assertEquals(TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.0, position.getAngle(), DELTA);
        assertEquals(0.95f, position.getWidthRatio(), DELTA);
    }

    /**
     * Verifies that the equals() method correctly returns false when comparing
     * a CategoryLabelPosition instance with an object of a different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        CategoryLabelPosition position = new CategoryLabelPosition();
        Object otherObject = new Object();

        // Act
        boolean isEqual = position.equals(otherObject);

        // Assert
        assertFalse(isEqual);
    }
}