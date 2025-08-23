package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

    @Test
    public void testParseString_unquotedString() {
        // JsonParser.parseString() uses a lenient parser by default, which allows unquoted strings.
        // A strict parser would throw a MalformedJsonException.
        String unquotedString = "Test";
        JsonElement element = JsonParser.parseString(unquotedString);

        assertThat(element.isJsonPrimitive()).isTrue();
        assertThat(element.getAsString()).isEqualTo(unquotedString);
    }
}