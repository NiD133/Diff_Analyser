package com.fasterxml.jackson.core.io;

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void testWritingMultipleTimes() throws Exception {
        // Setup: Create a ByteArrayOutputStream and a UTF8Writer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), outputStream);

        // Test data: A string with a mix of ASCII and non-ASCII characters
        String testString = "AB\u00A0\u1AE9\uFFFC";
        char[] charArray = testString.toCharArray();

        // Action: Write the string multiple times using different methods
        writer.write(testString);
        writer.append(charArray[0]);
        writer.write(charArray[1]);
        writer.write(charArray, 2, 3);
        writer.flush();

        writer.write(testString, 0, testString.length());
        writer.close();

        // Verification: Check that the output contains the string written three times
        byte[] outputData = outputStream.toByteArray();
        assertEquals(3 * 10, outputData.length); // 10 bytes per string
        String actualOutput = utf8String(outputStream);
        assertEquals(3 * testString.length(), actualOutput.length());
        assertEquals(testString + testString + testString, actualOutput);
    }

    @Test
    void testWritingAsciiCharacters() throws Exception {
        // Setup: Create a ByteArrayOutputStream and a UTF8Writer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), outputStream);

        // Test data: A string with ASCII characters and one non-ASCII character
        String asciiString = "abcdefghijklmnopqrst\u00A0";
        char[] charArray = asciiString.toCharArray();

        // Action: Write the entire character array and flush
        writer.write(charArray, 0, charArray.length);
        writer.flush();
        writer.close();

        // Verification: Check that the output length matches the input length
        byte[] outputData = outputStream.toByteArray();
        assertEquals(charArray.length + 1, outputData.length); // +1 for the non-ASCII character
        String actualOutput = utf8String(outputStream);
        assertEquals(asciiString, actualOutput);
    }

    @Test
    void testFlushAfterClose() throws Exception {
        // Setup: Create a ByteArrayOutputStream and a UTF8Writer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), outputStream);

        // Action: Write characters and close the writer
        writer.write('X');
        char[] charArray = { 'Y' };
        writer.write(charArray);
        writer.close();

        // Verification: Ensure the output size is correct and flushing after close does not throw an exception
        assertEquals(2, outputStream.size());
        writer.flush(); // Should not throw an exception
        writer.close(); // Should not throw an exception
        writer.flush(); // Should not throw an exception
    }

    @Test
    void testValidSurrogatePairs() throws Exception {
        // Setup: Create a ByteArrayOutputStream and a UTF8Writer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), outputStream);

        // Action: Write valid surrogate pairs character by character
        writer.write(0xD83D);
        writer.write(0xDE03);
        writer.close();

        // Verification: Ensure the output matches the expected byte sequence
        assertEquals(4, outputStream.size());
        final byte[] expectedSurrogates = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83 };
        assertArrayEquals(expectedSurrogates, outputStream.toByteArray());

        // Test writing surrogate pairs as a string
        outputStream = new ByteArrayOutputStream();
        writer = new UTF8Writer(_ioContext(), outputStream);
        writer.write("\uD83D\uDE03");
        writer.close();
        assertEquals(4, outputStream.size());
        assertArrayEquals(expectedSurrogates, outputStream.toByteArray());
    }

    @SuppressWarnings("resource")
    @Test
    void testInvalidSurrogatePairs() throws Exception {
        // Test various invalid surrogate pair scenarios

        // Unmatched second part
        assertThrows(IOException.class, () -> {
            try (UTF8Writer writer = new UTF8Writer(_ioContext(), new ByteArrayOutputStream())) {
                writer.write(0xDE03);
            }
        }, "Unmatched second part");

        // Broken surrogate pair
        assertThrows(IOException.class, () -> {
            try (UTF8Writer writer = new UTF8Writer(_ioContext(), new ByteArrayOutputStream())) {
                writer.write(0xD83D);
                writer.write('a');
            }
        }, "Broken surrogate pair");

        // Unmatched second part in string
        assertThrows(IOException.class, () -> {
            try (UTF8Writer writer = new UTF8Writer(_ioContext(), new ByteArrayOutputStream())) {
                writer.write("\uDE03");
            }
        }, "Unmatched second part");

        // Broken surrogate pair in string
        assertThrows(IOException.class, () -> {
            try (UTF8Writer writer = new UTF8Writer(_ioContext(), new ByteArrayOutputStream())) {
                writer.write("\uD83Da");
            }
        }, "Broken surrogate pair");
    }

    // For [core#1218]
    // @since 2.17
    @Test
    void testSurrogateConversion() {
        // Test conversion of surrogate pairs to Unicode code points
        for (int first = UTF8Writer.SURR1_FIRST; first <= UTF8Writer.SURR1_LAST; first++) {
            for (int second = UTF8Writer.SURR2_FIRST; second <= UTF8Writer.SURR2_LAST; second++) {
                int expectedCodePoint = 0x10000 + ((first - UTF8Writer.SURR1_FIRST) << 10) + (second - UTF8Writer.SURR2_FIRST);
                int actualCodePoint = (first << 10) + second + UTF8Writer.SURROGATE_BASE;
                assertEquals(expectedCodePoint, actualCodePoint, 
                    String.format("Mismatch on: %s %s; expected: %d, actual: %d",
                        Integer.toHexString(first), Integer.toHexString(second), expectedCodePoint, actualCodePoint));
            }
        }
    }

    private IOContext _ioContext() {
        return testIOContext();
    }
}