package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link ByteUtils} class.
 * This snippet focuses on the behavior of the toLittleEndian method with a DataOutput.
 */
// The original test class structure is maintained for context.
public class ByteUtils_ESTestTest3 extends ByteUtils_ESTest_scaffolding {

    /**
     * Tests that calling the deprecated toLittleEndian(DataOutput, ...) method with a negative length
     * correctly does nothing and writes no bytes to the output stream.
     */
    @Test(timeout = 4000)
    public void toLittleEndianWithDataOutputShouldWriteNoBytesForNegativeLength() throws IOException {
        // Arrange: Set up an output stream to capture the written bytes.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        long anyValue = -1257L; // The specific value doesn't matter for this test.
        int negativeLength = -2038;

        // Act: Call the method under test with a negative length.
        // This method is deprecated, but its behavior for this edge case is being verified.
        ByteUtils.toLittleEndian(dataOutput, anyValue, negativeLength);

        // Assert: Verify that no bytes were written to the stream.
        assertEquals("No bytes should be written when the specified length is negative.",
                     0, outputStream.size());
    }
}