package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on exception handling.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.accept(IOIntConsumer, int) throws a NullPointerException
     * when the provided IOIntConsumer is null.
     */
    @Test(expected = NullPointerException.class)
    public void acceptIntConsumerShouldThrowNullPointerExceptionWhenConsumerIsNull() {
        // The integer value is arbitrary as it's not relevant to the null check.
        final int anyIntValue = 42;
        Uncheck.accept(null, anyIntValue);
    }
}