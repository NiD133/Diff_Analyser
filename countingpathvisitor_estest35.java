package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * This is a refactored version of an auto-generated test for the {@link CountingPathVisitor} class.
 * The focus is on improving clarity, focus, and maintainability.
 */
// The original test class name 'CountingPathVisitor_ESTestTest35' and scaffolding are kept
// to match the original context. In a real-world scenario, these would be simplified.
public class CountingPathVisitor_ESTestTest35 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that a {@link CountingPathVisitor} created with the
     * {@link CountingPathVisitor#withBigIntegerCounters()} factory method
     * is properly initialized with a non-null counter object.
     */
    @Test
    public void getPathCounters_whenCreatedWithBigIntegerCounters_returnsNonNull() {
        // Arrange
        // The original auto-generated test used AccumulatorPathVisitor, a subclass.
        // This version has been refactored to test the base class CountingPathVisitor
        // directly, aligning the test with the intended class under test.
        final CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();

        // Act
        final Counters.PathCounters counters = visitor.getPathCounters();

        // Assert
        assertNotNull("The PathCounters instance should not be null after initialization.", counters);
    }
}