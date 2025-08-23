package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readString() throws an EOFException when attempting to read
     * more bytes than are available in the underlying data source.
     */
    @Test(expected = EOFException.class)
    public void readString_whenLengthExceedsAvailableData_throwsEOFException() throws Exception {
        // Arrange: Create a data source with a known, small size.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Act: Attempt to read more bytes than exist in the source data.
        int bytesToRead = sourceData.length + 1;
        fileOrArray.readString(bytesToRead, "UTF-8");

        // Assert: The test expects an EOFException to be thrown.
        // The @Test(expected) annotation handles the assertion.
    }
}