package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on its equality logic.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the equals() method returns false for two CategoryLabelPosition
     * objects that differ only by their width ratio.
     */
    @Test
    public void equals_shouldReturnFalseWhenWidthRatioDiffers() {
        // Arrange: Create two positions with the same anchors but different width properties.
        RectangleAnchor anchor = RectangleAnchor.TOP_LEFT;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.BOTTOM_LEFT;

        // This constructor uses a default widthRatio of 0.95f.
        CategoryLabelPosition positionWithDefaultWidth = new CategoryLabelPosition(
                anchor, textBlockAnchor);

        // This constructor allows specifying a custom widthRatio. We use 0.0f to
        // create a distinct object. The widthType remains the default 'CATEGORY'.
        CategoryLabelPosition positionWithCustomWidth = new CategoryLabelPosition(
                anchor, textBlockAnchor, CategoryLabelWidthType.CATEGORY, 0.0F);

        // Act & Assert: The two objects should not be considered equal because their
        // widthRatio values (0.95f and 0.0f) are different.
        assertNotEquals(positionWithDefaultWidth, positionWithCustomWidth);

        // For completeness, ensure the comparison is symmetric.
        assertNotEquals(positionWithCustomWidth, positionWithDefaultWidth);
    }
}