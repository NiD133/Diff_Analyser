package org.apache.commons.io.function;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#accept(IOTriConsumer, Object, Object, Object)}
     * executes without throwing an exception when the underlying consumer is a no-op.
     * This verifies the "happy path" where no I/O error occurs.
     */
    @Test
    public void testAcceptWithNoOpTriConsumerCompletesNormally() {
        // Arrange: A consumer that does nothing and throws no exceptions.
        final IOTriConsumer<String, String, String> noOpConsumer = IOTriConsumer.noop();

        // Act & Assert: The method should execute without throwing any exception.
        // The test implicitly passes if the following line does not throw.
        Uncheck.accept(noOpConsumer, "arg1", "arg2", "arg3");
    }

    /**
     * Tests that {@link Uncheck#accept(IOTriConsumer, Object, Object, Object)}
     * catches an {@link IOException} from the consumer and wraps it in an
     * {@link UncheckedIOException}. This is the primary purpose of the Uncheck utility.
     */
    @Test
    public void testAcceptWithThrowingTriConsumerWrapsIOException() {
        // Arrange: A consumer that always throws a specific IOException.
        final IOException cause = new IOException("Test I/O failure");
        final IOTriConsumer<Object, Object, Object> throwingConsumer = (t, u, v) -> {
            throw cause;
        };

        // Act & Assert
        try {
            Uncheck.accept(throwingConsumer, null, null, null);
            fail("Expected UncheckedIOException to be thrown");
        } catch (final UncheckedIOException e) {
            // Verify that the thrown exception is wrapping the original IOException.
            assertSame("The cause of the UncheckedIOException should be the original IOException.", cause, e.getCause());
        }
    }
}