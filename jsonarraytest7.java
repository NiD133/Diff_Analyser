package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for failure conditions and exception handling in {@link JsonArray}.
 */
public class JsonArrayTest {

  @Test
  public void get_withNegativeIndex_throwsIndexOutOfBoundsException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("a");

    // Act & Assert
    IndexOutOfBoundsException e = assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
    assertThat(e).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");
  }

  @Test
  public void getAsType_onArrayWithSingleObjectElement_throwsUnsupportedOperationException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add(new JsonObject()); // The single element is a JsonObject

    // Act & Assert for getAsBoolean
    UnsupportedOperationException e1 =
        assertThrows(UnsupportedOperationException.class, () -> array.getAsBoolean());
    assertThat(e1).hasMessageThat().isEqualTo("JsonObject");

    // Act & Assert for getAsString
    UnsupportedOperationException e2 =
        assertThrows(UnsupportedOperationException.class, () -> array.getAsString());
    assertThat(e2).hasMessageThat().isEqualTo("JsonObject");
  }

  @Test
  public void getAsNumber_onArrayWithSingleNonNumericStringElement_throwsNumberFormatException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("hello"); // The single element is a non-numeric string

    // Act & Assert for various numeric types
    assertThrows(NumberFormatException.class, () -> array.getAsDouble());
    assertThrows(NumberFormatException.class, () -> array.getAsInt());
    assertThrows(NumberFormatException.class, () -> array.getAsLong());
  }

  @Test
  public void getAsJsonObject_onJsonArray_throwsIllegalStateException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("hello");

    // Act & Assert
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> array.getAsJsonObject());
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");
  }

  @Test
  public void getAsJsonArray_onContainedStringElement_throwsIllegalStateException() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add("hello");

    // Act & Assert
    // Note: This tests the behavior of JsonElement.getAsJsonArray(), not JsonArray itself.
    IllegalStateException e =
        assertThrows(IllegalStateException.class, () -> array.get(0).getAsJsonArray());
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");
  }
}