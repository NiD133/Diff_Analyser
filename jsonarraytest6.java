package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class JsonArrayTest {

  @Test
  public void testIsEmpty_reflectsArrayContent() {
    // 1. A newly created array should be empty
    JsonArray array = new JsonArray();
    assertThat(array).isEmpty();

    // 2. After adding an element, the array should not be empty
    JsonPrimitive element = new JsonPrimitive("a");
    array.add(element);
    assertThat(array).isNotEmpty();

    // 3. After removing the only element, the array should be empty again
    array.remove(0);
    assertThat(array).isEmpty();
  }
}