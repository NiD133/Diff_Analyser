package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Readable, intention‑revealing tests for RandomAccessFileOrArray.
 *
 * Notes:
 * - These tests exercise the most important behaviors with clear inputs and expectations.
 * - Helpers keep individual tests focused on “what” rather than “how.”
 * - We avoid EvoSuite-specific runners and verifications for simplicity and portability.
 */
public class RandomAccessFileOrArrayReadableTest {

    // ---------- Helpers ----------

    private static RandomAccessFileOrArray raf(byte... bytes) {
        // Deprecated ctor is used on purpose to keep tests simple and self-contained.
        return new RandomAccessFileOrArray(bytes);
    }

    private static byte b(int unsigned) {
        return (byte) (unsigned & 0xFF);
    }

    // ---------- Basic navigation ----------

    @Test
    public void getFilePointer_startsAtZero_andAdvancesAfterReads() throws Exception {
        RandomAccessFileOrArray in = raf(1, 2, 3, 4);
        assertEquals(0L, in.getFilePointer());

        in.readInt(); // consumes 4 bytes
        assertEquals(4L, in.getFilePointer());
    }

    @Test
    public void seek_movesThePointer() throws Exception {
        RandomAccessFileOrArray in = raf(10, 20, 30);
        in.seek(1);
        assertEquals(20, in.readUnsignedByte());
        assertEquals(2L, in.getFilePointer());
    }

    @Test
    public void skip_skipsBytes_and_skipBytesBehavesSimilarly() throws Exception {
        RandomAccessFileOrArray in = raf(1, 2, 3, 4);
        assertEquals(2L, in.skip(2));
        assertEquals(2L, in.getFilePointer());
        assertEquals(3, in.readUnsignedByte());

        RandomAccessFileOrArray in2 = raf(1, 2, 3, 4);
        assertEquals(2, in2.skipBytes(2));
        assertEquals(2L, in2.getFilePointer());
        assertEquals(3, in2.readUnsignedByte());
    }

    // ---------- Pushback ----------

    @Test
    public void pushBack_returnsByteBeforeUnderlyingData() throws Exception {
        RandomAccessFileOrArray in = raf(10, 20);
        assertEquals(10, in.readUnsignedByte());

        in.pushBack((byte) 99);
        assertEquals(99, in.readUnsignedByte()); // pushed back byte first
        assertEquals(20, in.readUnsignedByte()); // then underlying stream
    }

    // ---------- Unsigned/signed short ----------

    @Test
    public void readUnsignedShortLE_readsLittleEndian() throws Exception {
        // 0x1234 in little endian is bytes [0x34, 0x12]
        RandomAccessFileOrArray in = raf(b(0x34), b(0x12));
        assertEquals(0x1234, in.readUnsignedShortLE());
        assertEquals(2L, in.getFilePointer());
    }

    @Test
    public void readShort_readsBigEndianSigned() throws Exception {
        // 0x7F01 -> 32513
        RandomAccessFileOrArray in = raf(b(0x7F), b(0x01));
        assertEquals(32513, in.readShort());
        assertEquals(2L, in.getFilePointer());
    }

    // ---------- Char ----------

    @Test
    public void readCharLE_and_readChar_interpretEndianness() throws Exception {
        // Little endian 'A' (0x0041) is [0x41, 0x00]
        RandomAccessFileOrArray in1 = raf(b(0x41), b(0x00));
        assertEquals('A', in1.readCharLE());

        // Big endian 'A' (0x0041) is [0x00, 0x41]
        RandomAccessFileOrArray in2 = raf(b(0x00), b(0x41));
        assertEquals('A', in2.readChar());
    }

    // ---------- Int / Unsigned Int ----------

    @Test
    public void readIntLE_readsLittleEndianInt() throws Exception {
        // Little endian 0x12345678 -> [78,56,34,12]
        RandomAccessFileOrArray in = raf(b(0x78), b(0x56), b(0x34), b(0x12));
        assertEquals(0x12345678, in.readIntLE());
        assertEquals(4L, in.getFilePointer());
    }

    @Test
    public void readUnsignedInt_readsBigEndianUnsigned() throws Exception {
        // Big endian 255 -> [0,0,0,255]
        RandomAccessFileOrArray in = raf(b(0x00), b(0x00), b(0x00), b(0xFF));
        assertEquals(255L, in.readUnsignedInt());
        assertEquals(4L, in.getFilePointer());
    }

    // ---------- Long ----------

    @Test
    public void readLongLE_readsLittleEndianLong() throws Exception {
        // Little endian 1L -> [1,0,0,0,0,0,0,0]
        RandomAccessFileOrArray in = raf(1, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(1L, in.readLongLE());
        assertEquals(8L, in.getFilePointer());
    }

    // ---------- Float / Double ----------

    @Test
    public void readDoubleLE_readsLittleEndianDouble_1_0() throws Exception {
        // Double 1.0 is 0x3FF0000000000000
        // Little endian bytes: 00 00 00 00 00 00 F0 3F
        RandomAccessFileOrArray in = raf(
                b(0x00), b(0x00), b(0x00), b(0x00),
                b(0x00), b(0x00), b(0xF0), b(0x3F)
        );
        assertEquals(1.0, in.readDoubleLE(), 0.0);
        assertEquals(8L, in.getFilePointer());
    }

    @Test
    public void readDouble_readsBigEndianDouble_1_0() throws Exception {
        // Big endian 1.0 -> 3F F0 00 00 00 00 00 00
        RandomAccessFileOrArray in = raf(
                b(0x3F), b(0xF0), b(0x00), b(0x00),
                b(0x00), b(0x00), b(0x00), b(0x00)
        );
        assertEquals(1.0, in.readDouble(), 0.0);
        assertEquals(8L, in.getFilePointer());
    }

    // ---------- Boolean ----------

    @Test
    public void readBoolean_interpretsZeroAndNonZero() throws Exception {
        RandomAccessFileOrArray in = raf(0, 1);
        assertFalse(in.readBoolean());
        assertTrue(in.readBoolean());
    }

    // ---------- readFully ----------

    @Test
    public void readFully_readsAllRequestedBytes() throws Exception {
        RandomAccessFileOrArray in = raf(1, 2, 3, 4);
        byte[] out = new byte[4];
        in.readFully(out);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, out);
        assertEquals(4L, in.getFilePointer());
    }

    // ---------- readLine / readUTF / readString ----------

    @Test
    public void readLine_readsUntilNewline_withoutIncludingIt() throws Exception {
        RandomAccessFileOrArray in = raf('a', 'b', '\n', 'c');
        assertEquals("ab", in.readLine());
        assertEquals(3L, in.getFilePointer()); // "ab\n" consumed
        assertEquals('c', (char) in.readUnsignedByte());
    }

    @Test
    public void readUTF_readsModifiedUTF() throws Exception {
        // DataInput.readUTF expects: [2-byte length][payload]
        // length 3: 00 03, payload: "abc"
        RandomAccessFileOrArray in = raf(b(0x00), b(0x03), b('a'), b('b'), b('c'));
        assertEquals("abc", in.readUTF());
        assertEquals(5L, in.getFilePointer());
    }

    @Test
    public void readString_readsBytesWithGivenEncoding() throws Exception {
        byte[] bytes = "hello world".getBytes(StandardCharsets.ISO_8859_1);
        RandomAccessFileOrArray in = raf(bytes);
        assertEquals("hel", in.readString(3, "ISO-8859-1"));
        assertEquals(3L, in.getFilePointer());
    }

    // ---------- Views ----------

    @Test
    public void createView_hasIndependentPointer() throws Exception {
        RandomAccessFileOrArray original = raf(10, 20, 30);
        RandomAccessFileOrArray view = original.createView();

        // Advance view only
        assertEquals(10, view.readUnsignedByte());
        assertEquals(1L, view.getFilePointer());

        // Original pointer remains unchanged
        assertEquals(0L, original.getFilePointer());
        assertEquals(10, original.readUnsignedByte()); // reads same first byte independently
    }

    // ---------- EOF behavior ----------

    @Test(expected = EOFException.class)
    public void readInt_throwsEOF_whenNotEnoughBytes() throws Exception {
        RandomAccessFileOrArray in = raf(1, 2); // only 2 bytes
        in.readInt();
    }

    @Test(expected = EOFException.class)
    public void readDouble_throwsEOF_whenNoBytes() throws Exception {
        RandomAccessFileOrArray in = raf(); // empty
        in.readDouble();
    }

    @Test(expected = EOFException.class)
    public void readUnsignedShortLE_throwsEOF_whenSingleByteOnly() throws Exception {
        RandomAccessFileOrArray in = raf(0x12);
        in.readUnsignedShortLE();
    }
}