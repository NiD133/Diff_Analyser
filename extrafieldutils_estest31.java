package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that createExtraField() returns a default UnrecognizedExtraField instance
     * when provided with a header ID that does not correspond to any known extra field type.
     */
    @Test
    public void createExtraFieldShouldReturnUnrecognizedExtraFieldForUnknownHeaderId() {
        // Arrange: Define a header ID that is not registered in the ExtraFieldUtils map.
        // The value 0xDEAD is an arbitrary choice representing an unknown type.
        final ZipShort unknownHeaderId = new ZipShort(0xDEAD);

        // Act: Attempt to create an extra field with the unknown header ID.
        final ZipExtraField createdField = ExtraFieldUtils.createExtraField(unknownHeaderId);

        // Assert: The method should fall back to creating an UnrecognizedExtraField
        // instead of returning null or throwing an exception.
        assertNotNull("The created field should not be null.", createdField);
        assertTrue("Expected an instance of UnrecognizedExtraField for an unknown header ID.",
                   createdField instanceof UnrecognizedExtraField);
        assertEquals("The header ID of the created field should match the input.",
                     unknownHeaderId, createdField.getHeaderId());
    }
}