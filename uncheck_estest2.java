package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#accept(IOBiConsumer, Object, Object)} completes
     * successfully when the underlying consumer does not throw an {@link java.io.IOException}.
     */
    @Test
    public void acceptWithBiConsumerShouldNotThrowExceptionWhenNoIOExceptionOccurs() {
        // Arrange: Create a no-op IOBiConsumer that does nothing and never throws an exception.
        final IOBiConsumer<String, String> noOpConsumer = IOBiConsumer.noop();
        final String firstArgument = "test-string-1";
        final String secondArgument = "test-string-2";

        // Act & Assert: The call to Uncheck.accept should execute without throwing any exceptions.
        // This test implicitly passes if the following line executes without error.
        Uncheck.accept(noOpConsumer, firstArgument, secondArgument);
    }
}