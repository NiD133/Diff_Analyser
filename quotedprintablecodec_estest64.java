package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link QuotedPrintableCodec}.
 * This particular test focuses on the round-trip encoding and decoding
 * of binary data using the strict encoding mode.
 */
public class QuotedPrintableCodec_ESTestTest64 extends QuotedPrintableCodec_ESTest_scaffolding {

    /**
     * Tests that encoding and then decoding an array of null bytes using the strict
     * Quoted-Printable algorithm restores the original data perfectly.
     *
     * <p>This test verifies two key behaviors:</p>
     * <ol>
     *   <li>The strict encoder correctly applies soft line breaks ('=CRLF') to adhere to
     *       the 76-character line length limit specified in RFC 1521.</li>
     *   <li>The decode operation correctly processes the encoded data, including line breaks,
     *       to produce a byte array identical to the original input.</li>
     * </ol>
     *
     * @throws DecoderException if the decoding process fails.
     */
    @Test(timeout = 4000)
    public void strictEncodeDecodeRoundTripWithNullBytesShouldRestoreOriginalData() throws DecoderException {
        // Arrange: Create an array of 27 null bytes. Null bytes are non-printable
        // and must be escaped in Quoted-Printable encoding (as "=00").
        final byte[] originalData = new byte[27];

        // Act: Encode the data using the strict algorithm (which adds line breaks),
        // and then decode it back. The 'BitSet' parameter is null to use the default
        // set of printable characters.
        final byte[] encodedData = QuotedPrintableCodec.encodeQuotedPrintable(null, originalData, true);
        final byte[] decodedData = QuotedPrintableCodec.decodeQuotedPrintable(encodedData);

        // Assert

        // 1. Verify the length of the encoded data. This confirms that the strict
        //    encoding's line-breaking logic is working as expected.
        //
        // Calculation:
        // - Each null byte ('\0') is encoded as "=00" (3 bytes).
        // - The encoder fits 24 of these (72 chars) on the first line before adding a
        //   soft line break ("=\r\n", 3 bytes).
        // - Line 1 length: (24 bytes * 3) + 3 = 75 bytes.
        // - The remaining 3 null bytes are encoded on the next line: 3 * 3 = 9 bytes.
        // - Total expected length = 75 + 9 = 84 bytes.
        final int expectedEncodedLength = 84;
        assertEquals("Encoded data length should be correct due to soft line breaks",
                expectedEncodedLength, encodedData.length);

        // 2. Verify that the decoded data is identical to the original data.
        // This is the primary goal of a round-trip test.
        assertArrayEquals("Decoded data should match the original data", originalData, decodedData);
    }
}