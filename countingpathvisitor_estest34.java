package org.apache.commons.io.file;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.junit.Test;

/**
 * Contains an improved test case for the {@link CountingPathVisitor} constructor.
 */
public class CountingPathVisitor_ESTestTest34 { // Class name kept for context with the original

    /**
     * Tests that the deprecated constructor throws a NullPointerException
     * when the 'visitFileFailed' function argument is null.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullVisitFileFailedFunction() {
        // Arrange: Set up the necessary non-null arguments for the constructor.
        final PathCounters counters = CountingPathVisitor.defaultPathCounters();
        final PathFilter fileFilter = FileFileFilter.INSTANCE;
        final PathFilter directoryFilter = FileFileFilter.INSTANCE;
        final IOBiFunction<Path, IOException, FileVisitResult> nullVisitFileFailedFunction = null;

        // Act & Assert: Verify that calling the constructor with a null function
        // results in a NullPointerException.
        // This constructor is deprecated, but its contract for non-null arguments
        // should still be enforced, typically by the superclass constructor.
        assertThrows(NullPointerException.class, () -> {
            new CountingPathVisitor(counters, fileFilter, directoryFilter, nullVisitFileFailedFunction);
        });
    }
}