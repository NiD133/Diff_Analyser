package org.apache.commons.compress.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    // A sample long value used across multiple tests.
    // In little-endian, this is represented as the byte array {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}.
    private static final long TEST_VALUE = 0x0807060504030201L;
    private static final byte[] TEST_VALUE_BYTES = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

    @Nested
    @DisplayName("fromLittleEndian methods")
    class FromLittleEndianTests {

        @Test
        void shouldReadLongFromByteArray() {
            assertEquals(TEST_VALUE, ByteUtils.fromLittleEndian(TEST_VALUE_BYTES));
        }

        @Test
        void shouldReadLongFromByteArraySlice() {
            // Reads 4 bytes (0x04030201) starting from offset 1
            final byte[] source = {0x00, 0x01, 0x02, 0x03, 0x04, 0x00};
            final long expected = 0x04030201L;
            assertEquals(expected, ByteUtils.fromLittleEndian(source, 1, 4));
        }

        @Test
        void shouldReturnZeroForZeroLengthReadFromArray() {
            assertEquals(0L, ByteUtils.fromLittleEndian(TEST_VALUE_BYTES, 1, 0));
        }

        @Test
        void shouldThrowExceptionForLengthTooLongFromArray() {
            final byte[] bytes = new byte[8];
            final Exception e = assertThrows(IllegalArgumentException.class, () -> ByteUtils.fromLittleEndian(bytes, 0, 9));
            assertEquals("Can't read more than eight bytes into a long value", e.getMessage());
        }

        @Test
        void shouldThrowExceptionForOutOfBoundsReadFromArray() {
            final byte[] bytes = new byte[8];
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteUtils.fromLittleEndian(bytes, 4, 5));
        }

        @Test
        void shouldReadLongFromInputStream() throws IOException {
            final InputStream is = new ByteArrayInputStream(TEST_VALUE_BYTES);
            assertEquals(TEST_VALUE, ByteUtils.fromLittleEndian(is, 8));
        }

        @Test
        void shouldThrowExceptionForPrematureEofFromInputStream() {
            final InputStream is = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
            final Exception e = assertThrows(IOException.class, () -> ByteUtils.fromLittleEndian(is, 4));
            assertEquals("Premature end of data", e.getMessage());
        }

        @Test
        void shouldReadLongFromDataInput() throws IOException {
            final DataInput di = new DataInputStream(new ByteArrayInputStream(TEST_VALUE_BYTES));
            assertEquals(TEST_VALUE, ByteUtils.fromLittleEndian(di, 8));
        }

        @Test
        void shouldThrowExceptionForPrematureEofFromDataInput() {
            final DataInput di = new DataInputStream(new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
            assertThrows(EOFException.class, () -> ByteUtils.fromLittleEndian(di, 4));
        }

        @Test
        void shouldReadLongFromByteSupplier() throws IOException {
            final ByteUtils.ByteSupplier supplier = new ArrayByteSupplier(TEST_VALUE_BYTES);
            assertEquals(TEST_VALUE, ByteUtils.fromLittleEndian(supplier, 8));
        }

        @Test
        void shouldThrowExceptionForPrematureEofFromByteSupplier() {
            final ByteUtils.ByteSupplier supplier = new ArrayByteSupplier(new byte[]{0x01, 0x02, 0x03});
            final Exception e = assertThrows(IOException.class, () -> ByteUtils.fromLittleEndian(supplier, 4));
            assertEquals("Premature end of data", e.getMessage());
        }
    }

    @Nested
    @DisplayName("toLittleEndian methods")
    class ToLittleEndianTests {

        @Test
        void shouldWriteLongToByteArray() {
            final byte[] actual = new byte[8];
            ByteUtils.toLittleEndian(actual, TEST_VALUE, 0, 8);
            assertArrayEquals(TEST_VALUE_BYTES, actual);
        }

        @Test
        void shouldWriteLongToByteArraySlice() {
            final byte[] actual = new byte[6];
            final byte[] expected = {0x00, 0x01, 0x02, 0x03, 0x04, 0x00};
            // Writes 4 bytes of TEST_VALUE into the middle of the array
            ByteUtils.toLittleEndian(actual, TEST_VALUE, 1, 4);
            assertArrayEquals(expected, actual);
        }

        @Test
        void shouldDoNothingForZeroLengthWriteToArray() {
            final byte[] actual = new byte[8];
            final byte[] original = Arrays.copyOf(actual, actual.length);
            ByteUtils.toLittleEndian(actual, TEST_VALUE, 1, 0);
            assertArrayEquals(original, actual);
        }

        @Test
        void shouldThrowExceptionForOutOfBoundsWriteToArray() {
            final byte[] bytes = new byte[8];
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> ByteUtils.toLittleEndian(bytes, TEST_VALUE, 4, 5));
        }

        @Test
        void shouldWriteLongToOutputStream() throws IOException {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteUtils.toLittleEndian((OutputStream) bos, TEST_VALUE, 8);
            assertArrayEquals(TEST_VALUE_BYTES, bos.toByteArray());
        }

        @Test
        void shouldWriteLongToDataOutput() throws IOException {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final MyDataOutput dos = new MyDataOutput(bos);
            ByteUtils.toLittleEndian(dos, TEST_VALUE, 8);
            assertArrayEquals(TEST_VALUE_BYTES, bos.toByteArray());
        }

        @Test
        void shouldWriteLongToByteConsumer() throws IOException {
            final ListByteConsumer consumer = new ListByteConsumer();
            ByteUtils.toLittleEndian(consumer, TEST_VALUE, 8);
            assertArrayEquals(TEST_VALUE_BYTES, consumer.toByteArray());
        }

        @Test
        void shouldWriteNegativeLongToByteConsumer() throws IOException {
            final ListByteConsumer consumer = new ListByteConsumer();
            // -2 in little-endian is {0xFE, 0xFF, 0xFF, ...}
            final byte[] expected = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            ByteUtils.toLittleEndian(consumer, -2L, 4);
            assertArrayEquals(expected, consumer.toByteArray());
        }
    }

    @Nested
    @DisplayName("Helper classes")
    class HelperClassTests {

        @Test
        void outputStreamByteConsumerShouldWriteByte() throws IOException {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ByteUtils.OutputStreamByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(bos);
            consumer.accept(0x41); // 'A'
            consumer.accept(0x42); // 'B'
            assertArrayEquals(new byte[]{0x41, 0x42}, bos.toByteArray());
        }
    }

    /**
     * A simple implementation of {@link ByteUtils.ByteSupplier} backed by a byte array for predictable testing.
     */
    private static class ArrayByteSupplier implements ByteUtils.ByteSupplier {
        private final byte[] data;
        private int pos = 0;

        ArrayByteSupplier(final byte[] data) {
            this.data = data;
        }

        @Override
        public int getAsByte() {
            if (pos >= data.length) {
                return -1; // EOF
            }
            return data[pos++] & 0xFF; // Return as unsigned byte
        }
    }

    /**
     * A simple implementation of {@link ByteUtils.ByteConsumer} that stores bytes in a list for easy verification.
     */
    private static class ListByteConsumer implements ByteUtils.ByteConsumer {
        private final List<Byte> bytes = new ArrayList<>();

        @Override
        public void accept(final int b) {
            bytes.add((byte) b);
        }

        public byte[] toByteArray() {
            final byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                result[i] = bytes.get(i);
            }
            return result;
        }
    }

    /**
     * A minimal {@link java.io.DataOutput} implementation for testing, delegating to an {@link OutputStream}.
     */
    private static class MyDataOutput implements java.io.DataOutput {
        private final OutputStream out;

        MyDataOutput(final OutputStream out) {
            this.out = out;
        }

        @Override
        public void write(final int b) throws IOException {
            out.write(b);
        }

        // Other DataOutput methods are not needed for this test and are left unimplemented.
        @Override public void write(final byte[] b) { /* not used */ }
        @Override public void write(final byte[] b, final int off, final int len) { /* not used */ }
        @Override public void writeBoolean(final boolean v) { /* not used */ }
        @Override public void writeByte(final int v) { /* not used */ }
        @Override public void writeShort(final int v) { /* not used */ }
        @Override public void writeChar(final int v) { /* not used */ }
        @Override public void writeInt(final int v) { /* not used */ }
        @Override public void writeLong(final long v) { /* not used */ }
        @Override public void writeFloat(final float v) { /* not used */ }
        @Override public void writeDouble(final double v) { /* not used */ }
        @Override public void writeBytes(final String s) { /* not used */ }
        @Override public void writeChars(final String s) { /* not used */ }
        @Override public void writeUTF(final String s) { /* not used */ }
    }
}