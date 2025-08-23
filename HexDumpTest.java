package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.Test;

/**
 * Readable, maintainable tests for HexDump.
 *
 * The original tests contained a lot of duplicated logic and large inline literals.
 * This version:
 * - Uses small helpers to build input data and expected output.
 * - Compares human-readable strings instead of hand-crafted byte arrays.
 * - Splits scenarios into focused test methods with descriptive names.
 */
class HexDumpTest {

    private static final int BYTES_PER_LINE = 16;
    private static final String LS = System.lineSeparator();

    // ------------------------
    // Appendable-based methods
    // ------------------------

    @Test
    void testDumpAppendable_fullArray() throws IOException {
        final byte[] data = sequentialBytes(256);

        final StringBuilder actual = new StringBuilder();
        HexDump.dump(data, actual);

        final String expected = expectedHexDump(data, /*offset*/ 0, /*index*/ 0, /*length*/ data.length);
        assertEquals(expected, actual.toString());
    }

    @Test
    void testDumpAppendable_withNonZeroOffsetIndexAndLength() throws IOException {
        final byte[] data = sequentialBytes(256);

        final StringBuilder actual = new StringBuilder();
        HexDump.dump(data, 0x1000_0000L, actual, /*index*/ 0x28, /*length*/ 32);

        final String expected = expectedHexDump(data, /*offset*/ 0x1000_0000L, /*index*/ 0x28, /*length*/ 32);
        assertEquals(expected, actual.toString());
    }

    @Test
    void testDumpAppendable_withNonZeroIndexAndShorterLength() throws IOException {
        final byte[] data = sequentialBytes(256);

        final StringBuilder actual = new StringBuilder();
        HexDump.dump(data, /*offset*/ 0, actual, /*index*/ 0x40, /*length*/ 24);

        final String expected = expectedHexDump(data, /*offset*/ 0, /*index*/ 0x40, /*length*/ 24);
        assertEquals(expected, actual.toString());
    }

    @Test
    void testDumpAppendable_invalidArguments() {
        final byte[] data = sequentialBytes(256);

        // Negative index
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0x1000_0000L, new StringBuilder(), -1, data.length));

        // Index too large
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0x1000_0000L, new StringBuilder(), data.length, data.length));

        // Negative length
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0, new StringBuilder(), 0, -1));

        // Length too large (also assert message)
        final Exception ex = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0, new StringBuilder(), 1, data.length));
        assertEquals("Range [1, 1 + 256) out of bounds for length 256", ex.getMessage());

        // Null appendable
        assertThrows(NullPointerException.class,
                () -> HexDump.dump(data, 0x1000_0000L, null, 0, data.length));
    }

    // ------------------------
    // OutputStream-based methods
    // ------------------------

    @Test
    void testDumpOutputStream_fullArray() throws IOException {
        final byte[] data = sequentialBytes(256);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(data, /*offset*/ 0, out, /*index*/ 0);

        final String expected = expectedHexDump(data, /*offset*/ 0, /*index*/ 0, /*length*/ data.length);
        final String actual = new String(out.toByteArray(), Charset.defaultCharset());
        assertEquals(expected, actual);
    }

    @Test
    void testDumpOutputStream_withNonZeroOffset() throws IOException {
        final byte[] data = sequentialBytes(256);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(data, /*offset*/ 0x1000_0000L, out, /*index*/ 0);

        final String expected = expectedHexDump(data, /*offset*/ 0x1000_0000L, /*index*/ 0, /*length*/ data.length);
        final String actual = new String(out.toByteArray(), Charset.defaultCharset());
        assertEquals(expected, actual);
    }

    @Test
    void testDumpOutputStream_with0xFF000000Offset() throws IOException {
        final byte[] data = sequentialBytes(256);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(data, /*offset*/ 0xFF00_0000L, out, /*index*/ 0);

        final String expected = expectedHexDump(data, /*offset*/ 0xFF00_0000L, /*index*/ 0, /*length*/ data.length);
        final String actual = new String(out.toByteArray(), Charset.defaultCharset());
        assertEquals(expected, actual);
    }

    @Test
    void testDumpOutputStream_withNonZeroIndex() throws IOException {
        final byte[] data = sequentialBytes(256);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(data, /*offset*/ 0x1000_0000L, out, /*index*/ 0x81);

        final int length = data.length - 0x81; // OutputStream variant dumps to end of array
        final String expected = expectedHexDump(data, /*offset*/ 0x1000_0000L, /*index*/ 0x81, /*length*/ length);
        final String actual = new String(out.toByteArray(), Charset.defaultCharset());
        assertEquals(expected, actual);
    }

    @Test
    void testDumpOutputStream_invalidArgumentsAndDoesNotClose() throws IOException {
        final byte[] data = sequentialBytes(256);

        // Negative index
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0x1000_0000L, new ByteArrayOutputStream(), -1));

        // Index too large
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> HexDump.dump(data, 0x1000_0000L, new ByteArrayOutputStream(), data.length));

        // Null stream
        assertThrows(NullPointerException.class,
                () -> HexDump.dump(data, 0x1000_0000L, null, 0));

        // Stream must not be closed by dump()
        HexDump.dump(data, 0, new ThrowOnCloseOutputStream(new ByteArrayOutputStream()), 0);
    }

    // ------------------------
    // Helpers
    // ------------------------

    /**
     * Creates an array with values [0, 1, 2, ..., size-1] wrapped as bytes.
     */
    private static byte[] sequentialBytes(final int size) {
        final byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) i;
        }
        return data;
    }

    /**
     * Builds the expected hex dump string for the given arguments.
     *
     * Format per line:
     * - 8 uppercase hex digits of (offset + lineStartIndex), followed by a space
     * - 16 groups of "HH " for available bytes, or "   " for missing bytes past length
     * - one space
     * - ASCII view for available bytes (printable 32..126), '.' otherwise
     * - system line separator
     */
    private static String expectedHexDump(final byte[] data, final long offset, final int index, final int length) {
        final StringBuilder sb = new StringBuilder();
        final int endExclusive = Math.min(index + length, data.length);

        for (int lineStart = index; lineStart < endExclusive; lineStart += BYTES_PER_LINE) {
            // Address header
            sb.append(String.format(Locale.ROOT, "%08X", offset + lineStart));
            sb.append(' ');

            // Hex bytes (fixed 16 columns)
            for (int k = 0; k < BYTES_PER_LINE; k++) {
                final int pos = lineStart + k;
                if (pos < endExclusive) {
                    sb.append(String.format(Locale.ROOT, "%02X", data[pos] & 0xFF));
                } else {
                    sb.append("  ");
                }
                sb.append(' ');
            }

            // ASCII view (only for actually present bytes)
            sb.append(' ');
            for (int k = 0; k < BYTES_PER_LINE; k++) {
                final int pos = lineStart + k;
                if (pos < endExclusive) {
                    sb.append(toAscii(data[pos] & 0xFF));
                }
            }

            sb.append(LS);
        }

        return sb.toString();
    }

    private static char toAscii(final int v) {
        return (v >= 32 && v <= 126) ? (char) v : '.';
    }
}