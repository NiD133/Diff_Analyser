package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    private static final String TEST_STRING = "AB\u00A0\u1AE9\uFFFC";
    private static final String ASCII_STRING = "abcdefghijklmnopqrst\u00A0";
    private static final String SURROGATE_STRING = "\uD83D\uDE03";
    private static final byte[] EXPECTED_SURROGATE_BYTES = {
        (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83
    };

    private UTF8Writer buildWriter(ByteArrayOutputStream out) {
        return new UTF8Writer(_ioContext(), out);
    }

    @Nested
    @DisplayName("Basic Write Operations")
    class BasicWriteOperations {
        @Test
        void writeUsingMultipleMethods() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                // Write using different method variants
                w.write(TEST_STRING);               // String write
                w.append(TEST_STRING.charAt(0));    // Append char
                w.write(TEST_STRING.charAt(1));     // Single char write
                w.write(TEST_STRING.toCharArray(), 2, 3);  // Char array segment
                w.write(TEST_STRING, 0, TEST_STRING.length());  // String segment
            }

            // Verify combined output
            String result = out.toString("UTF-8");
            assertEquals(TEST_STRING.length() * 3, result.length());
            assertEquals(TEST_STRING + TEST_STRING + TEST_STRING, result);
        }

        @Test
        void writeAsciiWithNonAsciiCharacter() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(ASCII_STRING.toCharArray());
            }

            // ASCII characters: 1 byte, non-ASCII: 2 bytes (total = 21 chars + 1 extra byte)
            byte[] data = out.toByteArray();
            assertEquals(ASCII_STRING.length() + 1, data.length);
            assertEquals(ASCII_STRING, out.toString("UTF-8"));
        }
    }

    @Nested
    @DisplayName("Special Case Handling")
    class SpecialCaseHandling {
        @Test
        void flushAndCloseAfterWriterClosed() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            UTF8Writer w = buildWriter(out);
            
            // Write some data and close normally
            w.write('X');
            w.write(new char[]{'Y'});
            w.close();
            assertEquals(2, out.size());
            
            // Verify post-close operations are safe
            w.flush();
            w.close();
        }

        @Test
        void flushBeforeCloseTriggersDifferentCodePath() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(ASCII_STRING);
                w.flush();  // Explicit flush before close
            }
            assertEquals(ASCII_STRING, out.toString("UTF-8"));
        }
    }

    @Nested
    @DisplayName("Surrogate Pair Handling")
    class SurrogatePairHandling {
        @Test
        void validSurrogatePairWrittenAsChars() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(0xD83D);  // High surrogate
                w.write(0xDE03);  // Low surrogate
            }
            assertArrayEquals(EXPECTED_SURROGATE_BYTES, out.toByteArray());
        }

        @Test
        void validSurrogatePairWrittenAsString() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(SURROGATE_STRING);
            }
            assertArrayEquals(EXPECTED_SURROGATE_BYTES, out.toByteArray());
        }

        @Test
        void unpairedLowSurrogateThrowsException() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(0xDE03);  // Unpaired low surrogate
                fail("Should throw IOException for unpaired low surrogate");
            } catch (IOException e) {
                verifyException(e, "Unmatched second part");
            }
        }

        @Test
        void brokenSurrogatePairThrowsException() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write(0xD83D);  // High surrogate
                w.write('a');     // Non-surrogate character breaks pair
                fail("Should throw IOException for broken surrogate pair");
            } catch (IOException e) {
                verifyException(e, "Broken surrogate pair");
            }
        }

        @Test
        void unpairedLowSurrogateStringThrowsException() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write("\uDE03");  // Unpaired low surrogate in string
                fail("Should throw IOException for unpaired low surrogate");
            } catch (IOException e) {
                verifyException(e, "Unmatched second part");
            }
        }

        @Test
        void brokenSurrogatePairStringThrowsException() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer w = buildWriter(out)) {
                w.write("\uD83Da");  // High surrogate followed by non-surrogate
                fail("Should throw IOException for broken surrogate pair");
            } catch (IOException e) {
                verifyException(e, "Broken surrogate pair");
            }
        }
    }

    @Test
    @DisplayName("Verify Surrogate Conversion Logic")
    void surrogateConversion() {
        for (int first = UTF8Writer.SURR1_FIRST; first <= UTF8Writer.SURR1_LAST; first++) {
            for (int second = UTF8Writer.SURR2_FIRST; second <= UTF8Writer.SURR2_LAST; second++) {
                int expected = 0x10000 + ((first - UTF8Writer.SURR1_FIRST) << 10)
                    + (second - UTF8Writer.SURR2_FIRST);
                int actual = (first << 10) + second + UTF8Writer.SURROGATE_BASE;
                
                if (expected != actual) {
                    fail(String.format(
                        "Mismatch for surrogates U+%04X U+%04X: expected 0x%X, actual 0x%X",
                        first, second, expected, actual
                    ));
                }
            }
        }
    }

    private IOContext _ioContext() {
        return testIOContext();
    }
}