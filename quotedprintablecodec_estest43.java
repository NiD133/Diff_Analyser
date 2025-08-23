package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertArrayEquals;

/**
 * This test suite contains improved, human-readable tests for the QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the strict encoding mode correctly escapes a trailing space character.
     * According to RFC 1521, in strict Quoted-Printable encoding, space or tab characters
     * at the end of a line must be encoded.
     */
    @Test
    public void encodeQuotedPrintableWithStrictTrueShouldEncodeTrailingSpace() {
        // Arrange
        // Input data containing four null bytes followed by a space.
        final byte[] originalData = {0, 0, 0, 0, ' '};

        // In Quoted-Printable, non-printable characters like null (0x00) are
        // encoded as "=00". In strict mode, a trailing space (0x20) must also
        // be encoded, becoming "=20".
        final byte[] expectedEncodedData = "=00=00=00=00=20".getBytes(StandardCharsets.US_ASCII);

        // Act
        // Encode the data with the 'strict' flag enabled.
        // Passing a null BitSet uses the default set of printable characters.
        final byte[] actualEncodedData = QuotedPrintableCodec.encodeQuotedPrintable(null, originalData, true);

        // Assert
        // Verify that the actual encoded content matches the expected result.
        assertArrayEquals(expectedEncodedData, actualEncodedData);
    }
}