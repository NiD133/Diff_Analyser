package org.apache.commons.io.file;

import static org.junit.Assert.assertThrows;

import java.nio.file.Path;
import java.util.Comparator;
import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 * This class contains a specific test case that was improved for understandability.
 */
public class AccumulatorPathVisitor_ESTestTest25 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that relativizeFiles() throws a NullPointerException when the parent path argument is null.
     */
    @Test
    public void relativizeFiles_whenParentPathIsNull_throwsNullPointerException() {
        // Arrange
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        final Path nullParentPath = null;
        final Comparator<Path> nullComparator = null;

        // Act & Assert
        // The method is expected to fail fast when the essential 'parent' argument is null.
        assertThrows(NullPointerException.class, () -> {
            visitor.relativizeFiles(nullParentPath, true, nullComparator);
        });
    }
}