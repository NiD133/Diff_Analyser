package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for {@link CountingPathVisitor}.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that the single-argument constructor accepts a null PathCounters object.
     */
    @Test
    public void constructorShouldAcceptNullCounters() {
        // This test verifies that the constructor for CountingPathVisitor
        // accepts a null PathCounters object without throwing an exception.
        // Note: This behavior is inconsistent with other constructors in the class.
        // Using the resulting visitor will likely cause a NullPointerException later.
        // This test documents the current implementation's behavior.
        final CountingPathVisitor visitor = new CountingPathVisitor((PathCounters) null);

        // 1. Assert that the visitor object was successfully created.
        assertNotNull("The visitor should be created even with a null counter.", visitor);

        // 2. Assert that the internal counter is null, confirming the state.
        assertNull("The internal PathCounters should be null.", visitor.getPathCounters());
    }
}