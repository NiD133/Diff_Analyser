package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the static utility methods of the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that {@link BinaryCodec#fromAscii(char[])} correctly handles an empty
     * character array by returning an empty byte array.
     */
    @Test
    public void fromAscii_withEmptyCharArray_returnsEmptyByteArray() {
        // Arrange: Create an empty character array as input.
        final char[] emptyAsciiChars = new char[0];

        // Act: Call the method under test.
        final byte[] result = BinaryCodec.fromAscii(emptyAsciiChars);

        // Assert: Verify that the result is an empty, non-null byte array.
        assertNotNull("The result should not be null.", result);
        assertEquals("The resulting byte array should be empty.", 0, result.length);
    }
}