package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.zip.ZipException;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that parsing an empty byte array of extra field data
     * correctly results in an empty array of ZipExtraField objects.
     */
    @Test
    public void parseWithEmptyByteArrayReturnsEmptyArray() throws ZipException {
        // Arrange: Create an empty byte array to simulate empty extra field data.
        final byte[] emptyExtraFieldData = new byte[0];
        final boolean isLocalHeader = false; // Parsing central directory data
        final ExtraFieldUtils.UnparseableExtraField skipOnUnparseable = ExtraFieldUtils.UnparseableExtraField.SKIP;

        // Act: Parse the empty data.
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(
                emptyExtraFieldData, isLocalHeader, skipOnUnparseable);

        // Assert: The result should be a non-null, empty array.
        assertNotNull("The parsed fields array should not be null.", parsedFields);
        assertEquals("Parsing empty data should yield an empty array of extra fields.", 0, parsedFields.length);
    }
}