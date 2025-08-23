package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

// The test class name is kept for context, but in a real scenario,
// it would be renamed to something like ExtraFieldUtilsTest.
public class ExtraFieldUtils_ESTestTest11 extends ExtraFieldUtils_ESTest_scaffolding {

    /**
     * Tests that ExtraFieldUtils.parse() throws a ZipException when the declared
     * length of an extra field block is greater than the actual remaining data
     * in the byte array.
     */
    @Test
    public void parseShouldThrowZipExceptionWhenDeclaredLengthExceedsAvailableData() {
        // Arrange: Create a malformed extra field byte array.
        // The extra field format is: [Header ID (2 bytes)] [Data Size (2 bytes)] [Data...].
        // We will declare a data size of 10 bytes but provide only 6 bytes of actual data.
        // The total array length is 10 bytes (2 for header + 2 for size + 6 for data).
        final byte[] malformedExtraFieldData = {
            // Header ID: 0x0001
            0x01, 0x00,
            // Declared Data Size: 10 bytes (which is more than the available 6 bytes)
            0x0A, 0x00,
            // Actual Data: Only 6 bytes are provided here.
            (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF, 0x01, 0x02
        };

        // Act & Assert
        // We expect a ZipException because the declared length (10) exceeds the
        // available data length (6) in the buffer.
        ZipException exception = assertThrows(ZipException.class, () ->
            ExtraFieldUtils.parse(malformedExtraFieldData, false, ExtraFieldUtils.UnparseableExtraField.THROW)
        );

        // Verify the exception message clearly states the reason for the failure.
        String expectedMessageFragment = "exceeds remaining data";
        assertTrue(
            "Exception message should indicate that the declared block length is too large.",
            exception.getMessage().contains(expectedMessageFragment)
        );
    }
}