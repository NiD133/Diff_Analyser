package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling the read(byte[]) method with a null buffer
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullBufferShouldThrowNullPointerException() throws Exception {
        // Arrange: Create an instance of RandomAccessFileOrArray with some dummy data.
        // The content of the data is irrelevant for this test.
        byte[] dummyData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);

        // Act: Call the method under test with a null argument.
        // This action is expected to throw the NullPointerException.
        fileOrArray.read((byte[]) null);

        // Assert: The test framework verifies that a NullPointerException was thrown.
        // If no exception is thrown, the test will fail automatically.
    }
}