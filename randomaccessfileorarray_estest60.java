package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

// Note: The original test class name "RandomAccessFileOrArray_ESTestTest60" and its
// base class are preserved for context. In a real-world scenario, a more descriptive
// name like "RandomAccessFileOrArrayTest" would be preferable.
public class RandomAccessFileOrArray_ESTestTest60 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling skip() on an instance created with a null RandomAccessSource
     * results in a NullPointerException.
     *
     * This test ensures that the class correctly handles the invalid state of having
     * no underlying data source when methods that rely on it are called.
     */
    @Test(expected = NullPointerException.class)
    public void skipShouldThrowNullPointerExceptionWhenSourceIsNull() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance with a null internal source.
        // This simulates an invalid state where the object is not properly initialized.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to skip bytes. This action requires accessing the internal source,
        // which is null, and is expected to cause a NullPointerException.
        fileOrArray.skip(1L);

        // Assert: The test passes if a NullPointerException is thrown, as specified
        // by the 'expected' parameter in the @Test annotation.
    }
}