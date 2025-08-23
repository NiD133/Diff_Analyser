package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on its constructor and getters.
 */
public class CategoryLabelPositionTest {

    private static final double FLOATING_POINT_DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties of the
     * CategoryLabelPosition object, and that the corresponding getters return
     * these values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the properties for the CategoryLabelPosition.
        RectangleAnchor categoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.CENTER_RIGHT;
        TextAnchor rotationAnchor = TextAnchor.HALF_ASCENT_RIGHT;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.CATEGORY;
        double expectedAngle = 986.267245817;
        float expectedWidthRatio = 0.95F;

        // Act: Create a new CategoryLabelPosition instance using the main constructor.
        CategoryLabelPosition position = new CategoryLabelPosition(
                categoryAnchor,
                labelAnchor,
                rotationAnchor,
                expectedAngle,
                widthType,
                expectedWidthRatio);

        // Assert: Verify that each getter returns the value passed to the constructor.
        assertEquals("Category anchor should match the constructor argument.",
                categoryAnchor, position.getCategoryAnchor());
        assertEquals("Label anchor should match the constructor argument.",
                labelAnchor, position.getLabelAnchor());
        assertEquals("Rotation anchor should match the constructor argument.",
                rotationAnchor, position.getRotationAnchor());
        assertEquals("Angle should match the constructor argument.",
                expectedAngle, position.getAngle(), FLOATING_POINT_DELTA);
        assertEquals("Width type should match the constructor argument.",
                widthType, position.getWidthType());
        assertEquals("Width ratio should match the constructor argument.",
                expectedWidthRatio, position.getWidthRatio(), FLOATING_POINT_DELTA);
    }
}