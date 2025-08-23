package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test suite is for the {@link RandomAccessFileOrArray} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class RandomAccessFileOrArray_ESTestTest106 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor throws a NullPointerException when given a null InputStream.
     * <p>
     * This is the expected behavior, as the underlying factory cannot create a data source from a null reference.
     * This test ensures the constructor correctly validates its input.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void constructorWithNullInputStreamShouldThrowNullPointerException() throws IOException {
        // Act: Attempt to create a RandomAccessFileOrArray with a null InputStream.
        new RandomAccessFileOrArray((InputStream) null);

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
        // No further assertions are needed.
    }
}