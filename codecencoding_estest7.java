package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * This test class contains tests for the {@link CodecEncoding} class.
 * This specific test focuses on the properties of canonical codecs.
 */
public class CodecEncodingTest {

    /**
     * Verifies that the canonical codec at index 9 is correctly retrieved and is an
     * unsigned codec.
     *
     * According to the Pack200 specification and the implementation in CodecEncoding,
     * the codec at index 9 is defined as {@code new BHSDCodec(3, 256)}, which
     * defaults to being unsigned.
     */
    @Test
    public void getCanonicalCodec_shouldReturnUnsignedCodecForIndex9() {
        // Arrange
        final int unsignedCodecIndex = 9;

        // Act
        final BHSDCodec codec = CodecEncoding.getCanonicalCodec(unsignedCodecIndex);

        // Assert
        assertNotNull("The codec for a valid index should not be null.", codec);
        assertFalse("The canonical codec at index " + unsignedCodecIndex + " is expected to be unsigned.", codec.isSigned());
    }
}