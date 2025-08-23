package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on constructor argument validation.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'labelAnchor' argument is null.
     */
    @Test
    public void constructor_shouldThrowException_forNullLabelAnchor() {
        // Arrange: Set up valid arguments for the constructor.
        // The specific values for other arguments are not relevant to this test.
        RectangleAnchor categoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextAnchor rotationAnchor = TextAnchor.CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.CATEGORY;
        double angle = 0.0;
        float widthRatio = 0.95f;

        // Act & Assert: Call the constructor with a null labelAnchor and verify the exception.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CategoryLabelPosition(
                categoryAnchor,
                null, // The invalid argument under test
                rotationAnchor,
                angle,
                widthType,
                widthRatio
            );
        });

        // Assert: Verify the exception message for more precise testing.
        assertEquals("Null 'labelAnchor' argument.", exception.getMessage());
    }
}