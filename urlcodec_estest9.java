package org.apache.commons.codec.net;

import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for the static methods in {@link URLCodec}.
 */
public class URLCodecStaticTest {

    /**
     * Tests that encoding an empty byte array returns a new, empty byte array instance,
     * rather than returning the original array reference.
     */
    @Test
    public void encodeUrlWithEmptyByteArrayShouldReturnNewEmptyArray() {
        // Arrange
        final byte[] emptyBytes = new byte[0];
        // Use an empty BitSet, meaning no characters are considered "safe" for encoding.
        final BitSet safeCharacters = new BitSet();

        // Act
        final byte[] encodedBytes = URLCodec.encodeUrl(safeCharacters, emptyBytes);

        // Assert
        // The result should be a new, distinct array instance.
        assertNotSame("Encoding an empty array should produce a new array instance.", emptyBytes, encodedBytes);
        
        // The new array should also be empty.
        assertArrayEquals("Encoding an empty array should result in an empty array.", new byte[0], encodedBytes);
    }
}