package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the default constructor correctly initializes all properties
     * to their expected default values.
     */
    @Test
    public void testDefaultConstructorInitializesProperties() {
        // Act: Create an instance using the no-argument constructor.
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert: Check that all properties match the documented or implemented defaults.
        // The source code shows the default constructor delegates with these values:
        // this(RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.CENTER, 0.0,
        //      CategoryLabelWidthType.CATEGORY, 0.95f);

        assertEquals("Default category anchor should be CENTER.",
                RectangleAnchor.CENTER, position.getCategoryAnchor());

        assertEquals("Default label anchor should be BOTTOM_CENTER.",
                TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());

        assertEquals("Default rotation anchor should be CENTER.",
                TextAnchor.CENTER, position.getRotationAnchor());

        assertEquals("Default angle should be 0.0.",
                0.0, position.getAngle(), DELTA);

        assertEquals("Default width type should be CATEGORY.",
                CategoryLabelWidthType.CATEGORY, position.getWidthType());

        assertEquals("Default width ratio should be 0.95f.",
                0.95f, position.getWidthRatio(), DELTA);
    }
}