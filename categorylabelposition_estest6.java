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

    /**
     * Verifies that the constructor correctly initializes all properties and that
     * the corresponding getters return the expected values.
     */
    @Test
    public void constructorShouldCorrectlyInitializeProperties() {
        // Arrange: Define the expected properties for the CategoryLabelPosition.
        RectangleAnchor expectedCategoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor expectedLabelAnchor = TextBlockAnchor.BOTTOM_RIGHT;
        TextAnchor expectedRotationAnchor = TextAnchor.BOTTOM_LEFT;
        double expectedAngle = 0.0;
        CategoryLabelWidthType expectedWidthType = CategoryLabelWidthType.CATEGORY;
        float expectedWidthRatio = -234.34F;

        // Act: Create a new CategoryLabelPosition instance with the specified properties.
        CategoryLabelPosition position = new CategoryLabelPosition(
                expectedCategoryAnchor,
                expectedLabelAnchor,
                expectedRotationAnchor,
                expectedAngle,
                expectedWidthType,
                expectedWidthRatio
        );

        // Assert: Check that each property of the created object matches the expected value.
        assertEquals("The category anchor should match the constructor argument.",
                expectedCategoryAnchor, position.getCategoryAnchor());
        assertEquals("The label anchor should match the constructor argument.",
                expectedLabelAnchor, position.getLabelAnchor());
        assertEquals("The rotation anchor should match the constructor argument.",
                expectedRotationAnchor, position.getRotationAnchor());
        assertEquals("The angle should match the constructor argument.",
                expectedAngle, position.getAngle(), 0.0);
        assertEquals("The width type should match the constructor argument.",
                expectedWidthType, position.getWidthType());
        assertEquals("The width ratio should match the constructor argument.",
                expectedWidthRatio, position.getWidthRatio(), 0.0f);
    }
}