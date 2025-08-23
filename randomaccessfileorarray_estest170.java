package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

/**
 * Tests the behavior of the {@link RandomAccessFileOrArray} class when its
 * underlying data source is null.
 */
// Note: The test class retains its original name and inheritance
// to remain compatible with its test suite.
public class RandomAccessFileOrArray_ESTestTest170 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readLine() throws a NullPointerException when the underlying
     * RandomAccessSource is null.
     * <p>
     * This test specifically covers a scenario where a byte is pushed back before
     * readLine() is called. The first internal read consumes the pushed-back byte,
     * but a subsequent read attempt on the null source triggers the expected exception.
     */
    @Test(expected = NullPointerException.class)
    public void readLineWithNullSourceAfterPushBackShouldThrowNullPointerException() throws Throwable {
        // Arrange: Create a RandomAccessFileOrArray instance with a null source.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);
        
        // Prime the pushback buffer. This ensures the first read inside readLine() succeeds.
        // The character '\r' (byte 13) is used as it's a line-ending character.
        fileOrArray.pushBack((byte) '\r');

        // Act & Assert: Attempting to read a line should cause a NullPointerException
        // when the code tries to read from the null source after consuming the pushed-back byte.
        fileOrArray.readLine();
    }
}