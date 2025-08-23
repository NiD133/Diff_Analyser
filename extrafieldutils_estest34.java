package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.util.zip.ZipException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ExtraFieldUtils#parse(byte[], boolean)} method.
 */
public class ExtraFieldUtilsParseTest {

    /**
     * Tests that the parse method correctly processes a byte array containing exactly
     * one valid extra field followed by trailing data that is too short to constitute
     * another field header. The parser should successfully extract the one field and
     * ignore the trailing bytes.
     */
    @Test
    public void parseShouldIgnoreTrailingDataThatIsTooShortToBeAField() throws ZipException {
        // The extra field data format is a sequence of:
        // - Header ID (2 bytes)
        // - Data Size (2 bytes)
        // - Data      (n bytes)

        // This byte array represents one complete extra field with an empty data section,
        // followed by a single trailing byte. This trailing byte is insufficient to form
        // the 4-byte header of a subsequent field.
        // Field 1: Header ID = 0x0000, Data Size = 0
        // Trailing data: 1 byte (0x2A)
        final byte[] rawData = {0x00, 0x00, 0x00, 0x00, 0x2A};

        // Execute the method under test. The 'local' parameter is false, indicating
        // this data is from a central directory header.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(rawData, false);

        // Verify that the parser identified exactly one extra field.
        assertEquals("Expected one extra field to be parsed from the data.", 1, parsedFields.length);

        // Since Header ID 0x0000 is not a standard registered type, it should be
        // parsed as a generic UnrecognizedExtraField.
        final ZipExtraField field = parsedFields[0];
        assertTrue("The parsed field should be an instance of UnrecognizedExtraField.",
                field instanceof UnrecognizedExtraField);

        // Verify the properties of the parsed field.
        assertEquals("Expected header ID to be 0.", 0, field.getHeaderId().getValue());
        assertEquals("Expected data length to be 0.", 0, field.getLocalFileDataLength().getValue());
    }
}