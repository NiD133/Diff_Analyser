package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its exception-handling capabilities.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#appendTwoBytes(int)} throws a
     * {@link NullPointerException} when the builder has been initialized with a null internal buffer.
     *
     * This test ensures the builder behaves predictably even when its internal state is compromised,
     * for example, through the package-private factory method {@code fromInitial()}.
     */
    @Test(expected = NullPointerException.class)
    public void appendTwoBytes_shouldThrowNullPointerException_whenBufferIsNull() {
        // Arrange: Create a ByteArrayBuilder in an invalid state where its internal
        // buffer is null. The `fromInitial` static method is used to achieve this state.
        // The negative length is incidental; the null buffer is the critical factor.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, -1);

        // Act: Attempt to append two bytes. This operation requires a valid, non-null
        // buffer and is expected to fail.
        builder.appendTwoBytes(4000);

        // Assert: The test succeeds if a NullPointerException is thrown, as specified
        // by the `expected` parameter of the @Test annotation.
    }
}