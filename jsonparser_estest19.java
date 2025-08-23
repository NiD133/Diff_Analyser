package com.google.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Reader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for the {@link JsonParser} class, focusing on edge cases and error handling.
 */
class JsonParserTest {

    @Test
    @DisplayName("Parsing from a null Reader should throw NullPointerException")
    void parseReader_withNullReader_throwsNullPointerException() {
        // The method under test is the static `JsonParser.parseReader(Reader)`.
        // This is the modern, recommended replacement for the deprecated instance method `parse(Reader)`.
        
        // We expect a NullPointerException because the underlying JsonReader
        // constructor does not accept a null Reader argument.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> JsonParser.parseReader((Reader) null)
        );

        // Verify that the exception message is clear and helpful,
        // originating from the null check.
        assertEquals("in == null", exception.getMessage());
    }
}