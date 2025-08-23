package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.io.IOException;

/**
 * Tests for {@link ParallelScatterZipCreator}.
 * This test file focuses on exception handling scenarios.
 */
// The original test class name and inheritance structure are preserved.
public class ParallelScatterZipCreator_ESTestTest6 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Verifies that the writeTo() method throws a NullPointerException
     * when called with a null ZipArchiveOutputStream.
     */
    @Test(expected = NullPointerException.class)
    public void writeToShouldThrowNullPointerExceptionWhenTargetStreamIsNull() throws IOException, InterruptedException, ExecutionException {
        // Arrange: Create a ParallelScatterZipCreator. The ExecutorService is not
        // relevant for this test's logic, so it can be null.
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator((ExecutorService) null);

        // Act & Assert: Calling writeTo with a null argument should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        zipCreator.writeTo(null);
    }
}