package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that fromAscii() returns an empty byte array when the input char array is null.
     * This verifies correct handling of null edge cases.
     */
    @Test
    public void fromAscii_withNullCharArray_shouldReturnEmptyByteArray() {
        // Arrange: No setup needed, the input is null.

        // Act: Call the method under test with a null char array.
        // The explicit cast is necessary to resolve method ambiguity.
        final byte[] result = BinaryCodec.fromAscii((char[]) null);

        // Assert: The result should be an empty byte array.
        assertArrayEquals(new byte[0], result);
    }
}