package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

  @Test
  public void parseString_withValidLenientJson_returnsEquivalentJsonObject() {
    // Arrange
    // JsonParser uses lenient parsing by default, so unquoted property names
    // and single-quoted string values are permitted.
    String json = "{a:10,b:'c'}";

    JsonObject expectedObject = new JsonObject();
    expectedObject.addProperty("a", 10);
    expectedObject.addProperty("b", "c");

    // Act
    JsonElement parsedElement = JsonParser.parseString(json);

    // Assert
    assertThat(parsedElement).isEqualTo(expectedObject);
  }
}