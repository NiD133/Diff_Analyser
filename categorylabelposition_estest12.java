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
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'labelAnchor' argument is null.
     */
    @Test
    public void constructorShouldThrowExceptionForNullLabelAnchor() {
        // Arrange: A valid category anchor and a null label anchor.
        RectangleAnchor categoryAnchor = RectangleAnchor.BOTTOM_LEFT;
        TextBlockAnchor nullLabelAnchor = null;

        // Act & Assert
        try {
            new CategoryLabelPosition(categoryAnchor, nullLabelAnchor);
            fail("Expected an IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is correct, confirming the right check failed.
            assertEquals("Null 'labelAnchor' argument.", e.getMessage());
        }
    }
}