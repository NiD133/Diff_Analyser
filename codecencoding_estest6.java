package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Tests that {@link CodecEncoding#getCanonicalCodec(int)} returns the correct
     * pre-defined codec for a specific index.
     *
     * <p>The canonical codecs are defined in a static array within the {@code CodecEncoding}
     * class. This test verifies that the codec at a given index has the expected
     * properties. Specifically, for index 40, the codec is expected to be
     * constructed as {@code new BHSDCodec(5, 32, 2, 1)}, which means its 's'
     * (sign) parameter should be 2.</p>
     */
    @Test
    public void getCanonicalCodecShouldReturnCorrectlyConfiguredCodecForIndex40() {
        // Arrange
        final int codecIndex = 40;
        final int expectedSValue = 2; // The expected 's' (sign) parameter for the codec at index 40.

        // Act
        final BHSDCodec codec = CodecEncoding.getCanonicalCodec(codecIndex);

        // Assert
        assertNotNull("The codec for index " + codecIndex + " should not be null.", codec);
        assertEquals("The 's' value of the codec should be correctly configured.",
                     expectedSValue, codec.getS());
    }
}