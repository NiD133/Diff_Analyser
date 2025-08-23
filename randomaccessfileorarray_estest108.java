package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * This test verifies the behavior of the deprecated copy constructor for the 
 * RandomAccessFileOrArray class, specifically its handling of null input.
 */
// The original test class name and inheritance are preserved as they might be part of a larger test setup.
public class RandomAccessFileOrArray_ESTestTest108 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the copy constructor throws a NullPointerException when passed a null source.
     * This ensures the constructor correctly handles invalid input, preventing potential
     * runtime errors downstream.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_whenSourceIsNull() {
        // Act & Assert
        // Attempting to create a new RandomAccessFileOrArray from a null source
        // is expected to fail immediately with a NullPointerException.
        new RandomAccessFileOrArray((RandomAccessFileOrArray) null);
    }
}