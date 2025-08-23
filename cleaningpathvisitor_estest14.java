package org.apache.commons.io.file;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the equals() method in {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitor_ESTestTest14 {

    /**
     * Tests that the equals() method returns false when comparing a CleaningPathVisitor
     * instance with an object of a different type.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        // Create a standard CleaningPathVisitor instance. The specific configuration
        // does not matter for this test case.
        final Counters.PathCounters pathCounters = Counters.longPathCounters();
        final String[] filesToSkip = {}; // An empty array for simplicity.
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, filesToSkip);

        // An object of a different type to compare against.
        final Object nonVisitorObject = "a random string";

        // Act
        // Compare the visitor with the non-visitor object.
        final boolean isEqual = visitor.equals(nonVisitorObject);

        // Assert
        // The equals method should robustly handle comparisons with other types and return false.
        assertFalse("An instance of CleaningPathVisitor should not be equal to an instance of String.", isEqual);
    }
}