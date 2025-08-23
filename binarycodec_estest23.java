package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that fromAscii() returns a new, empty byte array when given an empty input array.
     * The Apache Commons Codec implementation often returns a pre-allocated static empty array,
     * so it's important to verify it's not the same instance as the input.
     */
    @Test
    public void fromAscii_withEmptyInput_returnsEmptyArray() {
        // Arrange: Create an empty byte array to use as input.
        byte[] emptyInput = new byte[0];

        // Act: Call the method under test.
        byte[] result = BinaryCodec.fromAscii(emptyInput);

        // Assert: Verify the output is correct.
        assertNotNull("The result should not be null for empty input.", result);
        assertEquals("The result should be an empty byte array.", 0, result.length);
        assertNotSame("The returned array should be a different instance from the input.", emptyInput, result);
    }
}