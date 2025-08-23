package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * CategoryLabelPosition instances with different properties.
     *
     * This test compares an instance created with custom values against an
     * instance created with the default constructor.
     */
    @Test
    public void equals_shouldReturnFalse_forInstancesWithDifferentProperties() {
        // Arrange: Create a position with specific, non-default values.
        // The default constructor uses an angle of 0.0 and a width ratio of 0.95f.
        CategoryLabelPosition customPosition = new CategoryLabelPosition(
                RectangleAnchor.CENTER,
                TextBlockAnchor.CENTER,
                TextAnchor.TOP_CENTER,
                1.0,  // Custom angle
                CategoryLabelWidthType.RANGE,
                0.0f  // Custom width ratio
        );

        // Arrange: Create a position using the default constructor.
        CategoryLabelPosition defaultPosition = new CategoryLabelPosition();

        // Act & Assert: The two instances should not be equal.
        assertFalse("Instances with different properties should not be equal",
                customPosition.equals(defaultPosition));

        // For context, verify some properties of the default instance that differ
        // from the custom one, confirming why the equals() check should fail.
        assertEquals(0.0, defaultPosition.getAngle(), 0.01);
        assertEquals(0.95f, defaultPosition.getWidthRatio(), 0.01f);
    }
}