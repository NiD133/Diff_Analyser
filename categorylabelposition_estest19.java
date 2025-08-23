package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on the equals() method.
 */
public class CategoryLabelPosition_ESTestTest19 extends CategoryLabelPosition_ESTest_scaffolding {

    /**
     * Verifies that two CategoryLabelPosition instances are not equal if one is
     * created with the default constructor and the other is created with different
     * anchor points.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingDefaultInstanceToInstanceWithCustomAnchors() {
        // Arrange
        // An instance created with the default constructor.
        // Default anchors are RectangleAnchor.CENTER and TextBlockAnchor.BOTTOM_CENTER.
        CategoryLabelPosition defaultPosition = new CategoryLabelPosition();

        // An instance created with a specific constructor, providing custom anchors.
        // The other properties (angle, widthRatio) should fall back to their defaults.
        RectangleAnchor customCategoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor customLabelAnchor = TextBlockAnchor.CENTER_RIGHT;
        CategoryLabelPosition customPosition = new CategoryLabelPosition(customCategoryAnchor, customLabelAnchor);

        // Act & Assert
        // 1. Primary assertion: The two objects should not be equal due to different anchors.
        assertNotEquals("A default position should not be equal to a position with custom anchors.", defaultPosition, customPosition);

        // 2. Secondary assertions: Verify that the constructor for the custom position
        // correctly assigned the default values for the unspecified properties.
        assertEquals("The angle should default to 0.0.", 0.0, customPosition.getAngle(), 0.01);
        assertEquals("The width ratio should default to 0.95.", 0.95F, customPosition.getWidthRatio(), 0.01F);
    }
}