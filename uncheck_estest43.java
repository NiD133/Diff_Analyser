package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#accept(IOTriConsumer, Object, Object, Object)} throws a
     * {@link NullPointerException} when the consumer argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void acceptWithNullTriConsumerThrowsNullPointerException() {
        // Arrange: Define a null IOTriConsumer. The other arguments are arbitrary non-null values.
        final IOTriConsumer<String, String, String> nullConsumer = null;
        final String arg1 = "first argument";
        final String arg2 = "second argument";
        final String arg3 = "third argument";

        // Act & Assert: Calling accept with a null consumer should throw a NullPointerException.
        Uncheck.accept(nullConsumer, arg1, arg2, arg3);
    }
}