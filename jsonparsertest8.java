package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Test for {@link JsonParser}.
 */
public class JsonParserTest {

  /**
   * Tests that parsing a JSON string representing an array with mixed element types
   * (object, number, string) results in a correct {@link JsonArray}.
   */
  @Test
  public void parseString_withMixedTypeArray_parsesCorrectly() {
    // Arrange
    String json = "[{}, 13, \"stringValue\"]";

    JsonArray expectedArray = new JsonArray();
    expectedArray.add(new JsonObject());
    expectedArray.add(13);
    expectedArray.add("stringValue");

    // Act
    JsonElement parsedElement = JsonParser.parseString(json);

    // Assert
    assertThat(parsedElement).isEqualTo(expectedArray);
  }
}