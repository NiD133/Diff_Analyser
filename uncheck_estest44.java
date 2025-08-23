package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that calling {@link Uncheck#accept(IOBiConsumer, Object, Object)} with a null consumer
     * throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void acceptWithNullBiConsumerShouldThrowNullPointerException() {
        // The IOBiConsumer argument is null, which is expected to cause the exception.
        // The other two arguments are arbitrary non-null values.
        Uncheck.accept((IOBiConsumer<String, String>) null, "input1", "input2");
    }
}