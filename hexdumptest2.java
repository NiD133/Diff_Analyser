package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HexDump#dump(byte[], long, java.io.OutputStream, int)}.
 */
public class HexDumpOutputStreamTest {

    /**
     * The data to be dumped in tests. Contains all possible byte values (0-255).
     */
    private byte[] testData;

    @BeforeEach
    void setUp() {
        // Initialize testData with bytes 0x00 through 0xFF
        testData = new byte[256];
        for (int i = 0; i < 256; i++) {
            testData[i] = (byte) i;
        }
    }

    /**
     * Tests dumping the full data array with a zero offset.
     */
    @Test
    void testDumpWithZeroOffset() throws IOException {
        // Arrange
        final long offset = 0;
        final int index = 0;
        final String expected = buildExpectedHexDump(testData, offset, index, testData.length);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Act
        HexDump.dump(testData, offset, stream, index);

        // Assert
        assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), stream.toByteArray());
    }

    /**
     * Tests dumping with a large positive offset.
     */
    @Test
    void testDumpWithPositiveOffset() throws IOException {
        // Arrange
        final long offset = 0x10000000L;
        final int index = 0;
        final String expected = buildExpectedHexDump(testData, offset, index, testData.length);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Act
        HexDump.dump(testData, offset, stream, index);

        // Assert
        assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), stream.toByteArray());
    }

    /**
     * The dump method formats the long offset as a 32-bit integer. This test verifies
     * that a long value which would be negative if treated as an int is handled correctly.
     */
    @Test
    void testDumpWithLargeOffsetThatWraps() throws IOException {
        // Arrange
        final long offset = 0xFF000000L;
        final int index = 0;
        final String expected = buildExpectedHexDump(testData, offset, index, testData.length);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Act
        HexDump.dump(testData, offset, stream, index);

        // Assert
        assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), stream.toByteArray());
    }

    /**
     * Tests dumping a slice of the data array starting from a non-zero index.
     */
    @Test
    void testDumpWithNonZeroStartIndex() throws IOException {
        // Arrange
        final long offset = 0x10000000L;
        final int index = 0x81;
        final int length = testData.length - index;
        final String expected = buildExpectedHexDump(testData, offset, index, length);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Act
        HexDump.dump(testData, offset, stream, index);

        // Assert
        assertArrayEquals(expected.getBytes(StandardCharsets.UTF_8), stream.toByteArray());
    }

    @Test
    void testDumpWithNegativeIndexThrowsException() {
        // Act & Assert
        assertThrows(ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testData, 0, new ByteArrayOutputStream(), -1));
    }

    @Test
    void testDumpWithIndexTooLargeThrowsException() {
        // Act & Assert
        assertThrows(ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testData, 0, new ByteArrayOutputStream(), testData.length));
    }

    @Test
    void testDumpWithNullStreamThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> HexDump.dump(testData, 0, null, 0));
    }

    /**
     * Verifies that the dump method does not close the output stream it writes to.
     */
    @Test
    void testDumpDoesNotCloseOutputStream() {
        // Arrange
        final ThrowOnCloseOutputStream stream = new ThrowOnCloseOutputStream(new ByteArrayOutputStream());

        // Act & Assert
        // The test will fail with an IOException if dump closes the stream.
        assertDoesNotThrow(() -> HexDump.dump(testData, 0, stream, 0));
    }

    /**
     * Helper method to build the expected hex dump string, matching the format produced by {@link HexDump}.
     * This makes the tests much clearer by abstracting away the complex and repetitive string construction.
     *
     * @param data          The byte data to dump.
     * @param displayOffset The offset to display at the start of the first line.
     * @param startIndex    The starting index within the data array.
     * @param length        The number of bytes to dump.
     * @return A string containing the formatted hex dump.
     */
    private String buildExpectedHexDump(final byte[] data, final long displayOffset, final int startIndex, final int length) {
        final StringBuilder expected = new StringBuilder();
        final String eol = System.lineSeparator();
        final int end = startIndex + length;

        for (int i = startIndex; i < end; i += 16) {
            final int lineStartIndex = i;
            final long currentLineOffset = displayOffset + lineStartIndex;
            // The dump method formats the long offset as 8 hex characters (i.e., a 32-bit int).
            expected.append(String.format("%08X ", (int) currentLineOffset));

            // Append hex representation of bytes for the current line
            for (int j = 0; j < 16; j++) {
                final int currentIndex = lineStartIndex + j;
                if (currentIndex < end) {
                    expected.append(String.format("%02X ", data[currentIndex]));
                } else {
                    expected.append("   "); // Pad if line is incomplete
                }
            }

            // Append ASCII representation of bytes for the current line
            for (int j = 0; j < 16; j++) {
                final int currentIndex = lineStartIndex + j;
                if (currentIndex < end) {
                    final int value = data[currentIndex] & 0xFF; // Treat byte as unsigned
                    final char c = value >= 32 && value <= 126 ? (char) value : '.';
                    expected.append(c);
                }
            }
            expected.append(eol);
        }
        return expected.toString();
    }
}