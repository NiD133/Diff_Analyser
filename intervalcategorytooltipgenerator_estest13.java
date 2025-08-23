package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the hashCode() method of the {@link IntervalCategoryToolTipGenerator} class.
 *
 * Note: The original test class name '..._ESTestTest13' is preserved from the
 * input, but in a real-world scenario, it would be renamed to
 * 'IntervalCategoryToolTipGeneratorTest' for clarity.
 */
public class IntervalCategoryToolTipGenerator_ESTestTest13 {

    /**
     * Verifies that two equal IntervalCategoryToolTipGenerator instances produce the same hash code.
     * This test ensures that the class adheres to the general contract of Object.hashCode(),
     * which states that if two objects are equal, their hash codes must also be equal.
     */
    @Test
    public void hashCode_shouldReturnSameValue_forEqualInstances() {
        // Arrange: Create two identical generators using the default constructor.
        // These objects should be equal to each other by definition.
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator();

        // Assert: First, confirm that the two generators are indeed equal.
        // Then, verify that their hash codes match, fulfilling the contract.
        assertEquals("Two default-constructed generators should be equal.", generator1, generator2);
        assertEquals("Equal objects must have equal hash codes.", generator1.hashCode(), generator2.hashCode());
    }
}