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
     * Verifies that createExtraField() returns an instance of UnrecognizedExtraField
     * when provided with a header ID that is not registered.
     */
    @Test
    public void createExtraFieldWithUnregisteredIdShouldReturnUnrecognizedExtraField() {
        // Arrange: Define a header ID that is not registered in the default set.
        // 0xDEAD is a common choice for sentinel values and is not a standard ZIP extra field ID.
        final ZipShort unregisteredHeaderId = new ZipShort(0xDEAD);

        // Act: Call the factory method with the unregistered header ID.
        final ZipExtraField createdField = ExtraFieldUtils.createExtraField(unregisteredHeaderId);

        // Assert: The method should fall back to creating an UnrecognizedExtraField,
        // preserving the original header ID.
        assertNotNull("The created field should not be null.", createdField);
        assertTrue("The created field should be an instance of UnrecognizedExtraField.",
                createdField instanceof UnrecognizedExtraField);
        assertEquals("The header ID of the created field should be preserved.",
                unregisteredHeaderId, createdField.getHeaderId());
    }
}