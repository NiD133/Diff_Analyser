package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExtraFieldUtils} merging logic.
 */
public class ExtraFieldUtilsTest {

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    @Test
    void mergeMethodsShouldConcatenateFieldBytes() throws Exception {
        // Arrange
        // 1. A standard, recognized extra field (AsiExtraField for Unix metadata).
        final AsiExtraField asiField = new AsiExtraField();
        asiField.setMode(0755); // rwxr-xr-x
        asiField.setDirectory(true);

        // 2. An UnparseableExtraFieldData instance, which holds raw, unparsed data.
        // This simulates what happens when a parser encounters an unknown field type.
        final UnparseableExtraFieldData unparseableField = new UnparseableExtraFieldData();
        final byte[] rawUnparseableBytes = {
            (byte) 0x55, (byte) 0x55, // Header ID: UNRECOGNIZED_HEADER
            (byte) 0x01, (byte) 0x00  // Length of data payload (1 byte)
        };
        // Initialize the field with raw data that has a declared length but no corresponding payload.
        // The merge methods should still correctly read the header and length.
        unparseableField.parseFromLocalFileData(rawUnparseableBytes, 0, rawUnparseableBytes.length);

        final ZipExtraField[] fieldsToMerge = {asiField, unparseableField};

        // Act
        final byte[] mergedLocalData = ExtraFieldUtils.mergeLocalFileDataData(fieldsToMerge);
        final byte[] mergedCentralData = ExtraFieldUtils.mergeCentralDirectoryData(fieldsToMerge);

        // Assert
        // The merged data should be a concatenation of the full block for each field.
        // A full block consists of: Header ID (2 bytes) + Data Length (2 bytes) + Data Payload.

        // --- Verify Local File Data ---
        final byte[] expectedLocalData = buildExpectedByteArray(
            asiField.getHeaderId(), asiField.getLocalFileDataLength(), asiField.getLocalFileDataData(),
            unparseableField.getHeaderId(), unparseableField.getLocalFileDataLength(), unparseableField.getLocalFileDataData()
        );
        assertArrayEquals(expectedLocalData, mergedLocalData, "Merged local file data should be correct");

        // --- Verify Central Directory Data ---
        final byte[] expectedCentralData = buildExpectedByteArray(
            asiField.getHeaderId(), asiField.getCentralDirectoryLength(), asiField.getCentralDirectoryData(),
            unparseableField.getHeaderId(), unparseableField.getCentralDirectoryLength(), unparseableField.getCentralDirectoryData()
        );
        assertArrayEquals(expectedCentralData, mergedCentralData, "Merged central directory data should be correct");
    }

    /**
     * Helper method to build the expected byte array from extra field components.
     * This improves readability in the test's assertion phase.
     */
    private byte[] buildExpectedByteArray(final ZipShort header1, final ZipShort length1, final byte[] data1,
                                          final ZipShort header2, final ZipShort length2, final byte[] data2) throws Exception {
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            stream.write(header1.getBytes());
            stream.write(length1.getBytes());
            stream.write(data1);
            stream.write(header2.getBytes());
            stream.write(length2.getBytes());
            stream.write(data2);
            return stream.toByteArray();
        }
    }
}