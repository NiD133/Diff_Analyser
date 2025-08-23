package org.apache.commons.io.file;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 * This class focuses on specific behaviors of the visitor's methods.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that calling updateDirCounter with a null Path argument
     * results in a NullPointerException.
     */
    @Test
    public void updateDirCounterWithNullPathShouldThrowNullPointerException() {
        // Arrange
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        final IOException ioException = new IOException("Test exception");

        // Act & Assert
        // The method under test is expected to throw a NullPointerException because the 'dir' path is null.
        assertThrows(NullPointerException.class, () -> {
            visitor.updateDirCounter(null, ioException);
        });
    }
}