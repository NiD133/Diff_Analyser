package org.apache.commons.io.function;

import org.junit.Test;

import java.util.function.Supplier;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#run(IORunnable, Supplier)} completes normally
     * when the provided runnable succeeds, even if the message supplier is null.
     * The message supplier should only be evaluated if an exception is thrown.
     */
    @Test
    public void runWithNullMessageSupplierShouldNotThrowExceptionWhenRunnableSucceeds() {
        // Arrange: Create a runnable that does nothing and will not throw an exception.
        // The message supplier is explicitly null to test this edge case.
        final IORunnable successfulRunnable = IORunnable.noop();
        final Supplier<String> nullMessageSupplier = null;

        // Act & Assert: The call to Uncheck.run should execute the runnable
        // and complete without throwing any exceptions. The absence of an
        // exception is the success condition for this test.
        Uncheck.run(successfulRunnable, nullMessageSupplier);
    }
}