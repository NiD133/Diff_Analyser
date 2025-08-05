/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.EqualsTester;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Tests for {@link JsonArray}.
 *
 * @author Jesse Wilson
 */
public final class JsonArrayTest {

  // region Equality and HashCode Tests

  @Test
  public void equals_emptyArrays_areEqual() {
    JsonArray a = new JsonArray();
    JsonArray b = new JsonArray();

    assertThat(a).isEqualTo(b);
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
  }

  @Test
  public void equals_and_hashCode_contract() {
    JsonArray a1 = new JsonArray();
    a1.add(new JsonPrimitive(1));
    JsonArray a2 = new JsonArray();
    a2.add(new JsonPrimitive(1));

    JsonArray b = new JsonArray();
    b.add(new JsonPrimitive(2));

    new EqualsTester().addEqualityGroup(a1, a2).addEqualityGroup(b).testEquals();
  }

  // endregion

  // region Manipulation Tests (add, remove, set)

  @Test
  public void remove_byIndex_onEmptyArray_throwsException() {
    JsonArray array = new JsonArray();
    assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
  }

  @Test
  public void remove_byElement_removesFirstOccurrenceAndReturnsTrue() {
    JsonArray array = new JsonArray();
    JsonPrimitive a = new JsonPrimitive("a");
    JsonPrimitive b = new JsonPrimitive("b");
    array.add(b);
    array.add(a);
    array.add(a); // Add a second 'a'

    boolean result = array.remove(a);

    assertThat(result).isTrue();
    assertThat(array).containsExactly(b, a).inOrder();
  }

  @Test
  public void remove_byElement_nonExistentElement_returnsFalse() {
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive("a"));

    boolean result = array.remove(new JsonPrimitive("b"));

    assertThat(result).isFalse();
    assertThat(array).hasSize(1);
  }

  @Test
  public void remove_byIndex_removesElementAndReturnsIt() {
    JsonArray array = new JsonArray();
    JsonPrimitive a = new JsonPrimitive("a");
    JsonPrimitive b = new JsonPrimitive("b");
    array.add(a);
    array.add(b);

    JsonElement removedElement = array.remove(1);

    assertThat(removedElement).isEqualTo(b);
    assertThat(array).containsExactly(a);
  }

  @Test
  public void set_atInvalidIndex_throwsException() {
    JsonArray array = new JsonArray();
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));
  }

  @Test
  public void set_replacesElementAndReturnsOldValue() {
    JsonArray array = new JsonArray();
    JsonPrimitive a = new JsonPrimitive("a");
    JsonPrimitive b = new JsonPrimitive("b");
    array.add(a);

    JsonElement oldValue = array.set(0, b);

    assertThat(oldValue).isEqualTo(a);
    assertThat(array.get(0)).isEqualTo(b);
    assertThat(array).hasSize(1);
  }

  @Test
  public void set_withNullElement_replacesWithJsonNull() {
    JsonArray array = new JsonArray();
    JsonPrimitive a = new JsonPrimitive("a");
    array.add(a);

    JsonElement oldValue = array.set(0, null);

    assertThat(oldValue).isEqualTo(a);
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
  }

  // endregion

  // region State and Copy Tests

  @Test
  public void deepCopy_isIndependentOfOriginal() {
    // Arrange
    JsonArray original = new JsonArray();
    JsonArray nestedArray = new JsonArray();
    original.add(nestedArray);

    // Act
    JsonArray copy = original.deepCopy();

    // Assert: Modify original and verify copy is unchanged
    original.add(new JsonPrimitive("y"));
    assertThat(copy).hasSize(1); // Copy should not have the new element "y"

    // Assert: Modify nested element in original and verify copy is unchanged
    nestedArray.add(new JsonPrimitive("z"));
    assertThat(original.get(0).getAsJsonArray()).hasSize(1);
    assertThat(copy.get(0).getAsJsonArray()).isEmpty(); // Nested array in copy should be empty
  }

  @Test
  public void isEmpty_reflectsArrayContent() {
    JsonArray array = new JsonArray();
    assertThat(array.isEmpty()).isTrue();
    assertThat(array).isEmpty();

    array.add(new JsonPrimitive("a"));
    assertThat(array.isEmpty()).isFalse();
    assertThat(array).isNotEmpty();

    array.remove(0);
    assertThat(array.isEmpty()).isTrue();
    assertThat(array).isEmpty();
  }

  // endregion

  // region Getter Exception Tests

  @Test
  public void get_withNegativeIndex_throwsException() {
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(1));

    var e = assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
    assertThat(e).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");
  }

  @Test
  public void getAsType_onArrayWithNotOneElement_throwsException() {
    // Test with empty array
    JsonArray emptyArray = new JsonArray();
    var e1 = assertThrows(IllegalStateException.class, emptyArray::getAsByte);
    assertThat(e1).hasMessageThat().isEqualTo("Array must have size 1, but has size 0");

    // Test with multi-element array
    JsonArray multiElementArray = new JsonArray();
    multiElementArray.add(true);
    multiElementArray.add(false);
    var e2 = assertThrows(IllegalStateException.class, multiElementArray::getAsByte);
    assertThat(e2).hasMessageThat().isEqualTo("Array must have size 1, but has size 2");
  }

  @Test
  public void getAsType_onArrayOfObject_throwsException() {
    JsonArray array = new JsonArray();
    array.add(new JsonObject());

    // getAsString is just an example; any getAs... would fail with the same reason
    var e = assertThrows(UnsupportedOperationException.class, array::getAsString);
    assertThat(e).hasMessageThat().isEqualTo("JsonObject");
  }

  @Test
  public void getAsNumber_onArrayOfString_throwsException() {
    JsonArray array = new JsonArray();
    array.add("hello");

    assertThrows(NumberFormatException.class, array::getAsDouble);
    assertThrows(NumberFormatException.class, array::getAsInt);
    assertThrows(NumberFormatException.class, array::getAsLong);
  }

  @Test
  public void getAsJsonArray_onElementThatIsNotAnArray_throwsException() {
    JsonArray array = new JsonArray();
    array.add("hello");

    var e = assertThrows(IllegalStateException.class, () -> array.get(0).getAsJsonArray());
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");
  }

  @Test
  public void getAsJsonObject_onJsonArray_throwsException() {
    JsonArray array = new JsonArray();
    array.add("hello");

    var e = assertThrows(IllegalStateException.class, array::getAsJsonObject);
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");
  }

  // endregion

  // region Add Overload Tests

  @Test
  public void add_withString_addsJsonPrimitive() {
    JsonArray array = new JsonArray();
    array.add("Hello");
    array.add("Goodbye");
    array.add((String) null);
    array.add("Yes");

    assertThat(array)
        .containsExactly(
            new JsonPrimitive("Hello"),
            new JsonPrimitive("Goodbye"),
            JsonNull.INSTANCE,
            new JsonPrimitive("Yes"))
        .inOrder();
  }

  @Test
  public void add_withInteger_addsJsonPrimitive() {
    JsonArray array = new JsonArray();
    array.add(1);
    array.add(2);
    array.add(-3);
    array.add((Integer) null);
    array.add(0);

    assertThat(array)
        .containsExactly(
            new JsonPrimitive(1),
            new JsonPrimitive(2),
            new JsonPrimitive(-3),
            JsonNull.INSTANCE,
            new JsonPrimitive(0))
        .inOrder();
  }

  @Test
  public void add_withDouble_addsJsonPrimitive() {
    JsonArray array = new JsonArray();
    array.add(1.0);
    array.add(2.13232);
    array.add((Double) null);
    array.add(-0.00234);

    assertThat(array)
        .containsExactly(
            new JsonPrimitive(1.0),
            new JsonPrimitive(2.13232),
            JsonNull.INSTANCE,
            new JsonPrimitive(-0.00234))
        .inOrder();
  }

  @Test
  public void add_withBoolean_addsJsonPrimitive() {
    JsonArray array = new JsonArray();
    array.add(true);
    array.add(false);
    array.add((Boolean) null);
    array.add(true);

    assertThat(array)
        .containsExactly(
            new JsonPrimitive(true),
            new JsonPrimitive(false),
            JsonNull.INSTANCE,
            new JsonPrimitive(true))
        .inOrder();
  }

  @Test
  public void add_withCharacter_addsJsonPrimitive() {
    JsonArray array = new JsonArray();
    array.add('a');
    array.add('e');
    array.add((char) 111); // 'o'
    array.add((Character) null);
    array.add('u');

    assertThat(array)
        .containsExactly(
            new JsonPrimitive('a'),
            new JsonPrimitive('e'),
            new JsonPrimitive('o'),
            JsonNull.INSTANCE,
            new JsonPrimitive('u'))
        .inOrder();
  }

  @Test
  public void add_withMixedTypes_addsCorrespondingElements() {
    JsonArray array = new JsonArray();
    array.add('a');
    array.add("apple");
    array.add(12121);
    array.add((Boolean) null);
    array.add(12.232);
    array.add(BigInteger.valueOf(2323));

    assertThat(array)
        .containsExactly(
            new JsonPrimitive('a'),
            new JsonPrimitive("apple"),
            new JsonPrimitive(12121),
            JsonNull.INSTANCE,
            new JsonPrimitive(12.232),
            new JsonPrimitive(BigInteger.valueOf(2323)))
        .inOrder();
  }

  @Test
  public void add_withNulls_addsJsonNulls() {
    JsonArray array = new JsonArray();
    array.add((Character) null);
    array.add((Boolean) null);
    array.add((Number) null);
    array.add((String) null);
    array.add((JsonElement) null);

    assertThat(array)
        .containsExactly(
            JsonNull.INSTANCE,
            JsonNull.INSTANCE,
            JsonNull.INSTANCE,
            JsonNull.INSTANCE,
            JsonNull.INSTANCE)
        .inOrder();
  }

  @Test
  public void add_withDuplicatePrimitives_addsAll() {
    JsonArray array = new JsonArray();
    array.add('a');
    array.add('a');
    array.add(1212);
    array.add(1212);
    array.add((Boolean) null);
    array.add((Boolean) null);

    assertThat(array)
        .containsExactly(
            new JsonPrimitive('a'),
            new JsonPrimitive('a'),
            new JsonPrimitive(1212),
            new JsonPrimitive(1212),
            JsonNull.INSTANCE,
            JsonNull.INSTANCE)
        .inOrder();
  }

  // endregion

  @Test
  public void toString_withVariousElements_returnsCorrectJsonString() {
    // Arrange
    JsonArray array = new JsonArray();
    array.add(JsonNull.INSTANCE);
    array.add(Float.NaN); // Special floating point value
    array.add("a\0"); // String with null character
    JsonArray nestedArray = new JsonArray();
    nestedArray.add('"'); // String with quote
    array.add(nestedArray);
    JsonObject nestedObject = new JsonObject();
    nestedObject.addProperty("n\0", 1); // Property name with null character
    array.add(nestedObject);

    // Act & Assert
    assertThat(array.toString()).isEqualTo("[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]");
  }
}