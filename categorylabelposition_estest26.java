package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the default constructor correctly initializes the object
     * with the expected default values for angle and width ratio.
     */
    @Test
    public void constructor_withoutArguments_shouldSetDefaultValues() {
        // Arrange: Define the expected default values.
        // According to the source, the default angle is 0.0 radians
        // and the default width ratio is 0.95.
        final double expectedAngle = 0.0;
        final float expectedWidthRatio = 0.95f;
        final double tolerance = 0.000001;

        // Act: Create a new instance using the default constructor.
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert: Check that the properties match the expected default values.
        assertEquals("The default angle should be 0.0.",
                expectedAngle, position.getAngle(), tolerance);

        assertEquals("The default width ratio should be 0.95.",
                expectedWidthRatio, position.getWidthRatio(), tolerance);
    }
}