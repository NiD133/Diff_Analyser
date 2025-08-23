package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for SerializedString.
 *
 * Notes:
 * - Tests rely only on public API of SerializedString and standard JDK classes.
 * - Avoids EvoSuite-specific runners/mocks to reduce noise and improve clarity.
 * - Groups assertions by behavior: construction/validation, accessors, equality,
 *   caching, append/write/put operations for both quoted and unquoted variants.
 */
public class SerializedStringTest {

    // Construction and basic accessors

    @Test
    public void constructor_nullValue_throwsNPEWithHelpfulMessage() {
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> new SerializedString(null));
        assertTrue(npe.getMessage().contains("Null String illegal for SerializedString"));
    }

    @Test
    public void emptyString_basicAccessors() {
        SerializedString s = new SerializedString("");

        assertEquals("", s.getValue());
        assertEquals("", s.toString());
        assertEquals(0, s.charLength());
        assertEquals(0, s.asQuotedChars().length);
        assertEquals(0, s.asUnquotedUTF8().length);
    }

    @Test
    public void singleChar_basicAccessorsAndAppend() {
        SerializedString s = new SerializedString("j");

        assertEquals(1, s.charLength());

        char[] out = new char[4];
        int written = s.appendUnquoted(out, 1);
        assertEquals(1, written);
        assertArrayEquals(new char[] {'\u0000', 'j', '\u0000', '\u0000'}, out);
    }

    // Equality and hashing

    @Test
    public void equality_basicCases() {
        SerializedString a = new SerializedString("");
        SerializedString b = new SerializedString("");

        assertTrue(a.equals(a));            // reflexive
        assertTrue(a.equals(b));            // equal values
        assertEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(null));        // null
        assertFalse(a.equals(ByteBuffer.allocate(0))); // different type
    }

    // Caching behavior

    @Test
    public void caching_reusesSameInstances_forQuotedAndUnquotedEncodings() {
        SerializedString s = new SerializedString("R^$t>9-hl+");

        byte[] unq1 = s.asUnquotedUTF8();
        byte[] unq2 = s.asUnquotedUTF8();
        assertSame(unq1, unq2);

        byte[] qq1 = s.asQuotedUTF8();
        byte[] qq2 = s.asQuotedUTF8();
        assertSame(qq1, qq2);

        char[] qc1 = s.asQuotedChars();
        char[] qc2 = s.asQuotedChars();
        assertSame(qc1, qc2);
    }

    // Append to arrays (UTF-8 bytes)

    @Test
    public void appendUnquotedUTF8_canCopyIntoItsOwnCachedArray() {
        SerializedString s = new SerializedString("TP1aO6Zd");
        byte[] dest = s.asUnquotedUTF8(); // use the cached instance as destination
        int written = s.appendUnquotedUTF8(dest, 0);
        assertEquals(dest.length, written);
    }

    @Test
    public void appendUnquotedUTF8_offsetPastEnd_returnsMinusOne() {
        SerializedString s = new SerializedString("");
        byte[] dest = s.asUnquotedUTF8(); // zero-length array
        assertEquals(-1, s.appendUnquotedUTF8(dest, 1));
    }

    @Test
    public void appendQuotedUTF8_writesExpectedBytes() {
        SerializedString s = new SerializedString(", coipied ");
        byte[] expected = s.asQuotedUTF8();

        byte[] dest = new byte[expected.length + 10];
        int written = s.appendQuotedUTF8(dest, 0);

        assertEquals(expected.length, written);
        assertArrayEquals(expected, slice(dest, 0, written));
    }

    @Test
    public void appendQuotedUTF8_offsetBeyondCapacity_returnsMinusOne() {
        SerializedString s = new SerializedString("M:oX:\"bsoHuP");
        byte[] dest = new byte[8];
        assertEquals(-1, s.appendQuotedUTF8(dest, dest.length + 1));
    }

    // Append to arrays (chars)

    @Test
    public void appendQuoted_emptyString_writesZeroChars() {
        SerializedString s = new SerializedString("");
        char[] out = new char[5];
        int written = s.appendQuoted(out, 1);
        assertEquals(0, written);
    }

    @Test
    public void appendUnquoted_nullCharArray_throwsNPE() {
        SerializedString s = new SerializedString("wSQ ");
        assertThrows(NullPointerException.class, () -> s.appendUnquoted(null, 0));
    }

    // write* to OutputStream

    @Test
    public void writeUnquotedUTF8_toStream_matchesAsUnquotedUTF8() throws Exception {
        SerializedString s = new SerializedString("E]`R4#OI%");
        byte[] expected = s.asUnquotedUTF8();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int written = s.writeUnquotedUTF8(out);
            assertEquals(expected.length, written);
            assertArrayEquals(expected, out.toByteArray());
        }
    }

    @Test
    public void writeQuotedUTF8_toStream_matchesAsQuotedUTF8() throws Exception {
        SerializedString s = new SerializedString("a\"b"); // contains a quote, exercises escaping
        byte[] expected = s.asQuotedUTF8();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int written = s.writeQuotedUTF8(out);
            assertEquals(expected.length, written);
            assertArrayEquals(expected, out.toByteArray());
        }
    }

    @Test
    public void writeQuotedUTF8_nullStream_throwsNPE() {
        SerializedString s = new SerializedString("");
        assertThrows(NullPointerException.class, () -> s.writeQuotedUTF8((OutputStream) null));
    }

    @Test
    public void writeUnquotedUTF8_nullStream_throwsNPE() {
        SerializedString s = new SerializedString("");
        assertThrows(NullPointerException.class, () -> s.writeUnquotedUTF8((OutputStream) null));
    }

    // put* into ByteBuffer

    @Test
    public void putUnquotedUTF8_successAndInsufficientCapacity() {
        SerializedString s = new SerializedString(" ");
        ByteBuffer ok = ByteBuffer.allocateDirect(4);
        int writtenOk = s.putUnquotedUTF8(ok);
        assertEquals(1, writtenOk);
        assertEquals(3, ok.remaining());

        ByteBuffer small = ByteBuffer.allocate(0);
        int writtenSmall = s.putUnquotedUTF8(small);
        assertEquals(-1, writtenSmall);
    }

    @Test
    public void putQuotedUTF8_successAndInsufficientCapacity() {
        SerializedString s = new SerializedString("8+19F:23Vy=-x");
        ByteBuffer ok = ByteBuffer.allocateDirect(64);
        int writtenOk = s.putQuotedUTF8(ok);
        assertTrue("Should write a positive number of bytes", writtenOk > 0);
        assertTrue("Remaining should decrease after write", ok.remaining() < 64);

        ByteBuffer small = ByteBuffer.allocateDirect(0);
        int writtenSmall = s.putQuotedUTF8(small);
        assertEquals(-1, writtenSmall);
    }

    @Test
    public void putQuotedUTF8_nullBuffer_throwsNPE() {
        SerializedString s = new SerializedString("Q");
        s.asQuotedUTF8(); // warm up cache path used by many implementations
        assertThrows(NullPointerException.class, () -> s.putQuotedUTF8(null));
    }

    @Test
    public void putUnquotedUTF8_nullBuffer_throwsNPE() {
        SerializedString s = new SerializedString("X fh]-BVE2`Wh");
        assertThrows(NullPointerException.class, () -> s.putUnquotedUTF8(null));
    }

    // append* (byte[]) null checks

    @Test
    public void appendUnquotedUTF8_nullArray_throwsNPE() {
        SerializedString s = new SerializedString("");
        assertThrows(NullPointerException.class, () -> s.appendUnquotedUTF8(null, 0));
    }

    // Helpers

    private static byte[] slice(byte[] src, int from, int len) {
        byte[] out = new byte[len];
        System.arraycopy(src, from, out, 0, len);
        return out;
    }
}