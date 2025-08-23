package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the hashCode() and equals() contract of the {@link ModuloAxis} class.
 */
class ModuloAxisTest {

    /**
     * Verifies that two equal ModuloAxis instances have the same hash code,
     * as required by the Object.hashCode() contract.
     */
    @Test
    @DisplayName("Equal instances should have equal hash codes")
    void equalInstancesShouldHaveEqualHashCodes() {
        // Arrange: Create two identical ModuloAxis instances.
        // Equality is determined by the state passed to the constructor.
        String label = "Test Axis";
        Range range = new Range(0.0, 1.0);
        ModuloAxis axis1 = new ModuloAxis(label, range);
        ModuloAxis axis2 = new ModuloAxis(label, range);

        // Assert Precondition: The two instances must be equal for this test to be valid.
        assertEquals(axis1, axis2, "Precondition failed: The two axis instances should be equal.");

        // Act & Assert: The hash codes of the two equal objects must be the same.
        assertEquals(axis1.hashCode(), axis2.hashCode(), "Equal objects must return equal hash codes.");
    }
}