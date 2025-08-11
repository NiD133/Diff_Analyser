package com.fasterxml.jackson.core.io;

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for UTF8Writer class, which converts Java characters to UTF-8 encoded bytes.
 * Tests cover basic writing operations, ASCII handling, surrogate pair processing, and error conditions.
 */
class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Test data constants
    private static final String MIXED_UNICODE_STRING = "AB\u00A0\u1AE9\uFFFC"; // ASCII + non-breaking space + Unicode chars
    private static final String ASCII_WITH_UNICODE = "abcdefghijklmnopqrst\u00A0"; // Mostly ASCII with one Unicode char
    private static final String VALID_EMOJI_SURROGATE_PAIR = "\uD83D\uDE03"; // 😃 emoji as surrogate pair
    private static final byte[] EXPECTED_EMOJI_UTF8_BYTES = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83};

    @Test
    void shouldWriteUnicodeStringUsingDifferentMethods() throws Exception {
        // Given: A UTF8Writer with output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream);
        char[] characters = MIXED_UNICODE_STRING.toCharArray();

        // When: Writing the same string 3 times using different methods
        // Method 1: Write entire string
        writer.write(MIXED_UNICODE_STRING);
        
        // Method 2: Write characters individually and as array slice
        writer.append(characters[0]);           // 'A'
        writer.write(characters[1]);            // 'B' 
        writer.write(characters, 2, 3);         // remaining Unicode chars
        writer.flush();
        
        // Method 3: Write string with offset and length
        writer.write(MIXED_UNICODE_STRING, 0, MIXED_UNICODE_STRING.length());
        writer.close();

        // Then: Output should contain 3 copies of the original string
        byte[] encodedBytes = outputStream.toByteArray();
        String decodedString = utf8String(outputStream);
        
        assertEquals(3 * 10, encodedBytes.length, "UTF-8 encoded bytes should be 30 total");
        assertEquals(15, decodedString.length(), "Decoded string should have 15 characters");
        assertEquals(3 * MIXED_UNICODE_STRING.length(), decodedString.length());
        assertEquals(MIXED_UNICODE_STRING + MIXED_UNICODE_STRING + MIXED_UNICODE_STRING, decodedString);
    }

    @Test
    void shouldHandleAsciiStringWithSingleUnicodeCharacter() throws Exception {
        // Given: A UTF8Writer and a mostly ASCII string with one Unicode character
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream);
        char[] characters = ASCII_WITH_UNICODE.toCharArray();

        // When: Writing the character array
        writer.write(characters, 0, characters.length);
        writer.flush(); // Trigger different code path before close
        writer.close();

        // Then: Output should have correct UTF-8 encoding (ASCII chars = 1 byte, Unicode char = 2 bytes)
        byte[] encodedBytes = outputStream.toByteArray();
        String decodedString = utf8String(outputStream);
        
        assertEquals(characters.length + 1, encodedBytes.length, "Should have one extra byte for 2-byte Unicode char");
        assertEquals(ASCII_WITH_UNICODE, decodedString);
    }

    @Test
    void shouldAllowFlushAndCloseOperationsAfterWriterIsClosed() throws Exception {
        // Given: A UTF8Writer with some data written
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream);

        // When: Writing data and closing the writer
        writer.write('X');
        writer.write(new char[]{'Y'});
        writer.close();
        
        assertEquals(2, outputStream.size(), "Should have written 2 characters");

        // Then: Additional flush and close operations should not throw exceptions
        assertDoesNotThrow(() -> {
            writer.flush();
            writer.close();
            writer.flush(); // Multiple operations should be safe
        });
    }

    @Test
    void shouldCorrectlyEncodeValidSurrogatePairs() throws Exception {
        // Test Case 1: Writing surrogate pair character by character
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream);

        // When: Writing high and low surrogate separately
        writer.write(0xD83D); // High surrogate of 😃 emoji
        writer.write(0xDE03); // Low surrogate of 😃 emoji
        writer.close();

        // Then: Should produce correct 4-byte UTF-8 sequence
        assertEquals(4, outputStream.size(), "Surrogate pair should encode to 4 UTF-8 bytes");
        assertArrayEquals(EXPECTED_EMOJI_UTF8_BYTES, outputStream.toByteArray());

        // Test Case 2: Writing surrogate pair as string
        outputStream = new ByteArrayOutputStream();
        writer = new UTF8Writer(createIOContext(), outputStream);
        
        // When: Writing surrogate pair as complete string
        writer.write(VALID_EMOJI_SURROGATE_PAIR);
        writer.close();

        // Then: Should produce same UTF-8 encoding
        assertEquals(4, outputStream.size(), "String surrogate pair should also encode to 4 UTF-8 bytes");
        assertArrayEquals(EXPECTED_EMOJI_UTF8_BYTES, outputStream.toByteArray());
    }

    @SuppressWarnings("resource")
    @Test
    void shouldThrowExceptionForInvalidSurrogatePairs() throws Exception {
        // Test Case 1: Unmatched low surrogate (missing high surrogate)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream)) {
            writer.write(0xDE03); // Low surrogate without preceding high surrogate
            fail("Should throw IOException for unmatched low surrogate");
        } catch (IOException e) {
            verifyException(e, "Unmatched second part");
        }

        // Test Case 2: High surrogate followed by non-surrogate character
        outputStream = new ByteArrayOutputStream();
        try (UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream)) {
            writer.write(0xD83D); // High surrogate
            writer.write('a');    // Regular character instead of low surrogate
            fail("Should throw IOException for broken surrogate pair");
        } catch (IOException e) {
            verifyException(e, "Broken surrogate pair");
        }

        // Test Case 3: Unmatched low surrogate in string
        outputStream = new ByteArrayOutputStream();
        try (UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream)) {
            writer.write("\uDE03"); // String with lone low surrogate
            fail("Should throw IOException for unmatched low surrogate in string");
        } catch (IOException e) {
            verifyException(e, "Unmatched second part");
        }

        // Test Case 4: Broken surrogate pair in string
        outputStream = new ByteArrayOutputStream();
        try (UTF8Writer writer = new UTF8Writer(createIOContext(), outputStream)) {
            writer.write("\uD83Da"); // High surrogate followed by regular character
            fail("Should throw IOException for broken surrogate pair in string");
        } catch (IOException e) {
            verifyException(e, "Broken surrogate pair");
        }
    }

    /**
     * Validates the mathematical formula used for converting surrogate pairs to Unicode code points.
     * This test addresses issue [core#1218] and ensures the SURROGATE_BASE constant is correct.
     * 
     * @since 2.17
     */
    @Test
    void shouldCorrectlyCalculateUnicodeCodePointsFromSurrogatePairs() {
        // Test all possible surrogate pair combinations
        for (int highSurrogate = UTF8Writer.SURR1_FIRST; highSurrogate <= UTF8Writer.SURR1_LAST; highSurrogate++) {
            for (int lowSurrogate = UTF8Writer.SURR2_FIRST; lowSurrogate <= UTF8Writer.SURR2_LAST; lowSurrogate++) {
                
                // Calculate expected Unicode code point using standard formula
                int expectedCodePoint = 0x10000 + 
                    ((highSurrogate - UTF8Writer.SURR1_FIRST) << 10) + 
                    (lowSurrogate - UTF8Writer.SURR2_FIRST);
                
                // Calculate actual code point using UTF8Writer's optimized formula
                int actualCodePoint = (highSurrogate << 10) + lowSurrogate + UTF8Writer.SURROGATE_BASE;
                
                // Verify the formulas produce identical results
                if (expectedCodePoint != actualCodePoint) {
                    fail(String.format(
                        "Surrogate conversion mismatch for high=0x%X, low=0x%X: expected=%d, actual=%d",
                        highSurrogate, lowSurrogate, expectedCodePoint, actualCodePoint
                    ));
                }
            }
        }
    }

    /**
     * Creates an IOContext for testing purposes.
     */
    private IOContext createIOContext() {
        return testIOContext();
    }
}