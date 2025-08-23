package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the equals() method returns false for two CategoryLabelPosition
     * objects that differ only by their angle property.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnglesAreDifferent() {
        // Arrange: Define common properties for two CategoryLabelPosition instances.
        RectangleAnchor categoryAnchor = RectangleAnchor.RIGHT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.BOTTOM_CENTER;
        TextAnchor rotationAnchor = TextAnchor.BASELINE_CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        float widthRatio = -985.5677F;

        // Create a base instance with an angle of 0.0.
        CategoryLabelPosition position1 = new CategoryLabelPosition(
                categoryAnchor, labelAnchor, rotationAnchor, 0.0, widthType, widthRatio);

        // Create a second instance with a different angle but all other properties identical.
        CategoryLabelPosition position2 = new CategoryLabelPosition(
                categoryAnchor, labelAnchor, rotationAnchor, 986.2672, widthType, widthRatio);

        // Assert: The two objects should not be considered equal.
        assertNotEquals(position1, position2);
    }
}