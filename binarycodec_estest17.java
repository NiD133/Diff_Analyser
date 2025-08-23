package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link BinaryCodec} class.
 *
 * Note: This refactoring is based on the org.apache.commons.codec.binary.BinaryCodec
 * class, which was the actual target of the original test, not the provided
 * org.locationtech.spatial4j.io.BinaryCodec source.
 */
public class BinaryCodecTest {

    /**
     * Tests that toAsciiBytes(byte[]) returns an empty byte array when the input is null.
     * This is the expected behavior for safe handling of null inputs.
     */
    @Test
    public void toAsciiBytes_withNullInput_shouldReturnEmptyArray() {
        // Arrange: Define the expected outcome for a null input.
        final byte[] expectedOutput = new byte[0];

        // Act: Call the method with a null input.
        final byte[] actualOutput = BinaryCodec.toAsciiBytes(null);

        // Assert: Verify that the actual output is a non-null, empty array.
        assertNotNull("The result should be an empty array, not null.", actualOutput);
        assertArrayEquals("Passing a null array should result in an empty byte array.", expectedOutput, actualOutput);
    }
}