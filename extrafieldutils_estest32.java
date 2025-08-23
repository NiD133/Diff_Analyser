package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

/**
 * This class contains improved, more understandable tests for {@link ExtraFieldUtils}.
 * The original test was auto-generated and has been refactored for clarity and maintainability.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that calling {@code fillExtraField} with an offset and length
     * that are out of bounds for the given byte array throws an
     * {@code IndexOutOfBoundsException}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void fillExtraFieldWithOutOfBoundsOffsetAndLengthShouldThrowException() throws ZipException {
        // Arrange
        // A concrete ZipExtraField instance to be filled. The specific type is not critical for this test.
        final ZipExtraField fieldToFill = new Zip64ExtendedInformationExtraField();
        
        // Use an empty byte array as the source data. This makes any positive
        // offset and length combination invalid and clearly demonstrates the boundary condition.
        final byte[] emptyData = new byte[0];
        
        // Define an offset and length that are clearly out of bounds for the empty array.
        final int invalidOffset = 100;
        final int invalidLength = 100;
        final boolean isLocalHeader = true;

        // Act & Assert
        // This call is expected to fail with an IndexOutOfBoundsException
        // because the provided offset and length point outside the emptyData array.
        // The assertion is handled by the @Test(expected=...) annotation.
        ExtraFieldUtils.fillExtraField(fieldToFill, emptyData, invalidOffset, invalidLength, isLocalHeader);
    }
}