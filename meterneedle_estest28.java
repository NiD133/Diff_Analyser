package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MeterNeedle} class.
 * Since MeterNeedle is abstract, these tests use a concrete subclass, {@link ShipNeedle},
 * to verify the behavior of the base class.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the equals() method returns false when compared with an object
     * of a different type, and confirms that this comparison does not alter the
     * needle's internal state.
     */
    @Test
    public void equals_withDifferentObjectType_shouldReturnFalseAndNotAlterState() {
        // Arrange: Create a ShipNeedle instance and an object of an unrelated type.
        ShipNeedle needle = new ShipNeedle();
        Object otherObject = new Object();

        // Act: Call the equals() method.
        boolean isEqual = needle.equals(otherObject);

        // Assert:
        // 1. The result of the comparison should be false.
        assertFalse("equals() should return false when comparing to a different object type.", isEqual);

        // 2. The state of the needle should remain unchanged, confirming no side effects.
        assertEquals("The rotateX property should not be modified by the equals() call.", 0.5, needle.getRotateX(), 0.01);
        assertEquals("The rotateY property should not be modified by the equals() call.", 0.5, needle.getRotateY(), 0.01);
        assertEquals("The size property should not be modified by the equals() call.", 5, needle.getSize());
    }
}