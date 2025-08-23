package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class JsonArrayTestTest5 {

  @Test
  public void deepCopy_createsFullyIndependentCopy() {
    // Arrange: Create an array containing a nested array.
    // The structure is: [ [] ]
    JsonArray originalNestedArray = new JsonArray();
    JsonArray originalArray = new JsonArray();
    originalArray.add(originalNestedArray);

    // Act: Create a deep copy of the original array.
    JsonArray copiedArray = originalArray.deepCopy();

    // Act: Modify the original array and its nested elements.
    // These changes should not be reflected in the copy.
    originalArray.add(new JsonPrimitive("new top-level element"));
    originalNestedArray.add(new JsonPrimitive("new nested element"));

    // Assert: The copy remains unchanged and is independent of the original.
    // It should still have the structure it was created with: [ [] ]
    JsonArray expectedArray = new JsonArray();
    expectedArray.add(new JsonArray()); // The expected empty nested array

    assertThat(copiedArray).isEqualTo(expectedArray);

    // For additional clarity, assert that the copy is no longer equal to the
    // now-modified original array.
    assertThat(copiedArray).isNotEqualTo(originalArray);
  }
}