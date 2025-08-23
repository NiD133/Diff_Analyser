package org.apache.commons.io.file;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the equals method in {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitorEqualsTest {

    @Test
    public void equals_shouldReturnFalse_whenVisitorsHaveDifferentConfigurations() {
        // Arrange
        // Visitor A: Created using a factory method. It uses BigInteger counters
        // and has default delete options (i.e., 'overrideReadOnly' is false).
        final CleaningPathVisitor visitorA = (CleaningPathVisitor) CleaningPathVisitor.withBigIntegerCounters();

        // Visitor B: Created with an explicit configuration that differs from Visitor A.
        // It uses standard 'long' counters and is set to override read-only files.
        final Counters.PathCounters longCounters = CountingPathVisitor.defaultPathCounters();
        final DeleteOption[] deleteOptions = {StandardDeleteOption.OVERRIDE_READ_ONLY};
        final CleaningPathVisitor visitorB = new CleaningPathVisitor(longCounters, deleteOptions);

        // Act & Assert
        // The visitors are expected to be unequal because their configurations differ
        // in both the counter type and the 'overrideReadOnly' setting.
        assertNotEquals(visitorA, visitorB);
    }
}