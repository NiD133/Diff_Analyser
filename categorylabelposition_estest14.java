package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class, focusing on the equals() method.
 */
public class CategoryLabelPositionTest {

    @Test
    public void equals_shouldReturnFalse_whenRotationPropertiesDiffer() {
        // Arrange
        // Define common properties for two CategoryLabelPosition objects.
        RectangleAnchor commonCategoryAnchor = RectangleAnchor.BOTTOM_RIGHT;
        TextBlockAnchor commonLabelAnchor = TextBlockAnchor.CENTER_LEFT;
        CategoryLabelWidthType commonWidthType = CategoryLabelWidthType.RANGE;
        float widthRatio = -641.25F;

        // Create the first position using a constructor that sets default rotation properties
        // (angle = 0.0, rotationAnchor = TextAnchor.CENTER).
        CategoryLabelPosition positionWithDefaultRotation = new CategoryLabelPosition(
                commonCategoryAnchor, commonLabelAnchor, commonWidthType, widthRatio);

        // Create a second position with different, explicit rotation properties.
        CategoryLabelPosition positionWithCustomRotation = new CategoryLabelPosition(
                commonCategoryAnchor,
                commonLabelAnchor,
                TextAnchor.CENTER_RIGHT, // Different rotation anchor
                -641.25,                 // Different angle
                commonWidthType,
                widthRatio);

        // Act & Assert
        // The two objects should not be equal because their rotation angle and
        // rotation anchor properties differ, even if other properties are the same.
        assertNotEquals(positionWithDefaultRotation, positionWithCustomRotation);
    }
}