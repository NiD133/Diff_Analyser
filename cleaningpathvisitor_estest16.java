package org.apache.commons.io.file;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link CleaningPathVisitor}.
 * This class focuses on the constructor's behavior.
 */
public class CleaningPathVisitorTest {

    /**
     * Tests that the CleaningPathVisitor constructor handles a null 'skip' array gracefully
     * without throwing a NullPointerException.
     */
    @Test
    public void constructorWithNullSkipArrayShouldCreateInstance() {
        // Arrange: Create a path counter, a required dependency for the constructor.
        final PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        final String[] nullSkipArray = null;

        // Act: Call the constructor with the null array for paths to skip.
        // The constructor is expected to handle this case without errors.
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, nullSkipArray);

        // Assert: Verify that the visitor object was successfully created.
        assertNotNull("The visitor should be successfully instantiated even with a null skip array.", visitor);
    }
}