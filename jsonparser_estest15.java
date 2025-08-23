package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class, focusing on handling of invalid arguments.
 */
public class JsonParserTest {

    /**
     * Verifies that calling {@link JsonParser#parseReader(JsonReader)} with a null
     * argument correctly throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void parseReader_withNullJsonReader_throwsNullPointerException() {
        // The method under test is called with a null JsonReader.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        JsonParser.parseReader((JsonReader) null);
    }
}