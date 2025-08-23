package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on specific edge cases.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that attempting to append data to a ByteArrayBuilder initialized
     * with a null internal buffer results in a NullPointerException.
     *
     * This test simulates an invalid internal state to ensure the class behaves
     * predictably even under unexpected conditions.
     */
    @Test(expected = NullPointerException.class)
    public void appendFourBytes_whenBufferIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a ByteArrayBuilder in an invalid state where its internal
        // buffer is null. The package-private fromInitial() factory method allows
        // us to set up this specific scenario for testing.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, -1);

        // Act: Attempt to append four bytes to the builder.
        // This action is expected to throw a NullPointerException because it will
        // try to access the null internal buffer.
        builder.appendFourBytes(0x1A2B3C4D);

        // Assert: The test passes if a NullPointerException is thrown, which is
        // handled by the @Test(expected = ...) annotation.
    }
}