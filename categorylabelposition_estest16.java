package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on object equality.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that two CategoryLabelPosition instances created with the default
     * constructor are considered equal.
     */
    @Test
    public void twoDefaultInstancesShouldBeEqual() {
        // Arrange: Create two instances using the default constructor.
        // The default constructor creates a position with specific default values,
        // such as a 0.0 angle and a 0.95 width ratio.
        CategoryLabelPosition positionA = new CategoryLabelPosition();
        CategoryLabelPosition positionB = new CategoryLabelPosition();

        // Assert: The two instances should be equal, and their hash codes should match.
        // This confirms that the default constructor consistently initializes objects
        // to an identical state.
        assertEquals(positionA, positionB);
        assertEquals("Equal objects must have equal hash codes.", positionA.hashCode(), positionB.hashCode());

        // For clarity, explicitly verify the properties that were checked in the original test.
        // This makes it clear *why* the objects are considered equal.
        assertEquals("Default angle should be 0.0.", 0.0, positionA.getAngle(), 0.001);
        assertEquals("Default width ratio should be 0.95.", 0.95f, positionA.getWidthRatio(), 0.001f);
    }
}