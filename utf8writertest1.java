package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on its various write methods.
 */
public class UTF8WriterTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    /**
     * This test verifies that multiple calls to different 'write' and 'append' methods
     * produce a correct, concatenated UTF-8 byte sequence.
     */
    @Test
    void shouldCorrectlyEncodeAndCombineWrites() throws IOException {
        // Arrange
        // A test string with a mix of 1, 2, and 3-byte UTF-8 characters.
        // A(1) B(1) \u00A0(2) \u1AE9(3) \uFFFC(3) -> 10 bytes total.
        final String TEST_STRING = "AB\u00A0\u1AE9\uFFFC";
        final int EXPECTED_BYTES_PER_STRING = 10;
        final int REPETITIONS = 3;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(_ioContext(), outputStream);
        char[] testChars = TEST_STRING.toCharArray();

        // Act: Write the same string 3 times using a variety of methods to test different code paths.
        
        // 1. Write the full string using write(String)
        utf8Writer.write(TEST_STRING);

        // 2. Reconstruct the string using append(char), write(char), and write(char[], int, int)
        utf8Writer.append(testChars[0]);
        utf8Writer.write(testChars[1]);
        utf8Writer.write(testChars, 2, 3);

        // Explicitly flush the buffer before the next write
        utf8Writer.flush();

        // 3. Write the full string again using write(String, int, int)
        utf8Writer.write(TEST_STRING, 0, TEST_STRING.length());

        // Close the writer, which should also flush any remaining buffered data
        utf8Writer.close();

        // Assert
        String expectedString = TEST_STRING.repeat(REPETITIONS);
        byte[] actualBytes = outputStream.toByteArray();
        String actualString = utf8String(outputStream); // Helper from JUnit5TestBase

        // Verify the total byte count is correct
        assertEquals(REPETITIONS * EXPECTED_BYTES_PER_STRING, actualBytes.length,
                "The total number of bytes written should match the expected UTF-8 encoding length.");

        // Verify the final string content is correct
        assertEquals(expectedString, actualString,
                "The final decoded string should be three concatenated copies of the original.");
    }
}