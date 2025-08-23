package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on how it handles the addition of {@code null} values.
 */
public class JsonArrayTestTest15 {

  /**
   * The {@link JsonArray#add} methods are documented to convert any {@code null} argument into a
   * {@link JsonNull} instance. This test verifies that behavior across all relevant overloads.
   */
  @Test
  public void add_whenNullArgument_appendsJsonNullInstance() {
    // Arrange
    JsonArray jsonArray = new JsonArray();

    // Act: Add null using each distinct `add` method overload.
    jsonArray.add((Boolean) null);
    jsonArray.add((Character) null);
    jsonArray.add((Number) null);
    jsonArray.add((String) null);
    jsonArray.add((JsonElement) null);

    // Assert
    // Verify that the array contains the expected number of elements.
    assertThat(jsonArray).hasSize(5);
    // Verify that every element added is the singleton JsonNull.INSTANCE.
    assertThat(jsonArray).everyItem().isEqualTo(JsonNull.INSTANCE);
  }
}