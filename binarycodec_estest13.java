package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that toByteArray(String) returns an empty byte array when the input is null.
     * This is a deprecated method, but its contract should be maintained.
     */
    @Test
    public void toByteArray_withNullInput_shouldReturnEmptyArray() {
        // Arrange
        BinaryCodec codec = new BinaryCodec();
        byte[] expectedEmptyArray = new byte[0];

        // Act
        byte[] result = codec.toByteArray(null);

        // Assert
        assertNotNull("The result of toByteArray(null) should be an empty array, not null.", result);
        assertArrayEquals("An empty byte array should be returned for null input.", expectedEmptyArray, result);
    }
}