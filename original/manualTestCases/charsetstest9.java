package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test class validates the behavior of the {@link Charsets} class
 * in the Apache Commons IO library.  Specifically, it focuses on
 * confirming that the {@link Charsets#UTF_8} constant is equivalent
 * to the standard {@link StandardCharsets#UTF_8} charset.
 */
public class CharsetsTest { // Renamed class for better clarity

    /**
     * Tests that the {@link Charsets#UTF_8} constant is equivalent to
     * {@link StandardCharsets#UTF_8} by comparing their names.
     *
     * This ensures that the Commons IO library provides a correct
     * and consistent representation of the UTF-8 character set.
     */
    @Test
    public void testUtf8Charset() { // Renamed method for clarity
        // Assert that the name of the Charsets.UTF_8 charset is the same as StandardCharsets.UTF_8
        assertEquals(StandardCharsets.UTF_8.name(), Charsets.UTF_8.name(),
                "Charsets.UTF_8 should be equivalent to StandardCharsets.UTF_8");
    }
}