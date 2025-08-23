package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on adding elements.
 */
public class JsonArrayTest {

  @Test
  public void addString_withNullValue_addsJsonNull() {
    // Arrange
    JsonArray actualArray = new JsonArray();

    // Act
    actualArray.add("Hello");
    actualArray.add("Goodbye");
    actualArray.add("Thank you");
    actualArray.add((String) null); // This should be converted to JsonNull
    actualArray.add("Yes");

    // Assert
    JsonArray expectedArray = new JsonArray();
    expectedArray.add("Hello");
    expectedArray.add("Goodbye");
    expectedArray.add("Thank you");
    expectedArray.add(JsonNull.INSTANCE);
    expectedArray.add("Yes");

    assertThat(actualArray).isEqualTo(expectedArray);
  }
}