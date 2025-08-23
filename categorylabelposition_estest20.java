package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the default constructor correctly initializes all properties
     * to their expected default values.
     */
    @Test
    public void defaultConstructorShouldSetCorrectDefaultValues() {
        // Arrange: Create an instance using the no-argument constructor.
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert: Verify that all properties are set to their expected defaults.
        // The source code shows these are the default values:
        // new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER,
        //         TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);

        assertEquals(RectangleAnchor.CENTER, position.getCategoryAnchor());
        assertEquals(TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(0.0, position.getAngle(), 0.001);
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.95f, position.getWidthRatio(), 0.001f);
    }
}