package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the merge methods in {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsTest implements UnixStat {

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField unrecognizedExtraField;

    @BeforeEach
    void setUp() {
        // Arrange: Create a known extra field (AsiExtraField)
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        // Arrange: Create an unrecognized extra field
        unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 1, 2, 3 });
        unrecognizedExtraField.setCentralDirectoryData(new byte[] { 4, 5, 6 });
    }

    @Test
    @DisplayName("mergeLocalFileDataData should serialize and concatenate extra fields correctly")
    void mergeLocalFileDataDataShouldCombineFieldsInCorrectOrder() throws IOException {
        // Arrange
        final ZipExtraField[] fields = { asiExtraField, unrecognizedExtraField };

        // Build the expected byte array in a readable, self-documenting way.
        // The format for each field is: [Header ID][Data Length][Data]
        final byte[] expectedData = buildExpectedData(fields, true);

        // Act
        final byte[] actualData = ExtraFieldUtils.mergeLocalFileDataData(fields);

        // Assert
        assertArrayEquals(expectedData, actualData, "Merged local file data should match the expected byte sequence.");
    }

    @Test
    @DisplayName("mergeCentralDirectoryData should serialize and concatenate extra fields correctly")
    void mergeCentralDirectoryDataShouldCombineFieldsInCorrectOrder() throws IOException {
        // Arrange
        final ZipExtraField[] fields = { asiExtraField, unrecognizedExtraField };

        // Build the expected byte array for the central directory.
        // This verifies that the central-directory-specific methods are called.
        final byte[] expectedData = buildExpectedData(fields, false);

        // Act
        final byte[] actualData = ExtraFieldUtils.mergeCentralDirectoryData(fields);

        // Assert
        assertArrayEquals(expectedData, actualData, "Merged central directory data should match the expected byte sequence.");
    }

    /**
     * Helper method to construct the expected byte array for a list of extra fields.
     * This makes the test's "Arrange" phase clearer and less error-prone than manual array copying.
     *
     * @param fields The extra fields to serialize.
     * @param isLocalData True to use local file data methods, false for central directory data methods.
     * @return The concatenated byte array.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] buildExpectedData(final ZipExtraField[] fields, final boolean isLocalData) throws IOException {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (final ZipExtraField field : fields) {
                bos.write(field.getHeaderId().getBytes());
                if (isLocalData) {
                    bos.write(field.getLocalFileDataLength().getBytes());
                    bos.write(field.getLocalFileDataData());
                } else {
                    bos.write(field.getCentralDirectoryLength().getBytes());
                    bos.write(field.getCentralDirectoryData());
                }
            }
            return bos.toByteArray();
        }
    }
}