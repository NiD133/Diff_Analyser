package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExtraFieldUtils#parse(byte[], boolean, ExtraFieldUtils.UnparseableExtraField)}.
 */
@DisplayName("ExtraFieldUtils.parse behavior")
public class ExtraFieldUtilsParseTest implements UnixStat {

    /**
     * Header-ID of a ZipExtraField not natively supported by Commons Compress,
     * used to test parsing of unrecognized fields.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    // Raw extra field data, built in setUp(), containing one AsiExtraField and one UnrecognizedExtraField.
    private byte[] data;

    // The full block size of the first field (AsiExtraField) in the data array.
    private int asiFieldBlockSize;

    @BeforeEach
    void setUp() {
        // Arrange: Create a sequence of extra fields to be serialized into a byte array.

        // 1. A standard, recognized AsiExtraField.
        final AsiExtraField asiField = new AsiExtraField();
        asiField.setMode(0755);
        asiField.setDirectory(true); // This will add the S_IFDIR flag to the mode.
        asiFieldBlockSize = 4 + asiField.getLocalFileDataLength().getValue(); // 4 bytes for header and length

        // 2. A field with a header ID that ExtraFieldUtils does not recognize.
        final UnrecognizedExtraField unrecognizedField = new UnrecognizedExtraField();
        unrecognizedField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedField.setLocalFileDataData(new byte[] { 0 });

        // Use the utility method to create the raw byte data, which is much clearer
        // than manual byte array manipulation with System.arraycopy.
        final ZipExtraField[] fields = { asiField, unrecognizedField };
        data = ExtraFieldUtils.mergeLocalFileDataData(fields);
    }

    @Test
    void parse_shouldCorrectlyReadKnownAndUnrecognizedFields() throws ZipException {
        // Act: Parse the well-formed data. The READ policy dictates how to handle unparseable fields,
        // but it should not be triggered here.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(data, true, ExtraFieldUtils.UnparseableExtraField.READ);

        // Assert: Verify that both fields were parsed correctly.
        assertEquals(2, parsedFields.length, "Should parse two extra fields from the data");

        // Verify the first field (AsiExtraField)
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field should be parsed as AsiExtraField");
        final AsiExtraField parsedAsiField = (AsiExtraField) parsedFields[0];
        assertEquals(S_IFDIR | 0755, parsedAsiField.getMode(), "AsiExtraField mode should include directory flag and permissions");

        // Verify the second field (UnrecognizedExtraField)
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field should be parsed as UnrecognizedExtraField");
        final UnrecognizedExtraField parsedUnrecognizedField = (UnrecognizedExtraField) parsedFields[1];
        assertEquals(UNRECOGNIZED_HEADER, parsedUnrecognizedField.getHeaderId(), "Header ID of unrecognized field should be preserved");
        assertArrayEquals(new byte[] { 0 }, parsedUnrecognizedField.getLocalFileDataData(), "Data of unrecognized field should be preserved");
    }

    @Test
    void parse_shouldWrapTruncatedFieldAsUnparseableData() throws ZipException {
        // Arrange: Create a corrupted byte array by truncating the last byte.
        // This makes the data for the second field incomplete.
        final byte[] truncatedData = Arrays.copyOf(data, data.length - 1);

        // Act: Parse the truncated data with the READ policy for unparseable fields.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.READ);

        // Assert: The first field should parse correctly, while the second, incomplete
        // field should be wrapped in an UnparseableExtraFieldData instance.
        assertEquals(2, parsedFields.length, "Should still identify two fields (one valid, one unparseable)");

        // The first field was complete and should be parsed correctly.
        assertTrue(parsedFields[0] instanceof AsiExtraField, "The complete first field should be parsed correctly");

        // The second field was truncated and should be wrapped.
        assertTrue(parsedFields[1] instanceof UnparseableExtraFieldData, "The truncated second field should be wrapped as unparseable");

        // The wrapped data should contain the raw bytes of the field that failed to parse.
        final UnparseableExtraFieldData unparseableField = (UnparseableExtraFieldData) parsedFields[1];
        final byte[] expectedRawBytes = Arrays.copyOfRange(truncatedData, asiFieldBlockSize, truncatedData.length);

        assertArrayEquals(expectedRawBytes, unparseableField.getLocalFileDataData(), "Unparseable data should contain the remaining bytes of the truncated field");
    }
}