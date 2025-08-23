package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Tests for {@link Base16}.
 */
public class Base16Test {

    @Test
    public void testDecodeValidHexString() {
        // Arrange: Set up the test data and components.
        final Base16 base16 = new Base16();
        final String hexString = "21";
        // The hexadecimal string "21" represents the byte with the value 0x21 (decimal 33).
        final byte[] expectedDecodedBytes = { (byte) 0x21 };

        // Act: Execute the method under test.
        final byte[] actualDecodedBytes = base16.decode(hexString);

        // Assert: Verify the result is as expected.
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes);
    }
}