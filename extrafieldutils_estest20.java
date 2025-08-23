package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * This test class contains tests for ExtraFieldUtils.
 *
 * Note: The original test case from which this was refactored was an auto-generated
 * test that incorrectly asserted a NoClassDefFoundError. It has been replaced with a
 * meaningful test that verifies the intended behavior of the mergeCentralDirectoryData method.
 */
public class ExtraFieldUtils_ESTestTest20 {

    /**
     * Tests that mergeCentralDirectoryData correctly concatenates the data
     * from multiple ZipExtraField instances into a single byte array,
     * including each field's header ID and data length.
     */
    @Test
    public void mergeCentralDirectoryDataShouldCorrectlyCombineMultipleFields() {
        // Arrange: Create two distinct extra fields with specific data.
        // We use UnrecognizedExtraField as a simple container for this test.

        // Field 1: Header 0x000A, Data [1, 2]
        final UnrecognizedExtraField field1 = new UnrecognizedExtraField();
        field1.setHeaderId(new ZipShort(0x000A));
        field1.setCentralDirectoryData(new byte[]{1, 2});

        // Field 2: Header 0x000B, Data [3, 4, 5]
        final UnrecognizedExtraField field2 = new UnrecognizedExtraField();
        field2.setHeaderId(new ZipShort(0x000B));
        field2.setCentralDirectoryData(new byte[]{3, 4, 5});

        final ZipExtraField[] fieldsToMerge = {field1, field2};

        // The expected format for each field in the merged data is:
        // Header ID (2 bytes, little-endian) + Data Size (2 bytes, little-endian) + Data
        //
        // Expected for Field 1: 0A 00 (Header) | 02 00 (Size) | 01 02 (Data)
        // Expected for Field 2: 0B 00 (Header) | 03 00 (Size) | 03 04 05 (Data)
        final byte[] expectedMergedData = {
            0x0A, 0x00, // Header for field1
            0x02, 0x00, // Length of central directory data for field1
            1, 2,       // Central directory data for field1
            0x0B, 0x00, // Header for field2
            0x03, 0x00, // Length of central directory data for field2
            3, 4, 5     // Central directory data for field2
        };

        // Act: Merge the central directory data from the fields.
        final byte[] actualMergedData = ExtraFieldUtils.mergeCentralDirectoryData(fieldsToMerge);

        // Assert: The actual merged data should match the expected byte sequence.
        assertArrayEquals(expectedMergedData, actualMergedData);
    }
}