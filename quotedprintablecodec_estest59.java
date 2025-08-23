package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that repeatedly encoding data with the strict algorithm produces a
     * predictable result where the data size increases with each pass.
     *
     * <p>This regression test ensures that characters introduced by the encoding
     * process (like '=', hex digits, and soft line breaks) are themselves handled
     * correctly in subsequent encoding passes.</p>
     */
    @Test
    public void testRepeatedStrictEncodingIncreasesDataLength() {
        // Arrange: Create a sample byte array containing null bytes and a tab character.
        // Null bytes must be encoded, while the tab is a printable character that has
        // special handling rules in strict mode (e.g., at the end of a line).
        byte[] data = new byte[17];
        data[8] = '\t';

        // Act: Apply strict Quoted-Printable encoding four times in a row.
        // The 'true' flag enables strict mode, which adds soft line breaks for long lines.
        // Using 'null' for the BitSet defaults to the standard set of printable characters.
        final int encodingPasses = 4;
        for (int i = 0; i < encodingPasses; i++) {
            data = QuotedPrintableCodec.encodeQuotedPrintable(null, data, true);
        }

        // Assert: The final length should match the expected value. This specific value
        // is the result of multiple layers of encoding, including the encoding of
        // escape characters ('=') and soft line breaks introduced in previous passes.
        final int expectedLengthAfterFourPasses = 177;
        assertEquals(expectedLengthAfterFourPasses, data.length);
    }
}