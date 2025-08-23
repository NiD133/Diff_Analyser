package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for {@link CountingPathVisitor}.
 * This test focuses on constructor validation.
 */
public class CountingPathVisitor_ESTestTest30 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that the constructor throws a NullPointerException when the fileFilter argument is null.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullFileFilter() {
        // Arrange: Create a valid PathCounters instance. The filters will be null.
        final PathCounters counters = CountingPathVisitor.defaultPathCounters();

        // Act & Assert: Attempt to create an instance with a null fileFilter and expect an exception.
        try {
            // The constructor validates that fileFilter is not null.
            // The directoryFilter is also null, but the check for fileFilter comes first.
            new CountingPathVisitor(counters, null, null);
            fail("Expected a NullPointerException because the fileFilter argument was null.");
        } catch (final NullPointerException e) {
            // The constructor uses Objects.requireNonNull(fileFilter, "fileFilter"),
            // so we expect the exception message to be "fileFilter".
            assertEquals("fileFilter", e.getMessage());
        }
    }
}