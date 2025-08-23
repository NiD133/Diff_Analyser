package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * This class contains tests for RandomAccessFileOrArray.
 * Note: The original class name and scaffolding are preserved, but the test
 * has been rewritten for clarity. Unnecessary imports from the original
 * auto-generated test have been removed.
 */
public class RandomAccessFileOrArray_ESTestTest141 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that {@code readChar()} throws an {@link EOFException} when the
     * underlying data source has fewer than the two bytes required to read a char.
     * <p>
     * This test ensures that an attempt to read a char from a one-byte source
     * fails with the correct exception, as expected.
     */
    @Test(expected = EOFException.class, timeout = 4000)
    public void readChar_whenSourceHasInsufficientBytes_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only one byte. A Java char requires two bytes.
        byte[] insufficientBytes = new byte[1];
        
        // Use the recommended factory method to create the source, avoiding the deprecated constructor.
        RandomAccessSource source = new RandomAccessSourceFactory().createSource(insufficientBytes);
        RandomAccessFileOrArray randomAccessData = new RandomAccessFileOrArray(source);

        // Act & Assert: Attempt to read a two-byte char from the one-byte source.
        // The @Test(expected) annotation asserts that an EOFException is thrown.
        randomAccessData.readChar();
    }
}