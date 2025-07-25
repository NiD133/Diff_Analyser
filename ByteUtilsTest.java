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

    // Test conversion from little-endian byte array to long
    @Test
    void testFromLittleEndianFromArray() {
        final byte[] byteArray = { 1, 2, 3, 4, 5 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(byteArray, 1, 3));
    }

    // Test conversion from little-endian byte array to long with default length
    @Test
    void testFromLittleEndianFromArrayOneArg() {
        final byte[] byteArray = { 2, 3, 4 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(byteArray));
    }

    // Test exception for byte array length exceeding maximum
    @Test
    void testFromLittleEndianFromArrayOneArgThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    }

    // Test conversion from little-endian byte array to unsigned int32
    @Test
    void testFromLittleEndianFromArrayOneArgUnsignedInt32() {
        final byte[] byteArray = { 2, 3, 4, (byte) 128 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(byteArray));
    }

    // Test exception for byte array length exceeding maximum with offset
    @Test
    void testFromLittleEndianFromArrayThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
    }

    // Test conversion from little-endian byte array to unsigned int32 with offset
    @Test
    void testFromLittleEndianFromArrayUnsignedInt32() {
        final byte[] byteArray = { 1, 2, 3, 4, (byte) 128 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(byteArray, 1, 4));
    }

    // Test conversion from little-endian DataInput to long
    @Test
    void testFromLittleEndianFromDataInput() throws IOException {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(dataInput, 3));
    }

    // Test exception for DataInput length exceeding maximum
    @Test
    void testFromLittleEndianFromDataInputThrowsForLengthTooBig() {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(dataInput, 9));
    }

    // Test exception for premature end of DataInput
    @Test
    void testFromLittleEndianFromDataInputThrowsForPrematureEnd() {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));
        assertThrows(EOFException.class, () -> fromLittleEndian(dataInput, 3));
    }

    // Test conversion from little-endian DataInput to unsigned int32
    @Test
    void testFromLittleEndianFromDataInputUnsignedInt32() throws IOException {
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(dataInput, 4));
    }

    // Test conversion from little-endian InputStream to long
    @Test
    void testFromLittleEndianFromStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputStream, 3));
    }

    // Test exception for InputStream length exceeding maximum
    @Test
    void testFromLittleEndianFromStreamThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), 9));
    }

    // Test exception for premature end of InputStream
    @Test
    void testFromLittleEndianFromStreamThrowsForPrematureEnd() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(inputStream, 3));
    }

    // Test conversion from little-endian InputStream to unsigned int32
    @Test
    void testFromLittleEndianFromStreamUnsignedInt32() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputStream, 4));
    }

    // Test conversion from little-endian ByteSupplier to long
    @Test
    void testFromLittleEndianFromSupplier() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(inputStream), 3));
    }

    // Test exception for ByteSupplier length exceeding maximum
    @Test
    void testFromLittleEndianFromSupplierThrowsForLengthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY)), 9));
    }

    // Test exception for premature end of ByteSupplier
    @Test
    void testFromLittleEndianFromSupplierThrowsForPrematureEnd() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(inputStream), 3));
    }

    // Test conversion from little-endian ByteSupplier to unsigned int32
    @Test
    void testFromLittleEndianFromSupplierUnsignedInt32() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(inputStream), 4));
    }

    // Test conversion to little-endian byte array
    @Test
    void testToLittleEndianToByteArray() {
        final byte[] byteArray = new byte[4];
        toLittleEndian(byteArray, 2 + 3 * 256 + 4 * 256 * 256, 1, 3);
        assertArrayEquals(new byte[] { 2, 3, 4 }, Arrays.copyOfRange(byteArray, 1, 4));
    }

    // Test conversion to little-endian byte array for unsigned int32
    @Test
    void testToLittleEndianToByteArrayUnsignedInt32() {
        final byte[] byteArray = new byte[4];
        toLittleEndian(byteArray, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 0, 4);
        assertArrayEquals(new byte[] { 2, 3, 4, (byte) 128 }, byteArray);
    }

    // Test conversion to little-endian using ByteConsumer
    @Test
    void testToLittleEndianToConsumer() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Test conversion to little-endian using ByteConsumer for unsigned int32
    @Test
    void testToLittleEndianToConsumerUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Test conversion to little-endian using DataOutput
    @Test
    void testToLittleEndianToDataOutput() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Test conversion to little-endian using DataOutput for unsigned int32
    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Test conversion to little-endian using OutputStream
    @Test
    void testToLittleEndianToStream() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }

    // Test conversion to little-endian using OutputStream for unsigned int32
    @Test
    void testToLittleEndianToStreamUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = outputStream.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }
}