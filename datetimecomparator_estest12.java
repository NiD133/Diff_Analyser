package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the equals() method is reflexive for the time-only comparator.
     * According to the Java contract for Object.equals(), an object must be equal to itself.
     */
    @Test
    public void timeOnlyInstance_equals_isReflexive() {
        // Arrange
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act & Assert
        // The reflexive property of the equals contract states that x.equals(x) must be true.
        // We use assertEquals here as it's the idiomatic way to test this contract.
        assertEquals("A comparator instance should be equal to itself.", timeOnlyComparator, timeOnlyComparator);
    }
}