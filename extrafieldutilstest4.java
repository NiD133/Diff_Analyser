package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests parsing of a byte array into multiple {@link ZipExtraField} instances
 * for a ZIP central directory.
 *
 * @see ExtraFieldUtils#parse(byte[], boolean)
 */
public class ExtraFieldUtilsParseTest implements UnixStat {

    /**
     * Header-ID for a ZipExtraField not natively supported by Commons Compress,
     * used to test the handling of unrecognized fields.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    private byte[] extraFieldData;

    @BeforeEach
    public void setUp() throws IOException {
        // Arrange: Create two extra fields and serialize them into a single byte array,
        // simulating the raw extra field data from a ZIP file.

        // 1. A standard AsiExtraField for Unix permissions.
        final AsiExtraField asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        // 2. A dummy UnrecognizedExtraField.
        final UnrecognizedExtraField unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 0 }); // 1 byte of data

        // 3. Concatenate the raw data of the two fields into a single byte stream.
        // The format for each field is: [HeaderID (2 bytes)][DataLength (2 bytes)][Data]
        // We use a ByteArrayOutputStream to avoid manual and error-prone byte array copying.
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            bos.write(asiExtraField.getHeaderId().getBytes());
            bos.write(asiExtraField.getLocalFileDataLength().getBytes());
            bos.write(asiExtraField.getLocalFileDataData());

            bos.write(unrecognizedExtraField.getHeaderId().getBytes());
            bos.write(unrecognizedExtraField.getLocalFileDataLength().getBytes());
            bos.write(unrecognizedExtraField.getLocalFileDataData());

            extraFieldData = bos.toByteArray();
        }
    }

    @Test
    void shouldCorrectlyParseMultipleFieldsFromCentralDirectoryData() throws ZipException {
        // Act: Parse the raw byte data, treating it as central directory extra fields (local=false).
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(extraFieldData, false);

        // Assert
        assertEquals(2, parsedFields.length, "Should parse two extra fields from the data");

        // Assert properties of the first field (AsiExtraField)
        final AsiExtraField parsedAsiField = assertInstanceOf(AsiExtraField.class, parsedFields[0],
                "First field should be parsed as AsiExtraField");
        final int expectedMode = S_IFDIR | 0755; // Combine directory flag and permissions
        assertEquals(expectedMode, parsedAsiField.getMode(), "AsiExtraField mode should be correctly parsed");

        // Assert properties of the second field (UnrecognizedExtraField)
        final UnrecognizedExtraField parsedUnrecognizedField = assertInstanceOf(UnrecognizedExtraField.class, parsedFields[1],
                "Second field should be parsed as UnrecognizedExtraField");
        assertEquals(1, parsedUnrecognizedField.getCentralDirectoryLength().getValue(),
                "UnrecognizedExtraField data length should match the parsed length");
    }
}