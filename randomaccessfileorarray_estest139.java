package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
// The original test class name and inheritance are kept for context.
// In a real-world scenario, the class might be renamed to RandomAccessFileOrArrayTest
// and the scaffolding inheritance might be re-evaluated.
public class RandomAccessFileOrArray_ESTestTest139 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readCharLE() throws an EOFException when the underlying data source
     * contains fewer than the two bytes required to read a char.
     */
    @Test(expected = EOFException.class)
    public void readCharLE_withInsufficientData_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only one byte. Reading a char requires two.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act: Attempt to read a little-endian char from the insufficient data source.
        // Assert: An EOFException is expected, as declared by the @Test annotation.
        fileOrArray.readCharLE();
    }
}