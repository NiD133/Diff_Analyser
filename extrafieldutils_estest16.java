package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for parsing ZIP extra fields in {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsParseTest {

    /**
     * Tests that ExtraFieldUtils.parse() throws a ZipException when it encounters
     * an extra field block where the declared data length exceeds the number of
     * bytes actually available in the buffer.
     */
    @Test
    public void parseShouldThrowExceptionWhenDataLengthExceedsAvailableBytes() {
        // Arrange: Create a malformed extra field byte array.
        // The format of an extra field block is:
        // Header ID (2 bytes) + Data Size (2 bytes) + Data
        // We will declare a large data size but provide much less data.

        final int declaredDataLength = 58250;
        final int actualDataLength = 10;
        final int headerAndLengthSize = 4; // 2 bytes for Header ID + 2 bytes for Data Size

        // Use a ByteBuffer to construct the byte array in a structured and readable way.
        // ZIP format specifies little-endian byte order.
        ByteBuffer buffer = ByteBuffer.allocate(headerAndLengthSize + actualDataLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // 1. Header ID (using a known ID for clarity)
        buffer.putShort(AsiExtraField.HEADER_ID.getValue());
        // 2. A declared data length that is impossibly large for the buffer.
        buffer.putShort((short) declaredDataLength);
        // 3. The actual data, which is much shorter than what was declared.
        buffer.put(new byte[actualDataLength]);

        byte[] malformedExtraFieldData = buffer.array();

        // Act & Assert
        try {
            ExtraFieldUtils.parse(malformedExtraFieldData);
            fail("Expected a ZipException because the declared length exceeds available data.");
        } catch (ZipException e) {
            // Verify that the exception message clearly states the problem,
            // confirming the parse failed for the correct reason.
            String expectedMessage = "Bad extra field starting at 0.  Block length of "
                + declaredDataLength + " bytes exceeds remaining data of "
                + actualDataLength + " bytes.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}