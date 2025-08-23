package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that a custom ZipExtraField implementation can be registered and
     * subsequently instantiated by ExtraFieldUtils.
     */
    @Test
    public void registerShouldAllowCreationOfCustomExtraField() {
        // Arrange
        // UnparseableExtraFieldData is a concrete implementation of ZipExtraField,
        // which is suitable for testing the registration functionality.
        final Class<UnparseableExtraFieldData> customExtraFieldClass = UnparseableExtraFieldData.class;
        final ZipShort expectedHeaderId = new UnparseableExtraFieldData().getHeaderId();

        // Act
        // Register the custom extra field implementation.
        // Note: This method is deprecated, but its functionality is still being tested
        // for backward compatibility.
        ExtraFieldUtils.register(customExtraFieldClass);

        // Assert
        // Verify that createExtraField now returns an instance of our registered class
        // when given the corresponding header ID.
        final ZipExtraField createdField = ExtraFieldUtils.createExtraField(expectedHeaderId);

        assertNotNull("createExtraField should not return null for a registered header ID.", createdField);
        assertTrue("The created field should be an instance of the registered class.",
                createdField instanceof UnparseableExtraFieldData);
        assertEquals("The header ID of the created field should match the expected ID.",
                expectedHeaderId, createdField.getHeaderId());
    }
}