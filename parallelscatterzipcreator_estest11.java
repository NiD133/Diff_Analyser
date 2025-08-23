package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * This test case verifies the behavior of the ParallelScatterZipCreator class,
 * focusing on its handling of invalid arguments.
 */
// The original test class name and hierarchy are preserved to match the context.
public class ParallelScatterZipCreator_ESTestTest11 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Verifies that {@link ParallelScatterZipCreator#submitStreamAwareCallable(Callable)}
     * throws a NullPointerException when passed a null argument. This is the expected
     * behavior as the underlying ExecutorService rejects null tasks.
     */
    @Test(expected = NullPointerException.class)
    public void submitStreamAwareCallableShouldThrowNullPointerExceptionForNullCallable() {
        // Arrange: Create an instance of the class under test.
        final ParallelScatterZipCreator creator = new ParallelScatterZipCreator();

        // Act & Assert: Submitting a null callable should immediately throw a NullPointerException.
        // The @Test(expected = ...) annotation handles the assertion.
        creator.submitStreamAwareCallable(null);
    }
}