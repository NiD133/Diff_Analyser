package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

/**
 * Unit tests for the CpioUtil class.
 */
public class CpioUtilTest {

    /**
     * Tests that byteArray2long throws an ArrayIndexOutOfBoundsException
     * when called with an empty byte array, as it cannot read any data.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void byteArray2longShouldThrowExceptionForEmptyArray() {
        // Arrange: Create an empty byte array
        byte[] emptyArray = new byte[0];

        // Act: Attempt to convert the empty array to a long.
        // Assert: An ArrayIndexOutOfBoundsException is expected, verified by the @Test annotation.
        CpioUtil.byteArray2long(emptyArray, false);
    }
}