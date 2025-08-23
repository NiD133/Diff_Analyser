package org.apache.commons.io.file;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Contains tests for the equals() and hashCode() contract of {@link CountingPathVisitor}.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that a CountingPathVisitor instance is not equal to an instance of a different,
     * unrelated visitor class. This test compares a CleaningPathVisitor (a subclass of
     * CountingPathVisitor) with an AccumulatorPathVisitor to ensure the equals() method
     * correctly handles different types.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentVisitorTypes() {
        // Arrange: Create two different types of path visitors.
        // CleaningPathVisitor extends CountingPathVisitor, while AccumulatorPathVisitor is a separate class.
        CountingPathVisitor cleaningVisitor = CleaningPathVisitor.withBigIntegerCounters();
        AccumulatorPathVisitor accumulatorVisitor = AccumulatorPathVisitor.withBigIntegerCounters();

        // Act & Assert: The equals method should return false when comparing objects of
        // incompatible types. Using assertNotEquals clearly expresses this intent.
        assertNotEquals(cleaningVisitor, accumulatorVisitor);
    }
}