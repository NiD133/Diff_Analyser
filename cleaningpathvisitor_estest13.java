package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor}.
 * This test class focuses on the behavior of the equals() method.
 */
public class CleaningPathVisitor_ESTestTest13 extends CleaningPathVisitor_ESTest_scaffolding {

    /**
     * Tests that an instance of CleaningPathVisitor is always equal to itself,
     * verifying the reflexive property of the equals() method.
     */
    @Test
    public void testEquals_isReflexive() {
        // Arrange: Create a standard CleaningPathVisitor instance.
        final PathCounters pathCounters = Counters.longPathCounters();
        final String[] noFilesToSkip = {};
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, noFilesToSkip);

        // Act & Assert: An object must be equal to itself.
        // This is a fundamental contract of the Object.equals() method.
        assertEquals("A CleaningPathVisitor instance should be equal to itself.", visitor, visitor);
    }
}