package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}, focusing on the equals() method contract.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that an instance of AccumulatorPathVisitor is equal to itself,
     * verifying the reflexivity property of the equals() method.
     */
    @Test
    public void testEqualsIsReflexive() {
        // Arrange: Create an instance of the visitor.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();

        // Assert: An object must be equal to itself.
        // Using assertEquals is a standard and expressive way to test for object equality.
        assertEquals(visitor, visitor);
    }
}