package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Arrays;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExtraFieldUtils#parse(byte[], boolean, ExtraFieldUtils.UnparseableExtraField)}.
 * This class focuses on the SKIP behavior for unparseable extra fields.
 */
class ExtraFieldUtilsParseWithSkipTest implements UnixStat {

    /**
     * Header-ID for a ZipExtraField not natively supported by Commons Compress,
     * used to test the handling of unrecognized fields.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    private AsiExtraField asiField;
    private UnrecognizedExtraField unrecognizedField;
    private byte[] extraFieldData;

    @BeforeEach
    void setUp() {
        // Arrange: Create two distinct extra fields.
        asiField = new AsiExtraField();
        asiField.setMode(0755);
        asiField.setDirectory(true);

        unrecognizedField = new UnrecognizedExtraField();
        unrecognizedField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedField.setLocalFileDataData(new byte[] { 42 });

        // Arrange: Use the utility method to serialize the fields into a byte array.
        // This is the same process a ZIP archiver would use and is much clearer
        // than manual byte manipulation.
        extraFieldData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiField, unrecognizedField });
    }

    @Test
    @DisplayName("parse() with SKIP should correctly parse a stream of well-formed extra fields")
    void parseWithSkipShouldCorrectlyParseWellFormedFields() throws ZipException {
        // Act: Parse the well-formed data.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(extraFieldData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);

        // Assert: Both fields should be parsed successfully.
        assertEquals(2, parsedFields.length, "Should parse two fields from well-formed data");

        // Assert details of the first field (AsiExtraField)
        assertInstanceOf(AsiExtraField.class, parsedFields[0], "First field should be an AsiExtraField");
        final AsiExtraField parsedAsiField = (AsiExtraField) parsedFields[0];
        final int expectedMode = S_IFDIR | 0755; // S_IFDIR (040000) is added for directories.
        assertEquals(expectedMode, parsedAsiField.getMode(), "AsiExtraField mode should be correctly parsed");

        // Assert details of the second field (UnrecognizedExtraField)
        assertInstanceOf(UnrecognizedExtraField.class, parsedFields[1], "Second field should be an UnrecognizedExtraField");
        final UnrecognizedExtraField parsedUnrecognizedField = (UnrecognizedExtraField) parsedFields[1];
        assertEquals(UNRECOGNIZED_HEADER, parsedUnrecognizedField.getHeaderId(), "Header ID of unrecognized field should match");
        assertEquals(1, parsedUnrecognizedField.getLocalFileDataLength().getValue(), "Data length of unrecognized field should be 1");
    }

    @Test
    @DisplayName("parse() with SKIP should skip a trailing field that is truncated")
    void parseWithSkipShouldSkipTruncatedTrailingField() throws ZipException {
        // Arrange: Create truncated data by removing the last byte.
        // This makes the second extra field incomplete and thus "unparseable".
        final byte[] truncatedData = Arrays.copyOf(extraFieldData, extraFieldData.length - 1);

        // Act: Parse the data. The second field is now malformed and should be skipped.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);

        // Assert: Only the first, fully valid field should be returned.
        assertEquals(1, parsedFields.length, "Should parse only the first valid field, skipping the truncated one");

        // Assert the remaining field is the valid AsiExtraField
        assertInstanceOf(AsiExtraField.class, parsedFields[0], "The remaining field should be an AsiExtraField");
        final AsiExtraField parsedAsiField = (AsiExtraField) parsedFields[0];
        final int expectedMode = S_IFDIR | 0755;
        assertEquals(expectedMode, parsedAsiField.getMode(), "AsiExtraField mode should be correctly parsed");
    }
}