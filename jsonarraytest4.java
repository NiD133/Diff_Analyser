package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link JsonArray#set(int, JsonElement)} method.
 */
public class JsonArraySetTest {

  @Test
  public void set_atValidIndex_replacesElementAndReturnsOld() {
    // Arrange
    JsonPrimitive initialElement = new JsonPrimitive("a");
    JsonArray array = new JsonArray();
    array.add(initialElement);

    JsonPrimitive newElement = new JsonPrimitive("b");

    // Act
    JsonElement oldElement = array.set(0, newElement);

    // Assert
    assertThat(array.get(0)).isEqualTo(newElement);
    assertThat(oldElement).isEqualTo(initialElement);
    assertThat(array).hasSize(1); // Ensure size does not change
  }

  @Test
  public void set_withNullElement_replacesWithJsonNull() {
    // Arrange
    JsonPrimitive initialElement = new JsonPrimitive("a");
    JsonArray array = new JsonArray();
    array.add(initialElement);

    // Act
    JsonElement oldElement = array.set(0, null);

    // Assert
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
    assertThat(oldElement).isEqualTo(initialElement);
    assertThat(array).hasSize(1);
  }

  @Test
  public void set_onEmptyArray_throwsIndexOutOfBoundsException() {
    // Arrange
    JsonArray array = new JsonArray();

    // Act & Assert
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));
  }

  @Test
  public void set_withNegativeIndex_throwsIndexOutOfBoundsException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("a");

    // Act & Assert
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(-1, new JsonPrimitive("b")));
  }

  @Test
  public void set_withIndexEqualToSize_throwsIndexOutOfBoundsException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("a");

    // Act & Assert
    // Index 1 is out of bounds for an array of size 1
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(1, new JsonPrimitive("b")));
  }
}