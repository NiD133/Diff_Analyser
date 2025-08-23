package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that creating a view from an existing RandomAccessFileOrArray
     * results in a new, independent instance with its file pointer initialized to zero.
     *
     * This test improves upon the original by:
     * 1. Using the modern {@code createView()} method instead of a deprecated constructor.
     * 2. Explicitly checking that the new view's file pointer is independent of the original's.
     * 3. Using descriptive names for the test method and variables.
     *
     * @see RandomAccessFileOrArray#createView()
     */
    @Test
    public void createView_shouldCreateIndependentViewAtPositionZero() throws IOException {
        // Arrange: Create an original source from a byte array and move its file pointer
        // to a non-zero position to confirm the view's independence.
        byte[] sourceBytes = new byte[]{10, 20, 30};
        RandomAccessSource source = new RandomAccessSourceFactory().createSource(sourceBytes);
        RandomAccessFileOrArray original = new RandomAccessFileOrArray(source);
        original.seek(1L);

        // Act: Create an independent view of the original source.
        RandomAccessFileOrArray view = original.createView();

        // Assert: The new view's file pointer should be at the start (0),
        // and the original's pointer should remain unchanged.
        assertEquals("A newly created view must have its file pointer at position 0.", 0L, view.getFilePointer());
        assertEquals("Creating a view must not affect the original's file pointer.", 1L, original.getFilePointer());
    }
}