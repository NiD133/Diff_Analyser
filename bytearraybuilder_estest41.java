package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on edge cases and error handling.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling `append()` on a ByteArrayBuilder initialized with a null
     * internal buffer throws a NullPointerException.
     *
     * This scenario can occur when using the static factory `fromInitial()` with a null byte array.
     * The `append()` method requires a non-null buffer to write to, and this test ensures
     * it fails as expected in this state.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void appendShouldThrowNullPointerExceptionWhenInitializedWithNullBuffer() {
        // GIVEN a ByteArrayBuilder created with a null initial buffer.
        // The initial length parameter is arbitrary for this test's purpose.
        int initialLength = 2078;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, initialLength);

        // WHEN attempting to append a byte to the builder
        // THEN a NullPointerException is expected because the internal buffer is null.
        builder.append(1);
    }
}