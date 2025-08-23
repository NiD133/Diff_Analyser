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
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Behavior-focused tests for JsonArray.
 *
 * Each test follows the Arrange-Act-Assert pattern and uses descriptive names to clarify
 * intent and expected behavior. Helper assertions and comments highlight the most
 * relevant aspects of each scenario.
 */
public final class JsonArrayTest {

  // ---------------------------------------------------------------------------------------------
  // Equality and hash code
  // ---------------------------------------------------------------------------------------------

  @Test
  public void equals_emptyArrays_areEqualAndHaveSameHashCode() {
    MoreAsserts.assertEqualsAndHashCode(new JsonArray(), new JsonArray());
  }

  @Test
  public void equals_nonEmptyArrays_orderAndSizeMatter() {
    // Arrange
    JsonArray a = new JsonArray();
    JsonArray b = new JsonArray();

    // Sanity: equals on empty arrays (also covered above)
    new EqualsTester().addEqualityGroup(a).testEquals();

    // Act + Assert
    a.add(new JsonObject());
    assertThat(a.equals(b)).isFalse();
    assertThat(b.equals(a)).isFalse();

    b.add(new JsonObject());
    MoreAsserts.assertEqualsAndHashCode(a, b);

    a.add(new JsonObject());
    assertThat(a.equals(b)).isFalse();
    assertThat(b.equals(a)).isFalse();

    b.add(JsonNull.INSTANCE);
    assertThat(a.equals(b)).isFalse();
    assertThat(b.equals(a)).isFalse();
  }

  // ---------------------------------------------------------------------------------------------
  // Mutation: remove, set
  // ---------------------------------------------------------------------------------------------

  @Test
  public void remove_byIndexAndByElement_behavesLikeList() {
    JsonArray array = new JsonArray();

    assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));

    JsonPrimitive a = new JsonPrimitive("a");

    // Remove by element (not present -> false, present -> true)
    array.add(a);
    assertThat(array.remove(a)).isTrue();
    assertThat(array).doesNotContain(a);

    // Remove by index shifts following elements
    array.add(a);
    array.add(new JsonPrimitive("b"));
    assertThat(array.remove(1).getAsString()).isEqualTo("b");
    assertThat(array).hasSize(1);
    assertThat(array).contains(a);
  }

  @Test
  public void set_replacesElement_returnsOldValue_andTreatsNullAsJsonNull() {
    JsonArray array = new JsonArray();

    assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));

    JsonPrimitive a = new JsonPrimitive("a");
    array.add(a);

    // Replace existing with "b"
    JsonPrimitive b = new JsonPrimitive("b");
    JsonElement oldValue = array.set(0, b);
    assertThat(oldValue).isEqualTo(a);
    assertThat(array.get(0).getAsString()).isEqualTo("b");

    // Replace with null -> becomes JsonNull
    oldValue = array.set(0, null);
    assertThat(oldValue).isEqualTo(b);
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);

    // Replace JsonNull with "c"
    oldValue = array.set(0, new JsonPrimitive("c"));
    assertThat(oldValue).isEqualTo(JsonNull.INSTANCE);
    assertThat(array.get(0).getAsString()).isEqualTo("c");
    assertThat(array).hasSize(1);
  }

  // ---------------------------------------------------------------------------------------------
  // Copying
  // ---------------------------------------------------------------------------------------------

  @Test
  public void deepCopy_createsIndependentGraph() {
    // Arrange
    JsonArray original = new JsonArray();
    JsonArray firstEntry = new JsonArray();
    original.add(firstEntry);

    // Act
    JsonArray copy = original.deepCopy();
    original.add(new JsonPrimitive("y"));

    // Assert: top-level size differs after mutation
    assertThat(copy).hasSize(1);

    // Mutate nested array in original and verify copy's nested array is unaffected
    firstEntry.add(new JsonPrimitive("z"));
    assertThat(original.get(0).getAsJsonArray()).hasSize(1);
    assertThat(copy.get(0).getAsJsonArray()).hasSize(0);
  }

  // ---------------------------------------------------------------------------------------------
  // Emptiness
  // ---------------------------------------------------------------------------------------------

  @Test
  public void isEmpty_reflectsContent() {
    JsonArray array = new JsonArray();
    assertThat(array).isEmpty();

    array.add(new JsonPrimitive("a"));
    assertThat(array).isNotEmpty();

    array.remove(0);
    assertThat(array).isEmpty();
  }

  // ---------------------------------------------------------------------------------------------
  // getAsX: failures and size checks
  // ---------------------------------------------------------------------------------------------

  @Test
  public void getAsX_onIncompatibleElement_throwsWithHelpfulMessage() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(
        JsonParser.parseString(
            "{"
                + "\"key1\":\"value1\","
                + "\"key2\":\"value2\","
                + "\"key3\":\"value3\","
                + "\"key4\":\"value4\""
                + "}"));

    Exception e;

    // Incompatible: array does not represent a single boolean/string/etc.
    e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsBoolean());
    assertThat(e).hasMessageThat().isEqualTo("JsonObject");

    e = assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
    assertThat(e).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");

    e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsString());
    assertThat(e).hasMessageThat().isEqualTo("JsonObject");

    // Replace with a single string element, then use number getters
    jsonArray.remove(0);
    jsonArray.add("hello");

    e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsDouble());
    assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");

    e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsInt());
    assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");

    e = assertThrows(IllegalStateException.class, () -> jsonArray.get(0).getAsJsonArray());
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");

    e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsJsonObject());
    assertThat(e).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");

    e = assertThrows(NumberFormatException.class, () -> jsonArray.getAsLong());
    assertThat(e).hasMessageThat().isEqualTo("For input string: \"hello\"");
  }

  @Test
  public void getAsX_whenArraySizeNotOne_throwsIllegalStateException() {
    JsonArray jsonArray = new JsonArray();

    IllegalStateException e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
    assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 0");

    jsonArray.add(true);
    jsonArray.add(false);

    e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
    assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 2");
  }

  // ---------------------------------------------------------------------------------------------
  // Primitive additions (by type)
  // ---------------------------------------------------------------------------------------------

  @Test
  public void add_strings_areAppendedAndNullBecomesJsonNull() {
    JsonArray array = new JsonArray();

    array.add("Hello");
    array.add("Goodbye");
    array.add("Thank you");
    array.add((String) null);
    array.add("Yes");

    assertThat(array.toString()).isEqualTo("[\"Hello\",\"Goodbye\",\"Thank you\",null,\"Yes\"]");
  }

  @Test
  public void add_integers_areAppendedAndNullBecomesJsonNull() {
    JsonArray array = new JsonArray();

    int x = 1;
    array.add(x);

    x = 2;
    array.add(x);

    x = -3;
    array.add(x);

    array.add((Integer) null);

    x = 4;
    array.add(x);

    x = 0;
    array.add(x);

    assertThat(array.toString()).isEqualTo("[1,2,-3,null,4,0]");
  }

  @Test
  public void add_doubles_areAppendedAndNullBecomesJsonNull() {
    JsonArray array = new JsonArray();

    double x = 1.0;
    array.add(x);

    x = 2.13232;
    array.add(x);

    x = 0.121;
    array.add(x);

    array.add((Double) null);

    x = -0.00234;
    array.add(x);

    array.add((Double) null);

    assertThat(array.toString()).isEqualTo("[1.0,2.13232,0.121,null,-0.00234,null]");
  }

  @Test
  public void add_booleans_areAppendedAndNullBecomesJsonNull() {
    JsonArray array = new JsonArray();

    array.add(true);
    array.add(true);
    array.add(false);
    array.add(false);
    array.add((Boolean) null);
    array.add(true);

    assertThat(array.toString()).isEqualTo("[true,true,false,false,null,true]");
  }

  @Test
  public void add_characters_areStringified_andNullBecomesJsonNull() {
    JsonArray array = new JsonArray();
    final int letterOCode = 111; // 'o'

    array.add('a');
    array.add('e');
    array.add('i');
    array.add((char) letterOCode);
    array.add((Character) null);
    array.add('u');
    array.add("and sometimes Y");

    assertThat(array.toString())
        .isEqualTo("[\"a\",\"e\",\"i\",\"o\",null,\"u\",\"and sometimes Y\"]");
  }

  @Test
  public void add_mixedPrimitives_preservesOrder_andNullBecomesJsonNull() {
    JsonArray array = new JsonArray();

    array.add('a');
    array.add("apple");
    array.add(12121);
    array.add((char) 111);

    array.add((Boolean) null);
    assertThat(array.get(array.size() - 1)).isEqualTo(JsonNull.INSTANCE);

    array.add((Character) null);
    assertThat(array.get(array.size() - 1)).isEqualTo(JsonNull.INSTANCE);

    array.add(12.232);
    array.add(BigInteger.valueOf(2323));

    assertThat(array.toString())
        .isEqualTo("[\"a\",\"apple\",12121,\"o\",null,null,12.232,2323]");
  }

  @Test
  public void add_nullPrimitives_addAsJsonNull_notJavaNull() {
    JsonArray array = new JsonArray();

    array.add((Character) null);
    array.add((Boolean) null);
    array.add((Integer) null);
    array.add((Double) null);
    array.add((Float) null);
    array.add((BigInteger) null);
    array.add((String) null);
    array.add((Boolean) null);
    array.add((Number) null);

    assertThat(array.toString()).isEqualTo("[null,null,null,null,null,null,null,null,null]");

    // Verify stored as JsonNull elements
    for (int i = 0; i < array.size(); i++) {
      assertThat(array.get(i)).isEqualTo(JsonNull.INSTANCE);
    }
  }

  @Test
  public void add_nullJsonElement_addsJsonNull() {
    JsonArray array = new JsonArray();

    array.add((JsonElement) null);

    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
  }

  @Test
  public void add_duplicateValues_areAllowedAndPreserved() {
    JsonArray array = new JsonArray();

    array.add('a');
    array.add('a');
    array.add(true);
    array.add(true);
    array.add(1212);
    array.add(1212);
    array.add(34.34);
    array.add(34.34);
    array.add((Boolean) null);
    array.add((Boolean) null);

    assertThat(array.toString())
        .isEqualTo("[\"a\",\"a\",true,true,1212,1212,34.34,34.34,null,null]");
  }

  // ---------------------------------------------------------------------------------------------
  // String representation
  // ---------------------------------------------------------------------------------------------

  @Test
  public void toString_formatsJson_correctlyEscapesAndPreservesTypes() {
    JsonArray array = new JsonArray();
    assertThat(array.toString()).isEqualTo("[]");

    array.add(JsonNull.INSTANCE);
    array.add(Float.NaN);
    array.add("a\0");

    JsonArray nestedArray = new JsonArray();
    nestedArray.add('"');
    array.add(nestedArray);

    JsonObject nestedObject = new JsonObject();
    nestedObject.addProperty("n\0", 1);
    array.add(nestedObject);

    assertThat(array.toString()).isEqualTo("[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]");
  }
}