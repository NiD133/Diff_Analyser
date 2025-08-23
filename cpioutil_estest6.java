package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

/**
 * Unit tests for the CpioUtil class.
 */
public class CpioUtilTest {

    /**
     * Tests that byteArray2long throws a NullPointerException when the input byte array is null.
     * This test ensures the method correctly handles null inputs as a precondition.
     */
    @Test(expected = NullPointerException.class)
    public void byteArray2longShouldThrowNullPointerExceptionForNullInput() {
        // The 'swapHalfWord' parameter value (true or false) is irrelevant for this test,
        // as the null check should happen before it is used.
        final boolean swapHalfWord = true;

        // This call is expected to throw a NullPointerException.
        CpioUtil.byteArray2long(null, swapHalfWord);
    }
}