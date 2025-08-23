package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the Base16 class.
 * This class name is more conventional than the original "Base16_ESTestTest11".
 */
public class Base16Test {

    /**
     * Tests that the Base16(boolean) constructor correctly configures the encoder
     * to use the upper-case alphabet when the 'lowerCase' parameter is false.
     */
    @Test
    public void whenConstructedWithUpperCaseFlag_thenEncodesToUpperCase() {
        // Arrange: The 'false' argument signifies that the encoder should use the 
        // upper-case alphabet (A-F), as per the constructor's documentation.
        final Base16 base16UpperCase = new Base16(false);
        final byte[] binaryData = {(byte) 0x0A, (byte) 0x1B, (byte) 0x2C, (byte) 0x3D, (byte) 0x4E, (byte) 0x5F};
        final String expectedEncodedString = "0A1B2C3D4E5F";

        // Act: Encode the binary data to a string.
        final String actualEncodedString = base16UpperCase.encodeToString(binaryData);

        // Assert: The output should be in upper-case, matching the expected string.
        assertEquals("Encoding with lowerCase=false should produce an upper-case hex string.",
                expectedEncodedString, actualEncodedString);
    }
}