package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Tests for the {@link RandomAccessFileOrArray} class, focusing on read operations at the end of the data source.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readUnsignedIntLE() when the file pointer is at the end of the data source
     * correctly throws an EOFException.
     *
     * This test ensures that the class properly handles attempts to read past the end of the available data.
     */
    @Test(expected = EOFException.class)
    public void readUnsignedIntLE_whenAtEndOfFile_throwsEOFException() throws IOException {
        // Arrange: Create a data source and position the pointer at its end.
        byte[] sourceData = new byte[5]; // The content doesn't matter, only the length.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        
        // Explicitly move the pointer to the end of the data source.
        fileOrArray.seek(fileOrArray.length());

        // Act: Attempt to read a 4-byte integer. This operation is expected to fail
        // because there are no bytes left to read.
        fileOrArray.readUnsignedIntLE();

        // Assert: The test expects an EOFException, which is verified by the @Test annotation.
    }
}