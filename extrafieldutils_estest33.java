package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that merging an empty array of extra fields for the central directory
     * results in an empty byte array.
     */
    @Test
    public void mergeCentralDirectoryDataWithEmptyFieldsReturnsEmptyByteArray() {
        // Arrange: Create an empty array of extra fields.
        final ZipExtraField[] emptyExtraFields = new ZipExtraField[0];

        // Act: Call the method under test.
        final byte[] result = ExtraFieldUtils.mergeCentralDirectoryData(emptyExtraFields);

        // Assert: The resulting byte array should be empty.
        assertEquals("Merging zero fields should produce an empty byte array.", 0, result.length);
    }
}