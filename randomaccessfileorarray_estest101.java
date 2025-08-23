package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * This test class contains tests for the {@link RandomAccessFileOrArray} class.
 * The original test class name and inheritance are kept to match the provided context.
 * In a real-world scenario, these would likely be simplified (e.g., to RandomAccessFileOrArrayTest).
 */
public class RandomAccessFileOrArray_ESTestTest101 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor {@link RandomAccessFileOrArray#RandomAccessFileOrArray(byte[])}
     * throws a {@link NullPointerException} when passed a null byte array.
     * This is the expected behavior, as an underlying data source cannot be created from a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullByteArray_shouldThrowNullPointerException() {
        // Act: Attempt to create an instance with a null byte array.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        new RandomAccessFileOrArray((byte[]) null);
    }
}