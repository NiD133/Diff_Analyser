package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 *
 * Note: The original test class name `CategoryLabelPosition_ESTestTest2` and its
 * inheritance from a scaffolding class have been removed to improve clarity and
 * focus on the test logic itself.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the constructor taking a width ratio correctly initializes the
     * object and sets a default angle of 0.0.
     */
    @Test
    public void constructorWithWidthRatioShouldSetRatioAndUseDefaultAngle() {
        // Arrange
        RectangleAnchor categoryAnchor = RectangleAnchor.CENTER;
        TextBlockAnchor labelAnchor = TextBlockAnchor.TOP_CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        float expectedWidthRatio = 0.0F;
        double expectedDefaultAngle = 0.0;

        // Act
        CategoryLabelPosition position = new CategoryLabelPosition(
                categoryAnchor, labelAnchor, widthType, expectedWidthRatio);

        // Assert
        assertEquals("The width ratio should be correctly set by the constructor.",
                expectedWidthRatio, position.getWidthRatio(), 0.01f);
        assertEquals("The angle should default to 0.0 for this constructor overload.",
                expectedDefaultAngle, position.getAngle(), 0.01);
    }
}