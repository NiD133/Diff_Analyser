package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the equals() method correctly returns false when comparing
     * two CategoryLabelPosition objects that differ in their width-related properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenWidthPropertiesDiffer() {
        // Arrange
        RectangleAnchor commonAnchor = RectangleAnchor.CENTER;
        TextBlockAnchor commonTextBlockAnchor = TextBlockAnchor.CENTER;

        // Create a position with a custom width type (RANGE) and a specific ratio.
        CategoryLabelPosition positionWithCustomWidth = new CategoryLabelPosition(
                commonAnchor,
                commonTextBlockAnchor,
                CategoryLabelWidthType.RANGE,
                428.78F);

        // Create a second position with the same anchors but using a constructor
        // that applies default width properties (CATEGORY type, 0.95f ratio).
        CategoryLabelPosition positionWithDefaultWidth = new CategoryLabelPosition(
                commonAnchor,
                commonTextBlockAnchor);

        // Act & Assert
        // The two objects share the same anchors but differ in widthType and widthRatio.
        // Therefore, they should not be considered equal. The assertNotEquals method
        // provides a clear, intention-revealing check.
        assertNotEquals(positionWithCustomWidth, positionWithDefaultWidth);

        // For clarity, we can also assert the default values that cause the inequality.
        assertEquals(CategoryLabelWidthType.CATEGORY, positionWithDefaultWidth.getWidthType());
        assertEquals(0.95F, positionWithDefaultWidth.getWidthRatio(), 0.0f);
    }
}