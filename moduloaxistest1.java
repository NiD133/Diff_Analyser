package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the cloning functionality of the {@link ModuloAxis} class.
 */
@DisplayName("ModuloAxis Cloning")
class ModuloAxisTest {

    /**
     * Verifies that the clone() method creates a new instance that is a deep copy
     * of the original, meaning it is equal in value but not the same object reference.
     */
    @Test
    @DisplayName("Should create an independent and equal copy")
    void clone_shouldProduceIndependentCopy() throws CloneNotSupportedException {
        // Arrange: Create an original ModuloAxis instance.
        Range range = new Range(0.0, 1.0);
        ModuloAxis originalAxis = new ModuloAxis("Test Axis", range);

        // Act: Clone the original axis.
        ModuloAxis clonedAxis = (ModuloAxis) originalAxis.clone();

        // Assert: The clone should be a separate object but with an equal state.
        // The assertEquals check relies on a correctly implemented equals() method.
        assertNotSame(originalAxis, clonedAxis, "A clone must be a different object instance from the original.");
        assertEquals(originalAxis, clonedAxis, "A clone must be equal in value to the original.");
    }
}