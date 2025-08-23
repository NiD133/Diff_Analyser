package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that the matches() method throws a NullPointerException
     * when the input byte array (signature) is null.
     */
    @Test(expected = NullPointerException.class)
    public void matchesShouldThrowNullPointerExceptionForNullSignature() {
        // The 'length' parameter can be any integer for this test, as the
        // null check on the byte array happens first.
        final int arbitraryLength = 10;

        // This call is expected to throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        LZMAUtils.matches(null, arbitraryLength);
    }
}