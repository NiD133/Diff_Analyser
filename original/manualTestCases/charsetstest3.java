package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Charsets} utility class, focusing on the {@code toCharset} methods.
 * <p>
 * This class aims to provide clear and understandable test cases for converting various inputs
 * (String, Charset, null) into a Charset object using the {@code Charsets.toCharset} method.
 */
public class CharsetsTest { // Renamed class for clarity

    @Test
    public void testToCharset_String() {
        // Test case 1: Null String input - Should return the default charset.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null),
                "Should return default charset when input String is null.");

        // Test case 2: Null Charset input - Should return the default charset.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null),
                "Should return default charset when input Charset is null.");

        // Test case 3: Default Charset input - Should return the same default charset.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()),
                "Should return the same default charset when input is the default charset.");

        // Test case 4: StandardCharsets.UTF_8 input - Should return StandardCharsets.UTF_8.
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8),
                "Should return UTF-8 when input is StandardCharsets.UTF_8.");
    }
}