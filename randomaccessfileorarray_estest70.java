package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a closed RandomAccessFileOrArray instance
     * throws an IllegalStateException.
     */
    @Test
    public void readString_onClosedInstance_throwsIllegalStateException() throws IOException {
        // Arrange: Create an instance backed by a byte array and then close it.
        byte[] sourceData = {0};
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        fileOrArray.close();

        // Act & Assert: Attempting to read from the closed instance should throw.
        try {
            fileOrArray.readString(10, "UTF-8");
            fail("Expected an IllegalStateException because the source is closed, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception with the expected message was caught.
            assertEquals("Already closed", e.getMessage());
        }
    }
}