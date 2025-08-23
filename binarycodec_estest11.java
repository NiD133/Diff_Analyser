package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that passing a null byte array to {@code toAsciiChars} results in an
     * empty char array, rather than a NullPointerException.
     */
    @Test
    public void toAsciiChars_shouldReturnEmptyCharArray_whenInputIsNull() {
        // Arrange: No arrangement needed as the input is null.
        final byte[] nullInput = null;

        // Act: Convert the null byte array to ASCII characters.
        final char[] result = BinaryCodec.toAsciiChars(nullInput);

        // Assert: The result should be an empty, non-null char array.
        assertNotNull("The result should be an empty array, not null.", result);
        assertEquals("The length of the char array should be 0 for a null input.", 0, result.length);
    }
}