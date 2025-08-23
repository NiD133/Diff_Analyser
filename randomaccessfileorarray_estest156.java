package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that a newly created view from a RandomAccessFileOrArray
     * has its file pointer initialized to the beginning (position 0).
     */
    @Test(timeout = 4000)
    public void createView_shouldInitializeFilePointerToZero() throws IOException {
        // Arrange: Create a source RandomAccessFileOrArray from a dummy byte array.
        // The specific content or size of the array is not important for this test.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray original = new RandomAccessFileOrArray(sourceData);

        // Act: Create an independent view of the source data.
        RandomAccessFileOrArray view = original.createView();

        // Assert: The new view's file pointer should be at the start (position 0).
        assertEquals("The file pointer of a new view should be initialized to 0.", 0L, view.getFilePointer());
    }
}