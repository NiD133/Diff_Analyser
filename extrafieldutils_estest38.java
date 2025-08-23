package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that parsing and then merging the central directory data of a known
     * extra field results in the original byte data, verifying a successful round-trip.
     */
    @Test
    public void testParseAndMergeCentralDirectoryDataRoundtrip() throws Exception {
        // Arrange: Create a known extra field and get its raw central directory data.
        final AsiExtraField originalExtraField = new AsiExtraField();
        final byte[] originalData = originalExtraField.getCentralDirectoryData();

        // Act: Parse the raw data into an array of ZipExtraField objects.
        // The 'local' flag is false, indicating this is central directory data.
        final ZipExtraField[] parsedExtraFields = ExtraFieldUtils.parse(
            originalData, false, ExtraFieldUtils.UnparseableExtraField.READ);

        // Merge the parsed fields back into a byte array.
        final byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(parsedExtraFields);

        // Assert: The data after the parse-and-merge round-trip should be
        // identical to the original data.
        assertArrayEquals(originalData, mergedData);
    }
}