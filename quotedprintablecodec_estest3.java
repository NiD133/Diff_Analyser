package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link QuotedPrintableCodec} class, focusing on decoding behavior.
 * This class provides an improved version of a previously generated test case.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that data encoded with the 'strict' flag, which adds soft line breaks
     * for line length compliance, can be successfully decoded.
     *
     * <p>This test simulates a scenario where already encoded data is re-encoded,
     * this time using the strict mode which introduces line breaks. The main goal is
     * to verify that the decoder correctly handles this strictly-encoded format,
     * including the soft line breaks.</p>
     */
    @Test
    public void decodeShouldCorrectlyProcessStrictlyEncodedDataWithSoftLineBreaks() throws DecoderException {
        // --- Arrange ---

        // 1. Start with an array of 18 null bytes. In Java, a new byte array is
        //    automatically initialized with zeros.
        final byte[] originalBytes = new byte[18];

        // 2. First, perform a non-strict encoding. Each null byte ('\0') becomes "=00".
        //    This creates an intermediate result of 54 bytes (18 * 3).
        final byte[] nonStrictEncodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, originalBytes, false);
        assertEquals("Length after non-strict encoding should be 3x original", 54, nonStrictEncodedBytes.length);

        // 3. Second, re-encode the intermediate result using the strict mode.
        //    The '=' character from "=00" will be encoded to "=3D", and a soft line
        //    break ("=\r\n") will be added to adhere to the line length limit.
        //    The input "=00" becomes encoded as "=3D00".
        //    Expected length = (18 * 5 for each "=3D00") + 3 for one soft line break = 93 bytes.
        final byte[] strictEncodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, nonStrictEncodedBytes, true);
        assertEquals("Length after strict encoding should include encoded characters and a line break", 93, strictEncodedBytes.length);

        // --- Act ---

        // Decode the strictly-encoded data. This should reverse only the second (strict) encoding step.
        final byte[] decodedBytes = QuotedPrintableCodec.decodeQuotedPrintable(strictEncodedBytes);

        // --- Assert ---

        // The decoded result must be identical to the intermediate data (before strict encoding).
        // This confirms the decoder correctly processed the strictly encoded format.
        // The original `assertNotNull` is redundant, as `assertArrayEquals` provides a stronger check.
        assertArrayEquals("Decoded bytes should match the intermediate non-strictly encoded bytes",
                nonStrictEncodedBytes, decodedBytes);
    }
}