package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its behavior
 * under specific initialization conditions.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that attempting to write to a ByteArrayBuilder that was initialized
     * with a null internal buffer results in a NullPointerException.
     *
     * This scenario is possible through the package-private `fromInitial` factory method,
     * which allows setting the internal buffer directly.
     */
    @Test(expected = NullPointerException.class)
    public void writeToBuilderWithNullBufferShouldThrowNPE() {
        // Arrange: Create a ByteArrayBuilder with a null initial buffer.
        // The `fromInitial` method sets the internal buffer directly. When it's null,
        // any subsequent write operation is expected to fail.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, 0);

        // Act: Attempt to write a single byte to the builder.
        builder.write(42);

        // Assert: The test is expected to throw a NullPointerException.
        // The `expected` parameter of the @Test annotation handles this assertion.
        // If no exception or a different one is thrown, the test will fail.
    }
}