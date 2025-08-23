package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the default constructor initializes the object with the
     * expected default values for its properties.
     */
    @Test
    public void defaultConstructorShouldSetDefaultProperties() {
        // Arrange: The SUT (System Under Test) is the default constructor.
        // Act: Create an instance of CategoryLabelPosition using the default constructor.
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert: Verify that the properties are set to their documented default values.
        // From the source, the default angle is 0.0 radians.
        assertEquals("Default angle should be 0.0", 0.0, position.getAngle(), DELTA);

        // From the source, the default width ratio is 0.95.
        assertEquals("Default width ratio should be 0.95f", 0.95f, position.getWidthRatio(), DELTA);
    }
}