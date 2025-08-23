package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Tests the static factory methods for the {@link CleaningPathVisitor} class.
 */
public class CleaningPathVisitorFactoryTest {

    /**
     * Verifies that the {@code withLongCounters()} factory method successfully
     * creates and returns a non-null instance of a visitor.
     */
    @Test
    public void withLongCounters_should_returnNonNullInstance() {
        // Act
        // Call the static factory method to create a new visitor instance.
        final CountingPathVisitor visitor = CleaningPathVisitor.withLongCounters();

        // Assert
        // The factory method should always return a valid, non-null object.
        assertNotNull("The visitor created by withLongCounters() should not be null.", visitor);
    }
}