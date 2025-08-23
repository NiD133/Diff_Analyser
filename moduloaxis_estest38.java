package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.jfree.data.time.DateRange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * This test class contains the refactored test case.
 * The original class name 'ModuloAxis_ESTestTest38' and scaffolding are preserved
 * to match the context provided.
 */
public class ModuloAxis_ESTestTest38 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that a cloned ModuloAxis instance is equal to the original object,
     * but is not the same instance, fulfilling the contract of clone() and equals().
     */
    @Test
    public void clone_shouldReturnEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an original ModuloAxis instance with a specific range.
        Range fixedRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis originalAxis = new ModuloAxis("Test Axis", fixedRange);

        // Act: Clone the original axis.
        ModuloAxis clonedAxis = (ModuloAxis) originalAxis.clone();

        // Assert: The cloned object should be a different instance from the original.
        assertNotSame("The cloned object should be a new instance.", originalAxis, clonedAxis);

        // Assert: The cloned object should be considered equal to the original.
        assertEquals("The cloned object's state should be equal to the original's.", originalAxis, clonedAxis);

        // Further Assert: Verify that key properties were correctly copied to the clone.
        // The constructor sets default display values of 270.0 (start) and 90.0 (end).
        assertEquals("Cloned displayStart should match default value.", 270.0, clonedAxis.getDisplayStart(), 0.01);
        assertEquals("Cloned displayEnd should match default value.", 90.0, clonedAxis.getDisplayEnd(), 0.01);
    }
}