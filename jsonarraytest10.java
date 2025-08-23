package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

// The class name "JsonArrayTestTest10" is unusual. In a real-world scenario,
// this test would likely be part of a larger "JsonArrayTest" class.
public class JsonArrayTestTest10 {

  @Test
  public void add_withIntegersAndNull_buildsCorrectArray() {
    // Arrange: Create the JsonArray to be tested.
    JsonArray actualArray = new JsonArray();

    // Act: Add a series of integer primitives, including a null Integer, to the array.
    // The `add` method should handle autoboxing for primitives and convert null to JsonNull.
    actualArray.add(1);
    actualArray.add(2);
    actualArray.add(-3);
    actualArray.add((Integer) null);
    actualArray.add(4);
    actualArray.add(0);

    // Assert: Verify that the actual array matches the expected structure and content.
    JsonArray expectedArray = new JsonArray();
    expectedArray.add(1);
    expectedArray.add(2);
    expectedArray.add(-3);
    expectedArray.add((Integer) null);
    expectedArray.add(4);
    expectedArray.add(0);

    assertThat(actualArray).isEqualTo(expectedArray);
  }
}