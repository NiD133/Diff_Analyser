package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
// Note: The original class name "Years_ESTestTest33" was likely auto-generated.
// In a real-world scenario, it would be renamed to "YearsTest".
public class Years_ESTestTest33 {

    /**
     * Tests that dividing a Years instance by 1 results in an equal Years instance.
     * This verifies the identity property of division for the MAX_VALUE constant.
     */
    @Test
    public void dividedBy_one_shouldReturnAnEquivalentYearsObject() {
        // Arrange: Start with a known Years value, in this case, the maximum possible.
        Years maxYears = Years.MAX_VALUE;

        // Act: Perform the division operation.
        Years result = maxYears.dividedBy(1);

        // Assert: The result should be an object equal to the original.
        // Using assertEquals on the objects themselves is more expressive than
        // comparing their internal integer values, as it tests the value object's contract.
        assertEquals(maxYears, result);
    }
}