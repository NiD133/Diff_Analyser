package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method in the {@link GridArrangement} class.
 */
public class GridArrangementEqualsTest {

    /**
     * Verifies that the equals() method returns false when comparing a GridArrangement
     * instance with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange: Create an instance of GridArrangement and a generic Object.
        GridArrangement gridArrangement = new GridArrangement(10, 20);
        Object otherObject = new Object();

        // Act & Assert: The equals method should return false as the types are incompatible.
        assertFalse(gridArrangement.equals(otherObject));
    }
}