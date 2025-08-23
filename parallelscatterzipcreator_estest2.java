package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Test suite for {@link ParallelScatterZipCreator}.
 * This class focuses on testing constructor argument validation.
 */
public class ParallelScatterZipCreator_ESTestTest2 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Tests that the ParallelScatterZipCreator constructor throws an IllegalArgumentException
     * when provided with a compression level that is outside the valid range (-1 to 9).
     */
    @Test(timeout = 4000)
    public void constructorShouldThrowIllegalArgumentExceptionForInvalidCompressionLevel() {
        // Arrange: Create mock dependencies, as their internal behavior is not relevant
        // for testing constructor argument validation.
        final ExecutorService executorService = mock(ExecutorService.class);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier = mock(ScatterGatherBackingStoreSupplier.class);

        // An invalid compression level, outside the accepted range of [-1, 9].
        final int invalidCompressionLevel = -1088;
        final String expectedErrorMessage = "Compression level is expected between -1~9";

        // Act & Assert: Attempt to create an instance with the invalid level and
        // verify that the correct exception and message are produced.
        try {
            new ParallelScatterZipCreator(executorService, backingStoreSupplier, invalidCompressionLevel);
            fail("Expected an IllegalArgumentException to be thrown for an invalid compression level.");
        } catch (final IllegalArgumentException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}