package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;
import java.io.IOException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling close() on an instance created with a null RandomAccessSource
     * throws a NullPointerException.
     *
     * This test ensures that the class correctly handles null dependencies passed
     * during construction, failing fast when subsequent operations are attempted.
     */
    @Test(expected = NullPointerException.class)
    public void close_whenConstructedWithNullSource_throwsNullPointerException() throws IOException {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null underlying source.
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to close the instance.
        // The test expects this call to throw a NullPointerException.
        randomAccessFileOrArray.close();

        // Assert: The expected exception is declared in the @Test annotation.
    }
}