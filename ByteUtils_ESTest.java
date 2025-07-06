package org.apache.commons.compress.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteUtilsTest {

    @Test
    @DisplayName("Test writing a long value to an output stream in little-endian format")
    void testToLittleEndianOutputStream() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream)) {
            ByteUtils.toLittleEndian(bufferedOutputStream, -1L, 8);

            byte[] expected = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1};
            assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
        }
    }

    @Test
    @DisplayName("Test writing zero to an output stream in little-endian format with negative length")
    void testToLittleEndianOutputStreamNegativeLength() throws IOException {
        try (PipedOutputStream pipedOutputStream = new PipedOutputStream()) {
            // Negative length should not throw an exception, but doesn't write anything
            ByteUtils.toLittleEndian(pipedOutputStream, 0L, -1);
        }
    }

    @Test
    @DisplayName("Test writing a long value to a byte consumer in little-endian format")
    void testToLittleEndianByteConsumer() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(byteArrayOutputStream);
            ByteUtils.toLittleEndian(byteConsumer, -784L, 4);

            byte[] expected = new byte[]{0, -3, 0, 0};
            assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
        }
    }

    @Test
    @DisplayName("Test writing to a null byte consumer should throw NullPointerException")
    void testToLittleEndianNullByteConsumer() {
        assertThrows(NullPointerException.class, () -> ByteUtils.toLittleEndian(null, 8L, -623));
    }

    @Test
    @DisplayName("Test writing to a byte array in little-endian format")
    void testToLittleEndianByteArray() {
        byte[] byteArray = new byte[8];
        ByteUtils.toLittleEndian(byteArray, 0L, 0, 0);
        assertArrayEquals(new byte[8], byteArray);
    }

    @Test
    @DisplayName("Test reading from an input stream with insufficient data throws IOException")
    void testFromLittleEndianInputStreamInsufficientData() {
        byte[] byteArray = {0x27}; // Single byte, 39 in decimal
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        assertThrows(IOException.class, () -> ByteUtils.fromLittleEndian(byteArrayInputStream, 8),
                "Should throw IOException due to premature end of data");
    }

    @Test
    @DisplayName("Test reading from a DataInput with insufficient data throws EOFException")
    void testFromLittleEndianDataInputInsufficientData() {
        byte[] byteArray = {0x08};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        assertThrows(EOFException.class, () -> ByteUtils.fromLittleEndian(dataInputStream, 8),
                "Should throw EOFException due to premature end of data");
    }

    @Test
    @DisplayName("Test reading from a DataInput with sufficient data")
    void testFromLittleEndianDataInputSufficientData() throws IOException {
        byte[] byteArray = {0, 0};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        long value = ByteUtils.fromLittleEndian(dataInputStream, 2);

        assertEquals(0L, value, "Should return 0");
        assertEquals(0, byteArrayInputStream.available(), "InputStream should be empty");
    }

    @Test
    @DisplayName("Test reading from a null DataInput returns 0")
    void testFromLittleEndianNullDataInput() {
        long value = ByteUtils.fromLittleEndian((DataInput) null, -29);
        assertEquals(0L, value, "Should return 0");
    }

    @Test
    @DisplayName("Test reading from a null ByteSupplier returns 0")
    void testFromLittleEndianNullByteSupplier() {
        long value = ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, -1);
        assertEquals(0L, value, "Should return 0");
    }

    @Test
    @DisplayName("Test reading from a byte array with negative offset returns 0")
    void testFromLittleEndianByteArrayNegativeOffset() {
        byte[] byteArray = new byte[8];
        long value = ByteUtils.fromLittleEndian(byteArray, -181, -1);
        assertEquals(0L, value, "Should return 0");
    }

    @Test
    @DisplayName("Test writing to a byte array with ArrayIndexOutOfBoundsException")
    void testToLittleEndianByteArrayArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[8];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteUtils.toLittleEndian(byteArray, -22L, 0, 51));
    }

    @Test
    @DisplayName("Test reading a single byte from a byte array")
    void testFromLittleEndianByteArraySingleByte() {
        byte[] byteArray = {2, 0, 0, 0, 0, 0, 0, 0, 0};
        long value = ByteUtils.fromLittleEndian(byteArray, 0, 1);
        assertEquals(2L, value, "Should return 2");
    }

    @Test
    @DisplayName("Test reading a three-byte value from a byte array")
    void testFromLittleEndianByteArrayThreeBytes() {
        byte[] byteArray = {0, 0, 46, 0, 0, 0};
        long value = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(3014656L, value, "Should return correct long value");
    }

    @Test
    @DisplayName("Test reading a long value from a byte array with a negative byte")
    void testFromLittleEndianByteArrayNegativeByte() {
        byte[] byteArray = {0, 0, 0, 0, 0, 0, 0, -1};
        long value = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(-72057594037927936L, value, "Should return correct long value");
    }

    @Test
    @DisplayName("Test reading a long value from a ByteSupplier")
    void testFromLittleEndianByteSupplier() throws IOException {
        // Mocking a ByteSupplier which returns a sequence of bytes
        ByteUtils.ByteSupplier byteSupplier = () -> {
            // Returning a sequence of bytes
            int[] bytes = {3148, 3148, 856, 3148, 3148, 3148, 3148, 3148};
            int index = 0;

            return (index < bytes.length) ? bytes[index++] : -1;
        };

        long value = ByteUtils.fromLittleEndian(byteSupplier, 8);
        assertEquals(5497853135745207372L, value, "Should return correct long value");
    }

    @Test
    @DisplayName("Test reading a long value with negative bytes from a ByteSupplier")
    void testFromLittleEndianByteSupplierNegativeBytes() throws IOException {
        ByteUtils.ByteSupplier byteSupplier = () -> {
            // Returning a sequence of bytes with a negative value
            int[] bytes = {2709, 2709, 2709, -1006, 2709, 2709, 2709, 2709};
            int index = 0;

            return (index < bytes.length) ? bytes[index++] : -1;
        };

        long value = ByteUtils.fromLittleEndian(byteSupplier, 8);
        assertEquals(-12438233195L, value, "Should return correct long value");
    }

    @Test
    @DisplayName("Test reading a single byte from an InputStream")
    void testFromLittleEndianInputStreamSingleByte() throws IOException {
        byte[] byteArray = {95, 0, 0, 0, 0, 0};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        long value = ByteUtils.fromLittleEndian(byteArrayInputStream, 1);
        assertEquals(95L, value, "Should return 95");
        assertEquals(5, byteArrayInputStream.available(), "InputStream should have 5 bytes remaining");
    }

    @Test
    @DisplayName("Test reading a single byte from DataInput")
    void testFromLittleEndianDataInputSingleByte() throws IOException {
        byte[] byteArray = {8, 0, 0, 0, 0};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        long value = ByteUtils.fromLittleEndian(dataInputStream, 1);
        assertEquals(8L, value, "Should return 8");
        assertEquals(4, byteArrayInputStream.available(), "InputStream should have 4 bytes remaining");
    }

    @Test
    @DisplayName("Test toLittleEndian with null byte array throws NullPointerException")
    void testToLittleEndianNullByteArray() {
        assertThrows(NullPointerException.class, () -> ByteUtils.toLittleEndian(null, 0L, 182, 17));
    }

    @Test
    @DisplayName("Test toLittleEndian with null byte consumer throws NullPointerException")
    void testToLittleEndianNullByteConsumerException() {
        assertThrows(NullPointerException.class, () -> ByteUtils.toLittleEndian(null, 639L, 128));
    }

    @Test
    @DisplayName("Test toLittleEndian with IOException in ByteConsumer")
    void testToLittleEndianByteConsumerIOException() throws IOException {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(pipedOutputStream);
        assertThrows(IOException.class, () -> ByteUtils.toLittleEndian(byteConsumer, 0L, 2));
    }

    @Test
    @DisplayName("Test toLittleEndian with null output stream throws NullPointerException")
    void testToLittleEndianNullOutputStream() {
        assertThrows(NullPointerException.class, () -> ByteUtils.toLittleEndian((OutputStream) null, 0L, 10));
    }

    @Test
    @DisplayName("Test toLittleEndian with IOException in OutputStream")
    void testToLittleEndianOutputStreamIOException() throws IOException {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        assertThrows(IOException.class, () -> ByteUtils.toLittleEndian(pipedOutputStream, 255L, 2));
    }

    @Test
    @DisplayName("Test fromLittleEndian with null byte array throws NullPointerException")
    void testFromLittleEndianNullByteArrayException() {
        assertThrows(NullPointerException.class, () -> ByteUtils.fromLittleEndian((byte[]) null, 55, 1));
    }

    @Test
    @DisplayName("Test fromLittleEndian with null byte array (no offset) throws NullPointerException")
    void testFromLittleEndianNullByteArrayNoOffsetException() {
        assertThrows(NullPointerException.class, () -> ByteUtils.fromLittleEndian((byte[]) null));
    }

    @Test
    @DisplayName("Test fromLittleEndian with byte array length > 8 throws IllegalArgumentException")
    void testFromLittleEndianByteArrayLengthGreaterThan8() {
        byte[] byteArray = new byte[18];
        assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(byteArray));
    }

    @Test
    @DisplayName("Test fromLittleEndian with ByteSupplier and length > 8 throws IllegalArgumentException")
    void testFromLittleEndianByteSupplierLengthGreaterThan8() {
        ByteUtils.ByteSupplier byteSupplier = () -> 0; // Dummy supplier
        assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(byteSupplier, 190));
    }

    @Test
    @DisplayName("Test fromLittleEndian with InputStream and length > 8 throws IllegalArgumentException")
    void testFromLittleEndianInputStreamLengthGreaterThan8() throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(pipedInputStream, 1273));
    }

    @Test
    @DisplayName("Test fromLittleEndian with ArrayIndexOutOfBoundsException in InputStream")
    void testFromLittleEndianInputStreamArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -5477, 8);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteUtils.fromLittleEndian(byteArrayInputStream, 1));
    }

    @Test
    @DisplayName("Test fromLittleEndian with null DataInput throws NullPointerException")
    void testFromLittleEndianNullDataInputException() {
        assertThrows(NullPointerException.class, () -> ByteUtils.fromLittleEndian((DataInput) null, 1));
    }

    @Test
    @DisplayName("Test fromLittleEndian with DataInput and length > 8 throws IllegalArgumentException")
    void testFromLittleEndianDataInputLengthGreaterThan8() {
        DataInput dataInput = null;
        assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(dataInput, 955));
    }

    @Test
    @DisplayName("Test fromLittleEndian with IOException in DataInput")
    void testFromLittleEndianDataInputIOException() throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        DataInputStream dataInputStream = new DataInputStream(pipedInputStream);
        assertThrows(IOException.class, () -> ByteUtils.fromLittleEndian(dataInputStream, 2));
    }

    @Test
    @DisplayName("Test fromLittleEndian with ArrayIndexOutOfBoundsException in byte array")
    void testFromLittleEndianByteArrayArrayIndexOutOfBoundsException() {
        byte[] byteArray = new byte[1];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteUtils.fromLittleEndian(byteArray, 7, 7));
    }

    @Test
    @DisplayName("Test writing a long value to an output stream in little-endian format (large length)")
    void testToLittleEndianOutputStreamLargeLength() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ByteUtils.toLittleEndian(byteArrayOutputStream, 1196L, 1196);
            assertEquals(1196, byteArrayOutputStream.size());
        }
    }

    @Test
    @DisplayName("Test writing a long value to a DataOutput in little-endian format")
    void testToLittleEndianDataOutput() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new java.io.DataOutputStream(byteArrayOutputStream);
            ByteUtils.toLittleEndian(dataOutput, -114L, 2);
            byte[] expected = new byte[]{-114, -1};
            assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
        }
    }

    @Test
    @DisplayName("Test writing a long value to a DataOutput with a large negative length")
    void testToLittleEndianDataOutputNegativeLength() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new java.io.DataOutputStream(byteArrayOutputStream);
            ByteUtils.toLittleEndian(dataOutput, 4448L, -2147483645);
            assertEquals(0, byteArrayOutputStream.size());
        }
    }

    @Test
    @DisplayName("Test toLittleEndian with negative offset and length")
    void testToLittleEndianNegativeOffsetAndLength() {
        byte[] byteArray = new byte[9];
        ByteUtils.toLittleEndian(byteArray, 0L, -854, -1950);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, byteArray);
    }

    @Test
    @DisplayName("Test fromLittleEndian with null InputStream throws NullPointerException")
    void testFromLittleEndianNullInputStream() {
        assertThrows(NullPointerException.class, () -> ByteUtils.fromLittleEndian((InputStream) null, 8));
    }

    @Test
    @DisplayName("Test fromLittleEndian with negative length InputStream returns 0")
    void testFromLittleEndianNegativeLengthInputStream() throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        long result = ByteUtils.fromLittleEndian(pipedInputStream, -2049);
        assertEquals(0L, result);
    }

    @Test
    @DisplayName("Test fromLittleEndian ByteSupplier throws IOException")
    void testFromLittleEndianByteSupplierThrowsIOException() {
        ByteUtils.ByteSupplier byteSupplier = () -> -1;

        assertThrows(IOException.class, () -> ByteUtils.fromLittleEndian(byteSupplier, 8));
    }

    @Test
    @DisplayName("Test fromLittleEndian with invalid offset and length")
    void testFromLittleEndianInvalidOffsetAndLength() {
        byte[] byteArray = new byte[2];
        assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(byteArray, 4079, 4079));
    }

    @Test
    @DisplayName("Test OutputStreamByteConsumer accept method")
    void testOutputStreamByteConsumerAccept() throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(byteArrayOutputStream);
            byteConsumer.accept(174);
            byte[] expected = new byte[]{-82};
            assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
        }
    }

    @Test
    @DisplayName("Test fromLittleEndian with empty byte array")
    void testFromLittleEndianEmptyByteArray() {
        byte[] byteArray = new byte[5];
        long result = ByteUtils.fromLittleEndian(byteArray);
        assertEquals(0L, result);
    }

    @Test
    @Test
    @DisplayName("Test writing a long value to a file using ByteUtils and OutputStreamByteConsumer")
    void testWriteLongToFile(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("testFile.bin");

        long valueToWrite = 1234567890L;
        int length = 8;

        try (OutputStream fileOutputStream = Files.newOutputStream(tempFile);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            ByteUtils.OutputStreamByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(bufferedOutputStream);
            ByteUtils.toLittleEndian(byteConsumer, valueToWrite, length);
        }

        byte[] writtenBytes = Files.readAllBytes(tempFile);
        byte[] expectedBytes = new byte[]{
                (byte) 0xD2, (byte) 0x02, (byte) 0x96, (byte) 0x49, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        assertArrayEquals(expectedBytes, writtenBytes, "The file should contain the little-endian representation of the long value.");
    }
}