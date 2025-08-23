package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.util.zip.ZipException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ExtraFieldUtils} class, focusing on the parse method.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that parsing an empty byte array, which represents empty extra field data,
     * correctly results in an empty array of ZipExtraField objects. This tests the
     * base case for the parsing logic.
     */
    @Test
    public void parseShouldReturnEmptyArrayForEmptyData() throws ZipException {
        // Arrange: Create an empty byte array to simulate no extra field data.
        byte[] emptyExtraFieldData = new byte[0];

        // Act: Call the parse method with the empty data.
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(emptyExtraFieldData);

        // Assert: The result should be a non-null, empty array.
        assertNotNull("The parsed fields array should not be null.", parsedFields);
        assertEquals("Parsing empty data should yield an empty array of fields.", 0, parsedFields.length);
    }
}