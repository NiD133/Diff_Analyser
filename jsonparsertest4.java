package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

  @Test
  public void testParseString_withWhitespaceValue() {
    // Arrange
    // A JSON string literal whose value consists of only whitespace characters.
    String json = "\"   \"";

    // Act
    JsonElement result = JsonParser.parseString(json);

    // Assert
    assertThat(result.isJsonPrimitive()).isTrue();
    assertThat(result.getAsString()).isEqualTo("   ");
  }
}