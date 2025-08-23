package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

  /**
   * Verifies that parsing a string containing only whitespace results in a {@link JsonNull} object.
   * This is because the parser operates in lenient mode by default.
   */
  @Test
  public void parseString_whitespaceOnly_returnsJsonNull() {
    // Arrange
    String jsonWithOnlyWhitespace = "     ";

    // Act
    JsonElement result = JsonParser.parseString(jsonWithOnlyWhitespace);

    // Assert
    assertThat(result).isEqualTo(JsonNull.INSTANCE);
  }
}