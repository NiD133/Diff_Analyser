package org.apache.commons.codec.net;

import org.junit.Test;

/**
 * This refactored test case is part of a larger test suite for URLCodec.
 * The original class name and inheritance structure from the auto-generated test
 * are preserved to demonstrate a focused improvement on a single test method.
 */
public class URLCodec_ESTestTest29 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that attempting to decode a string using a URLCodec instance
     * that was constructed with a null charset results in a NullPointerException.
     *
     * The decode operation requires a charset to convert the decoded bytes
     * back into a String. A null charset causes the underlying `new String(bytes, charset)`
     * constructor to fail, which is the behavior this test verifies.
     */
    @Test(expected = NullPointerException.class)
    public void decodeWithNullCharsetShouldThrowNullPointerException() throws Exception {
        // Arrange: Create a URLCodec instance with a null charset.
        final URLCodec codec = new URLCodec(null);
        final String arbitraryInput = "8"; // The input string's value is irrelevant for this test.

        // Act & Assert: Attempt to decode the string.
        // The test will pass only if a NullPointerException is thrown, as declared
        // in the @Test annotation's 'expected' attribute.
        codec.decode(arbitraryInput);
    }
}