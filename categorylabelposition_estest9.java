package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that the constructor correctly initializes the object's state
     * and that the corresponding getters return the expected values.
     */
    @Test
    public void constructorShouldCorrectlySetAllProperties() {
        // Arrange: Define the parameters for creating a CategoryLabelPosition instance.
        RectangleAnchor categoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.CENTER_RIGHT;
        TextAnchor rotationAnchor = TextAnchor.TOP_RIGHT;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        double expectedAngle = -1.0;
        float expectedWidthRatio = 3650.8F;

        // Act: Create a new CategoryLabelPosition instance.
        CategoryLabelPosition position = new CategoryLabelPosition(
                categoryAnchor,
                labelAnchor,
                rotationAnchor,
                expectedAngle,
                widthType,
                expectedWidthRatio
        );

        // Assert: Check that the getters return the values passed to the constructor.
        // Note: The original test only checked angle and width ratio, but a thorough
        // constructor test should verify all properties.
        assertEquals(categoryAnchor, position.getCategoryAnchor());
        assertEquals(labelAnchor, position.getLabelAnchor());
        assertEquals(rotationAnchor, position.getRotationAnchor());
        assertEquals(widthType, position.getWidthType());
        assertEquals("The angle should match the value set in the constructor.",
                expectedAngle, position.getAngle(), DELTA);
        assertEquals("The width ratio should match the value set in the constructor.",
                expectedWidthRatio, position.getWidthRatio(), DELTA);
    }
}