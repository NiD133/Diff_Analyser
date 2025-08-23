package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains refactored tests for the RandomAccessFileOrArray class.
 * The original test was auto-generated and has been improved for clarity and maintainability.
 */
public class RandomAccessFileOrArray_ESTestTest158 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readDouble() throws a NullPointerException when the
     * RandomAccessFileOrArray is constructed with a null RandomAccessSource.
     * This is the expected behavior, as any read operation will attempt to
     * delegate to the underlying source, which is null in this case.
     */
    @Test(expected = NullPointerException.class)
    public void readDouble_whenSourceIsNull_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null source.
        // The cast to RandomAccessSource is necessary to resolve constructor ambiguity.
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read a double from the null source.
        // This action is expected to throw a NullPointerException.
        randomAccessFileOrArray.readDouble();

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected = ...) annotation.
    }
}