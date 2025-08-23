package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on constructor argument validation.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'labelAnchor' argument is null. This is a critical validation to ensure
     * the object is always in a consistent state.
     */
    @Test
    public void constructorShouldThrowExceptionForNullLabelAnchor() {
        // Arrange: Define valid arguments for the constructor, except for the one under test.
        RectangleAnchor categoryAnchor = RectangleAnchor.BOTTOM_LEFT;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        float widthRatio = 0.95f; // A typical value for the width ratio.
        TextBlockAnchor nullLabelAnchor = null;

        // Act & Assert: Attempt to create an instance with a null label anchor and verify the exception.
        try {
            new CategoryLabelPosition(categoryAnchor, nullLabelAnchor, widthType, widthRatio);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the cause of the error.
            assertEquals("Null 'labelAnchor' argument.", e.getMessage());
        }
    }
}