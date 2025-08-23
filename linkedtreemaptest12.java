package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class LinkedTreeMapTestTest12 {

  /**
   * Tests that the map can correctly store and retrieve an empty string as a value.
   * This verifies that empty strings are handled as valid, non-null values.
   */
  @Test
  public void mapShouldStoreAndRetrieveEmptyStringValue() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    String key = "anyKey";
    String emptyValue = "";

    // Act
    map.put(key, emptyValue);

    // Assert
    assertThat(map.containsKey(key)).isTrue();
    assertThat(map.get(key)).isEqualTo(emptyValue);
  }
}