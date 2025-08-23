package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link MergedStream} class, focusing on handling invalid arguments.
 */
public class MergedStreamTest {

    /**
     * Verifies that calling read(byte[], int, int) with a negative offset and length
     * throws an ArrayIndexOutOfBoundsException, as required by the InputStream contract.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readWithNegativeOffsetAndLengthShouldThrowException() throws IOException {
        // Arrange: Create a MergedStream instance.
        // A dummy input stream is required for the constructor, but its content is not used in this test.
        InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[12];

        // The MergedStream is constructed with negative start/end indices.
        // The constructor may allow this, but the subsequent read call is expected to fail
        // due to its own argument validation.
        int invalidIndex = -10;
        MergedStream mergedStream = new MergedStream(null, dummyInputStream, buffer, invalidIndex, invalidIndex);

        // Act & Assert: Attempt to read from the stream using a negative offset and length.
        // The @Test(expected=...) annotation asserts that this call must throw
        // an ArrayIndexOutOfBoundsException. If it doesn't, the test fails.
        mergedStream.read(buffer, invalidIndex, invalidIndex);
    }
}