package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Uncheck#run(IORunnable)}.
 */
// Renamed from UncheckTestTest19 for clarity
public class UncheckRunTest {

    /**
     * Tests that {@link Uncheck#run(IORunnable)} executes a runnable that does not
     * throw an exception and completes normally.
     */
    @Test
    public void testRun_whenRunnableSucceeds_completesNormally() {
        // Arrange: A flag to verify that the runnable was indeed executed.
        final AtomicBoolean executed = new AtomicBoolean(false);
        final IORunnable successfulRunnable = () -> executed.set(true);

        // Act: Execute the runnable using the Uncheck utility.
        Uncheck.run(successfulRunnable);

        // Assert: The flag should be true, confirming execution.
        assertTrue(executed.get(), "The runnable should have been executed.");
    }

    /**
     * Tests that {@link Uncheck#run(IORunnable)} correctly wraps a thrown
     * {@link IOException} into an {@link UncheckedIOException}.
     */
    @Test
    public void testRun_whenRunnableThrowsIOException_throwsUncheckedIOException() {
        // Arrange: A specific IOException to be thrown by the runnable.
        final IOException expectedCause = new IOException("Test I/O failure");
        final IORunnable failingRunnable = () -> {
            throw expectedCause;
        };

        // Act & Assert: Verify that Uncheck.run throws UncheckedIOException
        // and that the cause is the original IOException.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.run(failingRunnable);
        });

        assertEquals(expectedCause, thrown.getCause(), "The cause of the UncheckedIOException should be the original IOException.");
    }
}