package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Readability-focused tests for ByteUtils.
 *
 * The tests are grouped by API and cover:
 * - Correct little-endian encoding/decoding for arrays, streams, DataInput/DataOutput, and supplier/consumer variants.
 * - Offsets and lengths.
 * - Error conditions: nulls, out-of-bounds, excessive lengths, and premature end-of-data.
 */
public class ByteUtilsReadableTest {

    // --- Helpers ----------------------------------------------------------------

    private static byte[] bytes(int... values) {
        byte[] b = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            b[i] = (byte) values[i];
        }
        return b;
    }

    private static final class ArrayByteSupplier implements ByteUtils.ByteSupplier {
        private final byte[] data;
        private int pos;

        ArrayByteSupplier(byte[] data) {
            this.data = data;
        }

        @Override
        public int getAsByte() {
            if (pos >= data.length) {
                return -1;
            }
            return data[pos++] & 0xFF;
        }

        int position() {
            return pos;
        }
    }

    // --- toLittleEndian: byte[] -------------------------------------------------

    @Test
    public void toLittleEndian_array_writesLittleEndianOrder() {
        byte[] out = new byte[8];
        long value = 0x0102030405060708L;

        ByteUtils.toLittleEndian(out, value, 0, 8);

        assertArrayEquals(bytes(
            0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01
        ), out);
    }

    @Test
    public void toLittleEndian_array_withOffset_andLength() {
        byte[] out = new byte[6];
        // Fill with a sentinel so we can verify unaffected regions remain unchanged.
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) 0xEE;
        }
        long value = 0x000000000000C3B2A1L;

        ByteUtils.toLittleEndian(out, value, 2, 3);

        assertArrayEquals(bytes(
            0xEE, 0xEE,        // untouched
            0xA1, 0xB2, 0xC3,  // written (LSB first)
            0xEE               // untouched
        ), out);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void toLittleEndian_array_throwsOnOutOfBounds() {
        byte[] out = new byte[3];
        ByteUtils.toLittleEndian(out, 0x01L, 1, 3); // 1 + 3 > 3 -> OOB
    }

    @Test(expected = NullPointerException.class)
    public void toLittleEndian_array_throwsOnNull() {
        ByteUtils.toLittleEndian(null, 123L, 0, 1);
    }

    // --- fromLittleEndian: byte[] (whole array) --------------------------------

    @Test
    public void fromLittleEndian_array_fullLength() {
        // 0x0102030405060708 in little-endian byte order:
        byte[] in = bytes(0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01);

        long actual = ByteUtils.fromLittleEndian(in);

        assertEquals(0x0102030405060708L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLittleEndian_array_throwsWhenArrayLengthGreaterThan8() {
        byte[] in = new byte[9];
        ByteUtils.fromLittleEndian(in);
    }

    @Test(expected = NullPointerException.class)
    public void fromLittleEndian_array_throwsOnNull() {
        ByteUtils.fromLittleEndian((byte[]) null);
    }

    // --- fromLittleEndian: byte[] (offset, length) -----------------------------

    @Test
    public void fromLittleEndian_array_offsetAndLength() {
        // Array: [EE, EE, A1, B2, C3, EE]
        byte[] in = bytes(0xEE, 0xEE, 0xA1, 0xB2, 0xC3, 0xEE);

        long actual = ByteUtils.fromLittleEndian(in, 2, 3);

        // Expect 0x00_00_00_00_00_00_C3_B2_A1
        assertEquals(0x000000000000C3B2A1L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLittleEndian_array_throwsWhenLengthGreaterThan8() {
        byte[] in = new byte[10];
        ByteUtils.fromLittleEndian(in, 1, 9);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void fromLittleEndian_array_throwsOnOutOfBounds() {
        byte[] in = new byte[4];
        ByteUtils.fromLittleEndian(in, 2, 3); // 2 + 3 > 4 -> OOB
    }

    @Test(expected = NullPointerException.class)
    public void fromLittleEndian_array_offsetLen_throwsOnNull() {
        ByteUtils.fromLittleEndian((byte[]) null, 0, 1);
    }

    // --- fromLittleEndian: ByteSupplier ----------------------------------------

    @Test
    public void fromLittleEndian_supplier_readsExpectedBytes() throws Exception {
        // Supplier returns: [0x34, 0x12] -> expect 0x0000_0000_0000_1234
        ArrayByteSupplier supplier = new ArrayByteSupplier(bytes(0x34, 0x12));

        long actual = ByteUtils.fromLittleEndian(supplier, 2);

        assertEquals(0x0000000000001234L, actual);
        assertEquals(2, supplier.position()); // consumed exactly two bytes
    }

    @Test
    public void fromLittleEndian_supplier_returnsZeroWhenLengthIsNonPositive() throws Exception {
        ArrayByteSupplier supplier = new ArrayByteSupplier(bytes(0xAA, 0xBB));

        long actualZero = ByteUtils.fromLittleEndian(supplier, 0);
        assertEquals(0L, actualZero);

        long actualNegative = ByteUtils.fromLittleEndian(supplier, -5);
        assertEquals(0L, actualNegative);

        // Ensure no bytes were consumed by the non-positive length calls
        assertEquals(0xAA, supplier.getAsByte());
    }

    @Test(expected = NullPointerException.class)
    public void fromLittleEndian_supplier_throwsOnNull() throws Exception {
        ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLittleEndian_supplier_throwsWhenLengthGreaterThan8() throws Exception {
        ByteUtils.fromLittleEndian(new ArrayByteSupplier(new byte[10]), 9);
    }

    @Test
    public void fromLittleEndian_supplier_throwsWhenSupplierEndsEarly() {
        ArrayByteSupplier supplier = new ArrayByteSupplier(bytes(0x01)); // only 1 available

        try {
            ByteUtils.fromLittleEndian(supplier, 2);
            fail("Expected IOException for premature end of data");
        } catch (IOException expected) {
            // expected
        }
    }

    // --- fromLittleEndian: InputStream -----------------------------------------

    @Test
    public void fromLittleEndian_inputStream_readsAndAdvances() throws Exception {
        byte[] data = bytes(0x11, 0x22, 0x33, 0x44, 0x55);
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        long actual = ByteUtils.fromLittleEndian(in, 3);

        assertEquals(0x0000000000332211L, actual);
        assertEquals(2, in.available()); // 5 - 3 = 2 bytes remain
    }

    @Test(expected = NullPointerException.class)
    public void fromLittleEndian_inputStream_throwsOnNull() throws Exception {
        ByteUtils.fromLittleEndian((InputStream) null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLittleEndian_inputStream_throwsWhenLengthGreaterThan8() throws Exception {
        ByteUtils.fromLittleEndian(new ByteArrayInputStream(new byte[10]), 9);
    }

    @Test
    public void fromLittleEndian_inputStream_throwsOnPrematureEnd() {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes(0x01)); // only 1 byte

        try {
            ByteUtils.fromLittleEndian(in, 2);
            fail("Expected IOException for premature end of data");
        } catch (IOException expected) {
            // expected
        }
    }

    // --- fromLittleEndian: DataInput -------------------------------------------

    @Test
    public void fromLittleEndian_dataInput_readsExactBytes() throws Exception {
        byte[] data = bytes(0xAA, 0xBB, 0xCC, 0xDD);
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(data));

        long actual = ByteUtils.fromLittleEndian(din, 4);

        assertEquals(0x00000000DDCCBBAAL, actual);
    }

    @Test(expected = NullPointerException.class)
    public void fromLittleEndian_dataInput_throwsOnNull() throws Exception {
        ByteUtils.fromLittleEndian((DataInput) null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromLittleEndian_dataInput_throwsWhenLengthGreaterThan8() throws Exception {
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(new byte[10]));
        ByteUtils.fromLittleEndian(din, 9);
    }

    @Test(expected = EOFException.class)
    public void fromLittleEndian_dataInput_propagatesEOF() throws Exception {
        // only 2 bytes available, request 4
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes(0x01, 0x02)));
        ByteUtils.fromLittleEndian(din, 4);
    }

    // --- toLittleEndian: OutputStream ------------------------------------------

    @Test
    public void toLittleEndian_outputStream_writesExpectedBytes() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long value = 0x0000000001020304L;

        ByteUtils.toLittleEndian(out, value, 4);

        assertArrayEquals(bytes(0x04, 0x03, 0x02, 0x01), out.toByteArray());
    }

    @Test(expected = NullPointerException.class)
    public void toLittleEndian_outputStream_throwsOnNull() throws Exception {
        ByteUtils.toLittleEndian((OutputStream) null, 1L, 1);
    }

    // --- toLittleEndian: DataOutput (deprecated but test for behavior) ---------

    @Test
    public void toLittleEndian_dataOutput_writesExpectedBytes() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baos);

        long value = 0x000000000000FFFFL;
        ByteUtils.toLittleEndian(dout, value, 2);

        assertArrayEquals(bytes(0xFF, 0xFF), baos.toByteArray());
    }

    // --- ByteConsumer / OutputStreamByteConsumer -------------------------------

    @Test
    public void outputStreamByteConsumer_accept_writesSingleByte() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteUtils.OutputStreamByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(out);

        consumer.accept(0x0102); // lower 8 bits only -> 0x02

        assertArrayEquals(bytes(0x02), out.toByteArray());
    }

    @Test
    public void toLittleEndian_withByteConsumer_writesAllBytes() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteUtils.ByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(out);

        long value = 0x0000000000C0FFEE; // 3 bytes
        ByteUtils.toLittleEndian(consumer, value, 3);

        assertArrayEquals(bytes(0xEE, 0xFF, 0xC0), out.toByteArray());
    }

    @Test(expected = NullPointerException.class)
    public void toLittleEndian_withByteConsumer_throwsOnNull() throws Exception {
        ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 1L, 1);
    }

    // --- Misc ------------------------------------------------------------------

    @Test
    public void emptyByteArray_isZeroLength() {
        assertNotNull(ByteUtils.EMPTY_BYTE_ARRAY);
        assertEquals(0, ByteUtils.EMPTY_BYTE_ARRAY.length);
    }
}