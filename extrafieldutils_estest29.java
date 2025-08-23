package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that createExtraFieldNoDefault() creates a new, distinct instance
     * each time it is called for a registered extra field type.
     */
    @Test
    public void createExtraFieldNoDefaultShouldReturnNewInstanceForKnownHeaderId() {
        // Arrange
        // Use the header ID for a known and registered extra field, such as X000A_NTFS.
        final ZipShort knownHeaderId = X000A_NTFS.HEADER_ID;

        // Act
        // Call the factory method twice to create two instances for the same header ID.
        ZipExtraField firstInstance = ExtraFieldUtils.createExtraFieldNoDefault(knownHeaderId);
        ZipExtraField secondInstance = ExtraFieldUtils.createExtraFieldNoDefault(knownHeaderId);

        // Assert
        // Ensure that both created instances are valid and of the expected type.
        assertNotNull("The first created instance should not be null", firstInstance);
        assertTrue("The first instance should be of the correct type X000A_NTFS",
                firstInstance instanceof X000A_NTFS);

        // Crucially, verify that the factory method returned two different objects,
        // confirming it doesn't reuse instances.
        assertNotSame("The factory method should produce a new, distinct instance on each call",
                firstInstance, secondInstance);
    }
}