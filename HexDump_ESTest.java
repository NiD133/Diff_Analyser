package org.apache.commons.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Readable tests for HexDump that cover normal behavior, null checks, bounds checks,
 * and I/O error propagation without relying on EvoSuite scaffolding or brittle assertions.
 *
 * The assertions aim to validate key aspects of the output (offset presence and hex bytes)
 * without depending on exact spacing or column layout.
 */
public class HexDumpTest {

    // Helper: simple assert that the produced dump contains all given tokens.
    private static void assertContainsAll(final String dump, final String... tokens) {
        for (final String token : tokens) {
            assertTrue("Dump should contain token: " + token + "\nDump:\n" + dump, dump.contains(token));
        }
    }

    @Test
    public void constructor_isAccessible() {
        assertNotNull(new HexDump());
    }

    @Test
    public void dumpToAppendable_writesSingleLineWithOffsetAndHexBytes() throws Exception {
        // Given
        final byte[] data = { 0x00, 0x01, 0x7F, 0x41, (byte) 0xFF };
        final StringWriter out = new StringWriter();

        // When
        HexDump.dump(data, out);

        // Then
        final String dump = out.toString();
        // Contains an 8-digit offset for the first line.
        assertTrue(dump.contains("00000000"));
        // Contains the hex codes for our data (upper-case in Commons IO).
        assertContainsAll(dump, "00", "01", "7F", "41", "FF");
        // Ends with a line separator.
        assertTrue("Dump should end with a line separator", dump.endsWith(System.lineSeparator()));
    }

    @Test
    public void dumpToAppendable_withOffsetAndRange_writesRequestedSlice() throws Exception {
        // Given: 0..31 to make ranges easy to reason about.
        final byte[] data = new byte[32];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        final StringWriter out = new StringWriter();

        // When: dump 8 bytes starting at index 4 with a non-zero offset.
        HexDump.dump(data, 0x100L, out, 4, 8);

        // Then
        final String dump = out.toString();
        assertTrue("Should contain the starting offset", dump.contains("00000100"));
        // Should contain the endpoints of our requested slice.
        assertContainsAll(dump, "04", "05", "06", "07", "08", "09", "0A", "0B");
    }

    @Test
    public void dumpToAppendable_propagatesIOException() {
        // Given: Appendable that always throws.
        final Appendable failing = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("boom");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("boom");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("boom");
            }
        };

        // When/Then
        assertThrows(IOException.class, () -> HexDump.dump(new byte[] { 1, 2, 3 }, 0L, failing, 0, 3));
    }

    @Test
    public void dumpToAppendable_nullAppendable_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> HexDump.dump(new byte[] { 1 }, null));
        assertThrows(NullPointerException.class, () -> HexDump.dump(new byte[] { 1 }, 0L, null, 0, 1));
    }

    @Test
    public void dumpToAppendable_nullData_throwsNullPointerException() {
        final StringWriter out = new StringWriter();
        assertThrows(NullPointerException.class, () -> HexDump.dump(null, out));
        assertThrows(NullPointerException.class, () -> HexDump.dump(null, 0L, out, 0, 0));
    }

    @Test
    public void dumpToAppendable_outOfBoundsIndexOrLength_throwsArrayIndexOutOfBoundsException() {
        final StringWriter out = new StringWriter();
        final byte[] data = new byte[2];

        // index past end
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, 0L, out, 2, 1));

        // negative length
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, 0L, out, 0, -1));

        // range exceeds length
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, 0L, out, 1, 2));
    }

    @Test
    public void dumpToOutputStream_writesData() throws Exception {
        final byte[] data = { 0, 1, 2, 3 };
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        HexDump.dump(data, 0L, out, 0);

        final String dump = new String(out.toByteArray(), Charset.defaultCharset());
        assertTrue(dump.contains("00000000"));
        assertContainsAll(dump, "00", "01", "02", "03");
    }

    @Test
    public void dumpToOutputStream_nullStream_throwsNullPointerException() {
        final byte[] data = { 1, 2, 3 };
        final OutputStream nullStream = null;
        assertThrows(NullPointerException.class, () -> HexDump.dump(data, 0L, nullStream, 0));
    }

    @Test
    public void dumpToOutputStream_outOfBoundsIndex_throwsArrayIndexOutOfBoundsException() {
        final byte[] data = new byte[2];
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        // index equal to length
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, 0L, out, 2));

        // negative index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, 0L, out, -1));
    }
}