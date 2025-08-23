package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link ModuloAxis} class, focusing on the equals() method.
 */
public class ModuloAxisTest {

    /**
     * Verifies that the equals() method correctly handles changes to the display range.
     * The test confirms that two initially identical axes become unequal when one's
     * display range is changed, and equal again when the other's is changed to match.
     */
    @Test
    @DisplayName("equals() should be sensitive to changes in displayRange")
    void equals_correctlyConsidersDisplayRange() {
        // Arrange: Create two ModuloAxis instances with identical properties.
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));

        // Assert: Initially, the two axes should be equal.
        assertEquals(axis1, axis2, "Axes with identical initial properties should be equal");

        // Act: Change the display range of the first axis.
        axis1.setDisplayRange(0.1, 1.1);

        // Assert: The axes should now be unequal.
        assertNotEquals(axis1, axis2, "Axes should not be equal after changing one's display range");

        // Act: Apply the same display range change to the second axis.
        axis2.setDisplayRange(0.1, 1.1);

        // Assert: The axes should be equal again.
        assertEquals(axis1, axis2, "Axes should be equal again once their display ranges match");
    }
}