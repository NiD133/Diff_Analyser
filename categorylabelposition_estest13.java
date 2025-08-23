package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the equals() method of the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionEqualsTest {

    /**
     * Verifies that two CategoryLabelPosition instances are not equal if they have
     * different angles and width ratios, even if other properties are the same.
     */
    @Test
    public void equals_shouldReturnFalse_whenPropertiesDiffer() {
        // Arrange
        RectangleAnchor anchor = RectangleAnchor.TOP;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.CENTER_RIGHT;

        // Create a position using a constructor that applies default values for angle (0.0)
        // and widthRatio (0.95f).
        CategoryLabelPosition positionWithDefaults = new CategoryLabelPosition(anchor, textBlockAnchor);

        // Create a second position with the same core anchors but with a non-default
        // angle and width ratio.
        double differentAngle = -447.68;
        float differentWidthRatio = -1488.0f;
        CategoryLabelPosition positionWithDifferentProperties = new CategoryLabelPosition(
                anchor,
                textBlockAnchor,
                TextAnchor.CENTER, // Same as the default
                differentAngle,
                CategoryLabelWidthType.CATEGORY, // Same as the default
                differentWidthRatio
        );

        // Act & Assert
        // The two objects should not be considered equal because their angle and widthRatio differ.
        assertNotEquals(positionWithDefaults, positionWithDifferentProperties);
    }
}