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
     * Verifies that the default constructor initializes the object with the
     * expected default values.
     */
    @Test
    public void testDefaultConstructorSetsDefaultValues() {
        // Arrange: Create a new instance using the default constructor.
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Act: No action is needed as we are testing the state after construction.

        // Assert: Check that all properties are set to their documented default values.
        // The no-arg constructor should initialize with:
        // angle = 0.0, widthRatio = 0.95f, widthType = CATEGORY,
        // categoryAnchor = CENTER, labelAnchor = BOTTOM_CENTER, rotationAnchor = CENTER.
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.95f, position.getWidthRatio(), 0.001f);
        assertEquals(0.0, position.getAngle(), 0.001);
        assertEquals(RectangleAnchor.CENTER, position.getCategoryAnchor());
        assertEquals(TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
    }
}