package org.apache.commons.io.file;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that an AccumulatorPathVisitor instance is not considered equal to its Builder,
     * as they are objects of different types.
     */
    @Test
    public void testVisitorIsNotEqualToItsBuilder() {
        // Arrange: Create a visitor from its builder.
        final AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        final AccumulatorPathVisitor visitor = builder.get();

        // Act & Assert: Verify that the visitor's equals() method returns false when
        // compared with an object of a different type (the builder).
        assertFalse("A visitor instance should not be equal to its builder.", visitor.equals(builder));
    }
}