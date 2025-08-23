package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class.
 * This class focuses on testing the handling of invalid or null inputs.
 */
public class JsonParserTest {

    /**
     * Verifies that the deprecated instance method {@code parse(JsonReader)} throws a
     * {@code NullPointerException} when the provided {@code JsonReader} is null.
     *
     * This test ensures that the method's contract is upheld, even for deprecated APIs,
     * to maintain backward compatibility.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullJsonReaderThrowsNullPointerException() {
        JsonParser parser = new JsonParser();
        JsonReader nullReader = null;

        // The JsonParser() constructor and the parse(JsonReader) instance method are deprecated.
        // The modern static equivalent is JsonParser.parseReader(JsonReader).
        // We are calling the deprecated method here to ensure it fails as expected.
        parser.parse(nullReader);
    }
}