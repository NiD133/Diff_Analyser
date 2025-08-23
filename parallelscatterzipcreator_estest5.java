package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link ParallelScatterZipCreator} class.
 * This refactored test focuses on constructor behavior.
 */
public class ParallelScatterZipCreator_ESTestTest5 {

    /**
     * Verifies that the ParallelScatterZipCreator constructor can be successfully
     * instantiated even when provided with a null ScatterGatherBackingStoreSupplier.
     *
     * <p>This is valid behavior, as the backing store is only required when individual
     * scatter streams are created later, not during the initial construction of the creator itself.</p>
     */
    @Test
    public void constructorShouldSucceedWithNullBackingStoreSupplier() {
        // Arrange: Set up the necessary components for the test.
        // An ExecutorService is required by the constructor.
        ExecutorService executorService = new ForkJoinPool();
        ScatterGatherBackingStoreSupplier nullBackingStoreSupplier = null;

        // Act: Execute the code under test.
        // We expect this constructor call to complete without throwing an exception.
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executorService, nullBackingStoreSupplier);

        // Assert: Verify the outcome.
        // A non-null instance confirms that the object was created successfully.
        assertNotNull("The ParallelScatterZipCreator should be instantiated successfully.", creator);
    }
}