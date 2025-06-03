package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Test class for {@link Charsets} focusing on error handling.  This provides
 * more readable tests by explaining the purpose of each test.
 */
class CharsetsTest {

    /**
     * Tests that an {@link IllegalCharsetNameException} is thrown when attempting to
     * convert an invalid charset name to a Charset using {@link Charsets#toCharset(String, Charset)}.
     * Specifically, it tests the case where the charset name "Tv&)_" is used, which is
     * not a valid charset name according to the Java specification. The UTF-8 charset
     * is provided as a default charset in case the input string *was* a valid charset name.
     */
    @Test
    void testToCharsetWithInvalidCharsetNameThrowsIllegalCharsetNameException() {
        Charset defaultCharset = Charsets.UTF_8;
        String invalidCharsetName = "Tv&)_";

        Exception exception = assertThrows(IllegalCharsetNameException.class, () -> {
            Charsets.toCharset(invalidCharsetName, defaultCharset);
        });

        // Optional: Verify the message of the exception, if it is important to your testing
        // assertEquals("Tv&)_", exception.getMessage()); // The message might vary depending on the JVM
    }
}