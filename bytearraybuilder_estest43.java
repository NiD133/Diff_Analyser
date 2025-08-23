package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Tests for the {@link ByteArrayBuilder} class, focusing on constructor behavior.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the ByteArrayBuilder constructor throws a NegativeArraySizeException
     * when initialized with a negative size for its first block. This is the expected
     * behavior when any Java array is allocated with a negative size.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void constructorShouldThrowExceptionForNegativeInitialSize() {
        // Arrange: A negative value for the initial buffer size.
        final int negativeInitialSize = -1;

        // Act & Assert: Attempt to create a ByteArrayBuilder with the negative size.
        // The @Test(expected=...) annotation asserts that a NegativeArraySizeException is thrown.
        new ByteArrayBuilder(null, negativeInitialSize);
    }
}