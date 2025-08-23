package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the hashCode() and equals() contract of {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that two equal AccumulatorPathVisitor instances have the same hash code,
     * and that two unequal instances have different hash codes.
     */
    @Test
    public void testHashCodeContract() {
        // Arrange
        // Create two identical visitors. They should be equal to each other.
        AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withBigIntegerCounters();
        AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withBigIntegerCounters();

        // Create a different visitor that should not be equal to the first two.
        AccumulatorPathVisitor differentVisitor = AccumulatorPathVisitor.withLongCounters();

        // Assert
        // 1. Verify the equals() contract as a prerequisite for testing hashCode().
        assertEquals("Two visitors created with the same configuration should be equal.", visitor1, visitor2);
        assertNotEquals("Visitors with different counter types should not be equal.", visitor1, differentVisitor);

        // 2. Verify the hashCode() contract.
        assertEquals("Equal objects must have equal hash codes.", visitor1.hashCode(), visitor2.hashCode());
        
        // 3. While not a strict requirement of the contract, unequal objects should
        //    ideally have different hash codes. This is a good check for the quality
        //    of the hashCode() implementation.
        assertNotEquals("Unequal objects should ideally have different hash codes.", visitor1.hashCode(), differentVisitor.hashCode());
    }
}