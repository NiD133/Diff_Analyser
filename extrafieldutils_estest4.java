package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.util.zip.ZipException;

/**
 * Tests for the {@link ExtraFieldUtils.UnparseableExtraField} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that the onUnparseableExtraField method throws an
     * ArrayIndexOutOfBoundsException when the provided offset is outside
     * the bounds of the data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void onUnparseableExtraFieldWithInvalidOffsetShouldThrowException() throws ZipException {
        // Arrange
        // The behavior to test is READ, which attempts to access the data array.
        final ExtraFieldUtils.UnparseableExtraField readBehavior = ExtraFieldUtils.UnparseableExtraField.READ;
        final byte[] data = new byte[0];

        // An offset of 1 is invalid for an empty array.
        final int offsetOutOfBounds = 1;
        final int length = 0;
        final boolean isLocal = true;
        final int claimedLength = 0;

        // Act & Assert
        // This call is expected to throw an ArrayIndexOutOfBoundsException because the
        // offset is greater than the array's length. The assertion is handled by the
        // @Test(expected=...) annotation.
        readBehavior.onUnparseableExtraField(data, offsetOutOfBounds, length, isLocal, claimedLength);
    }
}