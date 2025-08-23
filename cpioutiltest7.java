package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

/**
 * This test suite verifies the behavior of the CpioUtil class,
 * focusing on how it handles invalid or edge-case inputs.
 */
public class CpioUtilTest {

    /**
     * Tests that the byteArray2long method correctly throws an
     * ArrayIndexOutOfBoundsException when provided with an empty byte array.
     * The method requires a non-empty array to perform the conversion,
     * so an empty one is an invalid argument.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class, timeout = 4000)
    public void byteArray2long_whenGivenEmptyArray_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Define an empty byte array as input.
        final byte[] emptyByteArray = new byte[0];

        // Act: Call the method with the invalid input.
        // Assert: The test expects an ArrayIndexOutOfBoundsException, as declared
        // in the @Test annotation, because the method cannot access elements
        // in an empty array.
        CpioUtil.byteArray2long(emptyByteArray, false);
    }
}