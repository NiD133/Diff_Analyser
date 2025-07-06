package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.apache.commons.compress.utils.ByteUtils.EMPTY_BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.Test;

class ByteUtilsTest {

    // Section for testing fromLittleEndian functionality
    // --------------------------------------------------

    // Testing fromLittleEndian with byte arrays
    @Test
    void testFromLittleEndianByteArray() {
        final byte[] b = { 1, 2, 3, 4, 5 };
        assertEquals(2 + 3 * 256 + 4 * 256 * 256, fromLittleEndian(b, 1, 3));
    }

    @Test
    void testFromLittleEndianByteArrayOneArg() {
        final byte[] b = { 2, 3, 4 };
        assertEquals(2 + 3 * 256 + 4 * 256 * 256, fromLittleEndian(b));
    }

    @Test
    void testFromLittleEndianByteArrayOneArgUnsignedInt32() {
        final byte[] b = { 2, 3, 4, (byte) 128 };
        assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, fromLittleEndian(b));
    }

    @Test
    void testFromLittleEndianByteArrayThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(EMPTY_BYTE_ARRAY, 0, 9));
    }

    @Test
    void testFromLittleEndianByteArrayThrowsForLengthTooBigOneArg() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    }

    // Testing fromLittleEndian with DataInput
    @Test
    void testFromLittleEndianDataInput() throws IOException {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));
        assertEquals(2 + 3 * 256 + 4 * 256 * 256, fromLittleEndian(din, 3));
    }

    @Test
    void testFromLittleEndianDataInputUnsignedInt32() throws IOException {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));
        assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, fromLittleEndian(din, 4));
    }

    @Test
    void testFromLittleEndianDataInputThrowsForLengthTooBig() {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(EMPTY_BYTE_ARRAY));
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(din, 9));
    }

    @Test
    void testFromLittleEndianDataInputThrowsForPrematureEnd() {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));
        assertThrows(EOFException.class, () -> fromLittleEndian(din, 3));
    }

    // Testing fromLittleEndian with InputStream
    @Test
    void testFromLittleEndianInputStream() throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        assertEquals(2 + 3 * 256 + 4 * 256 * 256, fromLittleEndian(bin, 3));
    }

    @Test
    void testFromLittleEndianInputStreamUnsignedInt32() throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, fromLittleEndian(bin, 4));
    }

    @Test
    void testFromLittleEndianInputStreamThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new ByteArrayInputStream(EMPTY_BYTE_ARRAY), 9));
    }

    @Test
    void testFromLittleEndianInputStreamThrowsForPrematureEnd() {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(bin, 3));
    }

    // Testing fromLittleEndian with ByteSupplier
    @Test
    void testFromLittleEndianByteSupplier() throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        assertEquals(2 + 3 * 256 + 4 * 256 * 256, fromLittleEndian(new InputStreamByteSupplier(bin), 3));
    }

    @Test
    void testFromLittleEndianByteSupplierUnsignedInt32() throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, fromLittleEndian(new InputStreamByteSupplier(bin), 4));
    }

    @Test
    void testFromLittleEndianByteSupplierThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(EMPTY_BYTE_ARRAY)), 9));
    }

    @Test
    void testFromLittleEndianByteSupplierThrowsForPrematureEnd() {
        final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(bin), 3));
    }

    // Section for testing toLittleEndian functionality
    // -------------------------------------------------

    // Testing toLittleEndian with byte arrays
    @Test
    void testToLittleEndianByteArray() {
        final byte[] b = new byte[4];
        toLittleEndian(b, 2 + 3 * 256 + 4 * 256 * 256, 1, 3);
        assertArrayEquals(new byte[] { 2, 3, 4 }, Arrays.copyOfRange(b, 1, 4));
    }

    @Test
    void testToLittleEndianByteArrayUnsignedInt32() {
        final byte[] b = new byte[4];
        toLittleEndian(b, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 0, 4);
        assertArrayEquals(new byte[] { 2, 3, 4, (byte) 128 }, b);
    }

    // Testing toLittleEndian with ByteConsumer
    @Test
    void testToLittleEndianByteConsumer() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(bos), 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianByteConsumerUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(bos), 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Testing toLittleEndian with DataOutput
    @Test
    void testToLittleEndianDataOutput() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);
            toLittleEndian(dos, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianDataOutputUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);
            toLittleEndian(dos, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Testing toLittleEndian with OutputStream
    @Test
    void testToLittleEndianOutputStream() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(bos, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianOutputStreamUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(bos, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }
}