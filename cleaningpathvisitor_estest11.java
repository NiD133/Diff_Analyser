package org.apache.commons.io.file;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor}, focusing on the equals() method contract.
 */
public class CleaningPathVisitorTest {

    /**
     * Tests that a CleaningPathVisitor instance is not equal to an instance of a different class
     * (in this case, DeletingPathVisitor), as required by the equals() contract.
     */
    @Test
    public void testEqualsReturnsFalseWhenComparedWithDifferentClass() {
        // Arrange: Create an instance of the class under test and an instance of a different class.
        final CountingPathVisitor cleaningVisitor = CleaningPathVisitor.withBigIntegerCounters();
        final Object otherVisitor = DeletingPathVisitor.withLongCounters();

        // Act: Compare the two objects using the equals() method.
        final boolean areEqual = cleaningVisitor.equals(otherVisitor);

        // Assert: Verify that the result is false.
        assertFalse("A CleaningPathVisitor should not be equal to a DeletingPathVisitor.", areEqual);
    }
}