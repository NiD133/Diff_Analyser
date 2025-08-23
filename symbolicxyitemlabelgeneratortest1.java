package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on object equality.
 */
class SymbolicXYItemLabelGeneratorTest {

    @Test
    @DisplayName("The equals() method should adhere to its contract")
    void testEqualsContract() {
        // Arrange: Create three identical generators to test the full equals() contract.
        // Since the class is stateless, all default-constructed instances should be equal.
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator3 = new SymbolicXYItemLabelGenerator();

        // --- Assert: Reflexivity ---
        // An object must be equal to itself.
        assertEquals(generator1, generator1, "An object should be equal to itself.");

        // --- Assert: Symmetry ---
        // If x.equals(y) is true, then y.equals(x) must be true.
        assertEquals(generator1, generator2, "Two default-constructed instances should be equal.");
        assertEquals(generator2, generator1, "Equality should be symmetric.");

        // --- Assert: Transitivity ---
        // If x.equals(y) and y.equals(z) are true, then x.equals(z) must be true.
        assertEquals(generator2, generator3, "Equality check for transitivity part 1 failed.");
        assertEquals(generator1, generator3, "Equality should be transitive.");

        // --- Assert: Inequality Cases ---
        // An object should not be equal to null.
        assertNotEquals(null, generator1, "An object should not be equal to null.");

        // An object should not be equal to an object of a different type.
        assertNotEquals(generator1, new Object(), "An object should not be equal to an object of a different type.");
    }
}