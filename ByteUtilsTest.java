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

    @Test
    void testFromLittleEndianWithArrayAndOffset() {
        byte[] inputBytes = { 1, 2, 3, 4, 5 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputBytes, 1, 3));
    }

    @Test
    void testFromLittleEndianWithArray() {
        byte[] inputBytes = { 2, 3, 4 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputBytes));
    }

    @Test
    void testFromLittleEndianWithArrayThrowsForExcessiveLength() {
        byte[] inputBytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(inputBytes));
    }

    @Test
    void testFromLittleEndianWithArrayUnsignedInt32() {
        byte[] inputBytes = { 2, 3, 4, (byte) 128 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputBytes));
    }

    @Test
    void testFromLittleEndianWithArrayAndOffsetThrowsForExcessiveLength() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
    }

    @Test
    void testFromLittleEndianWithArrayAndOffsetUnsignedInt32() {
        byte[] inputBytes = { 1, 2, 3, 4, (byte) 128 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputBytes, 1, 4));
    }

    @Test
    void testFromLittleEndianWithDataInput() throws IOException {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(dataInput, 3));
    }

    @Test
    void testFromLittleEndianWithDataInputThrowsForExcessiveLength() {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(dataInput, 9));
    }

    @Test
    void testFromLittleEndianWithDataInputThrowsForPrematureEnd() {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));
        assertThrows(EOFException.class, () -> fromLittleEndian(dataInput, 3));
    }

    @Test
    void testFromLittleEndianWithDataInputUnsignedInt32() throws IOException {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(dataInput, 4));
    }

    @Test
    void testFromLittleEndianWithStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputStream, 3));
    }

    @Test
    void testFromLittleEndianWithStreamThrowsForExcessiveLength() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), 9));
    }

    @Test
    void testFromLittleEndianWithStreamThrowsForPrematureEnd() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(inputStream, 3));
    }

    @Test
    void testFromLittleEndianWithStreamUnsignedInt32() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(inputStream, 4));
    }

    @Test
    void testFromLittleEndianWithSupplier() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(inputStream), 3));
    }

    @Test
    void testFromLittleEndianWithSupplierThrowsForExcessiveLength() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY)), 9));
    }

    @Test
    void testFromLittleEndianWithSupplierThrowsForPrematureEnd() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3 });
        assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(inputStream), 3));
    }

    @Test
    void testFromLittleEndianWithSupplierUnsignedInt32() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        assertEquals(expectedValue, fromLittleEndian(new InputStreamByteSupplier(inputStream), 4));
    }

    @Test
    void testToLittleEndianToByteArray() {
        byte[] outputBytes = new byte[4];
        toLittleEndian(outputBytes, 2 + 3 * 256 + 4 * 256 * 256, 1, 3);
        byte[] expectedBytes = { 2, 3, 4 };
        assertArrayEquals(expectedBytes, Arrays.copyOfRange(outputBytes, 1, 4));
    }

    @Test
    void testToLittleEndianToByteArrayUnsignedInt32() {
        byte[] outputBytes = new byte[4];
        toLittleEndian(outputBytes, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 0, 4);
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        assertArrayEquals(expectedBytes, outputBytes);
    }

    @Test
    void testToLittleEndianToConsumer() throws IOException {
        byte[] expectedBytes = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), 2 + 3 * 256 + 4 * 256 * 256, 3);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }

    @Test
    void testToLittleEndianToConsumerUnsignedInt32() throws IOException {
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(outputStream), 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }

    @Test
    void testToLittleEndianToDataOutput() throws IOException {
        byte[] expectedBytes = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }

    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }

    @Test
    void testToLittleEndianToStream() throws IOException {
        byte[] expectedBytes = { 2, 3, 4 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, 2 + 3 * 256 + 4 * 256 * 256, 3);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }

    @Test
    void testToLittleEndianToStreamUnsignedInt32() throws IOException {
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byte[] outputBytes = outputStream.toByteArray();
            assertArrayEquals(expectedBytes, outputBytes);
        }
    }
}