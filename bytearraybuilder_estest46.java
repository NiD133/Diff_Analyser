package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on constructor behavior.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the ByteArrayBuilder constructor throws a NegativeArraySizeException
     * when initialized with a negative size. This is the expected behavior from the
     * underlying Java array allocation.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void constructorShouldThrowExceptionForNegativeInitialSize() {
        // Attempt to create a ByteArrayBuilder with a negative initial capacity.
        // This action is expected to throw a NegativeArraySizeException.
        new ByteArrayBuilder(-1);
    }
}