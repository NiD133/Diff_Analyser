package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that when malformed extra field data is parsed and then merged,
     * the original data is preserved. This ensures that even data that cannot be
     * understood by the library is not lost during processing.
     */
    @Test
    public void mergeLocalFileDataShouldPreserveMalformedExtraFieldData() throws ZipException {
        // Arrange: Define a malformed extra field byte array.
        // The header ID is 0x0000, and the declared data length is 118 bytes.
        // However, the array is only 4 bytes long, making the data incomplete.
        final byte[] malformedExtraFieldData = {0, 0, 118, 0};

        // Use a parsing mode that will capture unparseable data rather than throwing an exception.
        // This mode creates an UnparseableExtraFieldData object for the malformed segment.
        final ExtraFieldParsingBehavior parsingBehavior =
            ZipArchiveEntry.ExtraFieldParsingMode.STRICT_FOR_KNOW_EXTRA_FIELDS;

        // Act:
        // 1. Parse the data. Due to the malformed length, the result should be an array
        //    containing one UnparseableExtraFieldData instance that holds the original bytes.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(malformedExtraFieldData, true, parsingBehavior);

        // 2. Merge the fields back into a byte array.
        final byte[] result = ExtraFieldUtils.mergeLocalFileDataData(parsedFields);

        // Assert: The merging process should reconstruct the original byte array,
        // demonstrating that even unparseable data is preserved correctly.
        assertArrayEquals(malformedExtraFieldData, result);
    }
}