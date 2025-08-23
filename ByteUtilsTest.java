package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
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
import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.Test;

class ByteUtilsTest {

    // Helper method to calculate expected little-endian value
    private long calculateLittleEndianValue(int... bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value += (bytes[i] & 0xFFL) << (8 * i);
        }
        return value;
    }

    @Test
    void testFromLittleEndianFromArray() {
        final byte[] byteArray = { 1, 2, 3, 4, 5 };
        long expectedValue = calculateLittleEndianValue(2, 3, 4);
        assertEquals(expectedValue, fromLittleEndian(byteArray, 1, 3));
    }

    @Test
    void testFromLittleEndianFromArrayOneArg() {
        final byte[] byteArray = { 2, 3, 4 };
        long expectedValue = calculateLittleEndianValue(2, 3, 4);
        assertEquals(expectedValue, fromLittleEndian(byteArray));
    }

    @Test
    void testFromLittleEndianFromArrayOneArgThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    }

    @Test
    void testFromLittleEndianFromArrayOneArgUnsignedInt32() {
        final byte[] byteArray = { 2, 3, 4, (byte) 128 };
        long expectedValue = calculateLittleEndianValue(2, 3, 4, 128);
        assertEquals(expectedValue, fromLittleEndian(byteArray));
    }

    @Test
    void testFromLittleEndianFromArrayThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
    }

    @Test
    void testFromLittleEndianFromArrayUnsignedInt32() {
        final byte[] byteArray = { 1, 2, 3, 4, (byte) 128 };
        long expectedValue = calculateLittleEndianValue(2, 3, 4, 128);
        assertEquals(expectedValue, fromLittleEndian(byteArray, 1, 4));
    }

    @Test
    void testFromLittleEndianFromDataInput() throws IOException {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));
        long expectedValue = calculateLittleEndianValue(2, 3, 4);
        assertEquals(expectedValue, fromLittleEndian(dataInput, 3));
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForLengthTooBig() {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(dataInput, 9));
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForPrematureEnd() {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));
        assertThrows(EOFException.class, () -> fromLittleEndian(dataInput, 3));
    }

    @Test
    void testFromLittleEndianFromDataInputUnsignedInt32() throws IOException {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));
        long expectedValue = calculateLittleEndianValue(2, 3, 4, 128);
        assertEquals(expectedValue, fromLittleEndian(dataInput, 4));
    }

    @Test
    void testFromLittleEndianFromStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = calculateLittleEndianValue(2, 3, 4);
        assertEquals(expectedValue, fromLittleEndian(byteArrayInputStream, 3));
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), 9));
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForPrematureEnd() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(byteArrayInputStream, 3));
    }

    @Test
    void testFromLittleEndianFromStreamUnsignedInt32() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = calculateLittleEndianValue(2, 3, 4, 128);
        assertEquals(expectedValue, fromLittleEndian(byteArrayInputStream, 4));
    }

    @Test
    void testFromLittleEndianFromSupplier() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = calculateLittleEndianValue(2, 3, 4);
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(byteArrayInputStream), 3));
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY)), 9));
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForPrematureEnd() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(byteArrayInputStream), 3));
    }

    @Test
    void testFromLittleEndianFromSupplierUnsignedInt32() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = calculateLittleEndianValue(2, 3, 4, 128);
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(byteArrayInputStream), 4));
    }

    @Test
    void testToLittleEndianToByteArray() {
        final byte[] byteArray = new byte[4];
        toLittleEndian(byteArray, calculateLittleEndianValue(2, 3, 4), 1, 3);
        assertArrayEquals(new byte[] { 2, 3, 4 }, Arrays.copyOfRange(byteArray, 1, 4));
    }

    @Test
    void testToLittleEndianToByteArrayUnsignedInt32() {
        final byte[] byteArray = new byte[4];
        toLittleEndian(byteArray, calculateLittleEndianValue(2, 3, 4, 128), 0, 4);
        assertArrayEquals(new byte[] { 2, 3, 4, (byte) 128 }, byteArray);
    }

    @Test
    void testToLittleEndianToConsumer() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), calculateLittleEndianValue(2, 3, 4), 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianToConsumerUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), calculateLittleEndianValue(2, 3, 4, 128), 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianToDataOutput() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, calculateLittleEndianValue(2, 3, 4), 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, calculateLittleEndianValue(2, 3, 4, 128), 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianToStream() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, calculateLittleEndianValue(2, 3, 4), 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    @Test
    void testToLittleEndianToStreamUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, calculateLittleEndianValue(2, 3, 4, 128), 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }
}