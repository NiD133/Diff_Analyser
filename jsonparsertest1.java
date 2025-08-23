package com.google.gson;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link JsonParser}.
 */
public class JsonParserTest {

  @Test
  public void parseString_withMalformedJson_throwsJsonSyntaxException() {
    // This JSON string is malformed because the inner array is not closed.
    String malformedJson = "[[]";

    // Expect a JsonSyntaxException when trying to parse malformed JSON.
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(malformedJson));
  }
}