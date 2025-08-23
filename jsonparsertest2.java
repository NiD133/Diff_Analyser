package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonParser}.
 */
public class JsonParserTest {

  /**
   * Tests that {@link JsonParser#parseString(String)} successfully parses an array with unquoted
   * string values. This is the expected behavior because the parser operates in a lenient mode by
   * default, which permits this syntax.
   */
  @Test
  public void testParseString_parsesArrayWithUnquotedStrings() {
    // Arrange
    String jsonWithUnquotedStrings = "[a,b,c]";
    JsonArray expectedArray = new JsonArray();
    expectedArray.add("a");
    expectedArray.add("b");
    expectedArray.add("c");

    // Act
    JsonElement parsedElement = JsonParser.parseString(jsonWithUnquotedStrings);

    // Assert
    assertThat(parsedElement).isEqualTo(expectedArray);
  }
}