package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExtraFieldUtils#parse(byte[])}.
 */
class ExtraFieldUtilsParseTest {

    /**
     * Header-ID for a ZipExtraField not natively supported by Commons Compress, used for testing unrecognized fields.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    private AsiExtraField asiExtraField;
    private byte[] extraFieldData;

    @BeforeEach
    void setUp() throws IOException {
        // Arrange: Create a known extra field (AsiExtraField)
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        // Arrange: Create an unrecognized extra field
        final UnrecognizedExtraField unrecognizedField = new UnrecognizedExtraField();
        unrecognizedField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedField.setLocalFileDataData(new byte[] { 0x00 });

        // Arrange: Concatenate the binary representations of the two fields into a single
        // byte array, simulating how they would appear in a ZIP file's extra field block.
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            bos.write(asiExtraField.getHeaderId().getBytes());
            bos.write(asiExtraField.getLocalFileDataLength().getBytes());
            bos.write(asiExtraField.getLocalFileDataData());

            bos.write(unrecognizedField.getHeaderId().getBytes());
            bos.write(unrecognizedField.getLocalFileDataLength().getBytes());
            bos.write(unrecognizedField.getLocalFileDataData());

            extraFieldData = bos.toByteArray();
        }
    }

    @Test
    @DisplayName("parse should correctly decode a byte array with multiple extra fields")
    void parseShouldCorrectlyDecodeConcatenatedExtraFields() throws ZipException {
        // Act
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(extraFieldData);

        // Assert
        assertEquals(2, parsedFields.length, "Should parse two extra fields");

        // Assert details of the first field (AsiExtraField)
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field should be an AsiExtraField");
        final AsiExtraField parsedAsiField = (AsiExtraField) parsedFields[0];
        assertEquals(0755, parsedAsiField.getMode(), "Mode of AsiExtraField should be preserved");
        assertTrue(parsedAsiField.isDirectory(), "Directory flag of AsiExtraField should be preserved");

        // Assert details of the second field (UnrecognizedExtraField)
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field should be an UnrecognizedExtraField");
        final UnrecognizedExtraField parsedUnrecognizedField = (UnrecognizedExtraField) parsedFields[1];
        assertEquals(UNRECOGNIZED_HEADER, parsedUnrecognizedField.getHeaderId(), "Header ID of unrecognized field should match");
        assertEquals(1, parsedUnrecognizedField.getLocalFileDataLength().getValue(), "Data length of unrecognized field should be 1");
    }

    @Test
    @DisplayName("parse should throw ZipException for truncated extra field data")
    void parseShouldThrowZipExceptionForTruncatedExtraFieldData() {
        // Arrange: Create a truncated version of the extra field data by removing the last byte.
        // This makes the data for the second field incomplete.
        final byte[] truncatedData = new byte[extraFieldData.length - 1];
        System.arraycopy(extraFieldData, 0, truncatedData, 0, truncatedData.length);

        // Act & Assert
        final ZipException e = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(truncatedData),
            "Parsing truncated data should throw ZipException");

        // Assert on the exception message to ensure the error is for the correct reason.
        // This confirms the parser failed at the expected location.
        final int asiFieldTotalLength = 4 + asiExtraField.getLocalFileDataLength().getValue(); // Header(2) + Length(2) + Data
        final String expectedMessage = "Bad extra field starting at " + asiFieldTotalLength
            + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.";
        assertEquals(expectedMessage, e.getMessage());
    }
}