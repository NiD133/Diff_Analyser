package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the CpioUtil class.
 */
public class CpioUtilTest {

    /**
     * Verifies that the long2byteArray method returns a byte array of the exact
     * length specified by the 'length' parameter.
     */
    @Test
    public void long2byteArrayShouldReturnArrayOfSpecifiedLength() {
        // Arrange
        final int expectedLength = 146;
        // The specific numeric value is not relevant for this test, as we are only
        // verifying the properties of the output array, not its content.
        final long arbitraryNumber = -3070L;
        final boolean swapHalfWord = false;

        // Act
        final byte[] resultArray = CpioUtil.long2byteArray(arbitraryNumber, expectedLength, swapHalfWord);

        // Assert
        assertNotNull("The resulting byte array should not be null.", resultArray);
        assertEquals("The length of the byte array should match the requested length.",
                expectedLength, resultArray.length);
    }
}