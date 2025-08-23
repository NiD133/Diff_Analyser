package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Verifies that getCanonicalCodec(0) returns null.
     * According to the Pack200 specification, the canonical codec at index 0
     * is defined as null, representing the absence of a specific encoding.
     */
    @Test
    public void getCanonicalCodecForIndexZeroShouldReturnNull() {
        // Arrange: The index for the first canonical codec, which is defined as null.
        final int nullCodecIndex = 0;

        // Act: Retrieve the canonical codec for the specified index.
        final BHSDCodec codec = CodecEncoding.getCanonicalCodec(nullCodecIndex);

        // Assert: The returned codec should be null.
        assertNull("The canonical codec for index 0 is expected to be null.", codec);
    }
}