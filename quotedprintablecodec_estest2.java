package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Contains tests for the static utility methods of the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void encodeWithEmptyPrintableSetShouldEscapeAllBytes() {
        // Arrange
        // Input data: an array of three null bytes.
        final byte[] bytesToEncode = {0, 0, 0};

        // Use an empty BitSet for the set of printable characters. This forces
        // the codec to escape every byte, as none are considered "printable".
        final BitSet emptyPrintableSet = new BitSet();

        // The strict flag enables all encoding rules.
        final boolean useStrictEncoding = true;

        // The null byte (0x00) is encoded as "=00". For three null bytes,
        // the expected output is "=00=00=00". We get the bytes in US-ASCII.
        final byte[] expectedEncoding = "=00=00=00".getBytes(StandardCharsets.US_ASCII);

        // Act
        final byte[] actualEncoding = QuotedPrintableCodec.encodeQuotedPrintable(
                emptyPrintableSet, bytesToEncode, useStrictEncoding);

        // Assert
        assertArrayEquals(expectedEncoding, actualEncoding);
    }
}