package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link ExtraFieldUtils}.
 * This class focuses on a specific test case from a larger generated suite.
 */
public class ExtraFieldUtils_ESTestTest18 extends ExtraFieldUtils_ESTest_scaffolding {

    /**
     * Tests that {@link ExtraFieldUtils#mergeLocalFileDataData(ZipExtraField[])}
     * throws an IllegalArgumentException when a Zip64ExtendedInformationExtraField
     * is incomplete for a local file header.
     * <p>
     * For local file data, a Zip64 field must contain both the original size and
     * the compressed size. This test verifies that providing a field with only
     * the size set triggers the appropriate validation exception.
     */
    @Test
    public void mergeLocalFileDataDataThrowsExceptionForIncompleteZip64Field() {
        // Arrange: Create a Zip64 extra field that is invalid for a local file header
        // because it's missing the compressed size.
        final Zip64ExtendedInformationExtraField incompleteZip64Field = new Zip64ExtendedInformationExtraField();
        incompleteZip64Field.setSize(ZipEightByteInteger.ZERO);
        // Note: The compressedSize field is deliberately left null.

        final ZipExtraField[] extraFields = { incompleteZip64Field };

        // Act & Assert
        try {
            ExtraFieldUtils.mergeLocalFileDataData(extraFields);
            fail("Expected an IllegalArgumentException because the Zip64 field is incomplete.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is the one expected from the validation logic.
            final String expectedMessage = "Zip64 extended information must contain both size values in the local file header.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}