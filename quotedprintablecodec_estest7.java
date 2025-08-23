package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.BitSet;

/**
 * Tests for the static methods of {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecStaticTest {

    /**
     * Tests that encoding an empty byte array results in an empty byte array.
     */
    @Test
    public void testEncodeQuotedPrintableWithEmptyByteArrayShouldReturnEmptyArray() {
        // Arrange
        final byte[] emptyByteArray = new byte[0];
        // The set of printable characters is irrelevant for an empty input,
        // but an empty BitSet is a valid parameter.
        final BitSet printableChars = new BitSet();

        // Act
        final byte[] result = QuotedPrintableCodec.encodeQuotedPrintable(printableChars, emptyByteArray, false);

        // Assert
        assertNotNull("The result of encoding should not be null", result);
        assertArrayEquals("Encoding an empty byte array should produce an empty byte array", emptyByteArray, result);
    }
}