package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the ExtraFieldUtils class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that fillExtraField throws a NegativeArraySizeException when a negative length is provided.
     * This exception is expected because the underlying ZipExtraField implementation (in this case,
     * Zip64ExtendedInformationExtraField) will fail when trying to process a buffer with a negative size.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void fillExtraFieldShouldThrowNegativeArraySizeExceptionForNegativeLength() throws ZipException {
        // Arrange: Create an extra field instance and define invalid (negative) parameters.
        // The specific field type is important as its parse method will throw the exception.
        final ZipExtraField extraField = new Zip64ExtendedInformationExtraField(null, null);
        final byte[] data = null; // The data buffer is not accessed before the length check fails.
        final int offset = 0;
        final int negativeLength = -1;
        final boolean isForCentralDirectory = false;

        // Act: Attempt to fill the extra field with a negative data length.
        // This call is expected to throw a NegativeArraySizeException from within the extraField's parsing logic.
        ExtraFieldUtils.fillExtraField(extraField, data, offset, negativeLength, isForCentralDirectory);

        // Assert: The test passes if the expected NegativeArraySizeException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}