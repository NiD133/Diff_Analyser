package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.util.BitSet;

/**
 * Contains tests for the static encoding methods of {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the encodeQuotedPrintable method correctly uses a custom BitSet
     * to determine which bytes should be encoded. Bytes not in the provided "printable"
     * set should be escaped, while bytes within the set should remain unchanged.
     */
    @Test
    public void encodeWithCustomPrintableSetEncodesOnlyNonPrintableBytes() {
        // Arrange
        // 1. Define a custom set of "printable" characters for this test.
        //    Here, we will only consider the period character '.' as printable.
        final BitSet customPrintableChars = new BitSet();
        customPrintableChars.set('.');

        // 2. Create input containing a mix of bytes:
        //    - 'A': An ASCII character NOT in our custom printable set.
        //    - '.': The single character that IS in our custom printable set.
        //    - 0xF2: A non-ASCII byte, also NOT in our printable set.
        final byte[] inputBytes = {'A', '.', (byte) 0xF2};

        // 3. Define the expected output after encoding.
        //    - 'A' (hex 41) is not printable -> becomes "=41".
        //    - '.' is printable -> remains ".".
        //    - 0xF2 is not printable -> becomes "=F2".
        final byte[] expectedEncodedBytes = new byte[]{'=', '4', '1', '.', '=', 'F', '2'};

        // The 'strict' parameter is false, so no line-wrapping rules are applied.
        final boolean isStrict = false;

        // Act
        final byte[] actualEncodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(
                customPrintableChars, inputBytes, isStrict);

        // Assert
        assertArrayEquals(expectedEncodedBytes, actualEncodedBytes);
    }
}