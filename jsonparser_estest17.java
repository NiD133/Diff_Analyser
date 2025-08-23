package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Verifies that parsing a null string results in a NullPointerException.
     *
     * The underlying implementation uses a `java.io.StringReader`, which
     * throws a NullPointerException when initialized with a null string.
     * This test ensures that this behavior is handled correctly.
     */
    @Test(expected = NullPointerException.class)
    public void parseString_whenInputIsNull_throwsNullPointerException() {
        // The static parseString method is the modern, recommended API.
        // Calling it with a null input is expected to throw.
        JsonParser.parseString(null);
    }
}