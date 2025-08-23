package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class contains an improved version of a test for the CountingPathVisitor.
 * The original test was automatically generated and lacked clarity and meaningful assertions.
 */
// The scaffolding and runner are kept to match the original test's context.
public class CountingPathVisitor_ESTestTest31 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests the contract of the hashCode() method for CountingPathVisitor.
     *
     * A well-behaved hashCode() method should satisfy these properties:
     * 1. Consistency: It returns the same integer on multiple calls for the same object.
     * 2. Equality: If two objects are equal according to their equals() method,
     *    then their hashCodes must be the same.
     * 3. Inequality (Good Practice): Unequal objects should ideally have different hash codes
     *    to ensure good performance in hash-based collections.
     */
    @Test(timeout = 4000)
    public void testHashCodeContract() {
        // Arrange: Create three visitor instances. Two are identical, and one is different.
        CountingPathVisitor visitor1 = CountingPathVisitor.withLongCounters();
        CountingPathVisitor visitor2 = CountingPathVisitor.withLongCounters();
        CountingPathVisitor differentVisitor = CountingPathVisitor.withBigIntegerCounters();

        // --- Assertions for Equal Objects ---

        // 1. Test for consistency
        assertEquals("hashCode() must be consistent across multiple calls on the same object.",
                visitor1.hashCode(), visitor1.hashCode());

        // 2. Test for equality
        // First, confirm the two objects are indeed equal.
        assertEquals("Two visitors created with the same configuration should be equal.",
                visitor1, visitor2);
        // Then, assert that their hash codes are also equal.
        assertEquals("Equal objects must have the same hash code.",
                visitor1.hashCode(), visitor2.hashCode());

        // --- Assertions for Unequal Objects ---

        // 3. Test for inequality (good practice check)
        // First, confirm the objects are not equal.
        assertNotEquals("Visitors with different counter types should not be equal.",
                visitor1, differentVisitor);
        // Then, check that their hash codes are different. While not strictly required
        // by the contract (due to hash collisions), it's a strong indicator of a
        // well-implemented hashCode function.
        assertNotEquals("Unequal objects should ideally have different hash codes.",
                visitor1.hashCode(), differentVisitor.hashCode());
    }
}