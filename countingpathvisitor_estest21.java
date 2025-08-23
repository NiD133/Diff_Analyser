package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CountingPathVisitor} class.
 * Note: This file was refactored from an auto-generated test suite.
 */
public class CountingPathVisitorTest { // Renamed from CountingPathVisitor_ESTestTest21

    /**
     * Tests that the equals() method is reflexive, meaning an object is always
     * equal to itself.
     */
    @Test
    public void testEquals_withSameInstance_shouldReturnTrue() {
        // Arrange: Create an instance of the visitor.
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

        // Act & Assert: Verify that the object is equal to itself.
        assertTrue("An object should always be equal to itself.", visitor.equals(visitor));
    }
}