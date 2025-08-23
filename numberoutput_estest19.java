package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on handling invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a null buffer throws a
     * {@code NullPointerException}. The method requires a valid character array
     * to write the output into, so passing null is an invalid use case that
     * should fail fast.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // The specific long value (1L) and offset (0) are arbitrary for this test,
        // as the primary condition being tested is the null buffer argument.
        NumberOutput.outputLong(1L, null, 0);
    }
}