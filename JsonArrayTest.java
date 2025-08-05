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
 * Tests handling of JSON arrays.
 *
 * @author Jesse Wilson
 */
public final class JsonArrayTest {

  // ========== Equality Tests ==========

  @Test
  public void testEqualsOnEmptyArray() {
    JsonArray emptyArray1 = new JsonArray();
    JsonArray emptyArray2 = new JsonArray();
    
    MoreAsserts.assertEqualsAndHashCode(emptyArray1, emptyArray2);
  }

  @Test
  public void testEqualsNonEmptyArray() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    // Test empty arrays are equal
    new EqualsTester().addEqualityGroup(array1).testEquals();

    // Arrays with different sizes should not be equal
    array1.add(new JsonObject());
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();

    // Arrays with same elements should be equal
    array2.add(new JsonObject());
    MoreAsserts.assertEqualsAndHashCode(array1, array2);

    // Arrays with different number of elements should not be equal
    array1.add(new JsonObject());
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();

    // Arrays with different element types should not be equal
    array2.add(JsonNull.INSTANCE);
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();
  }

  // ========== Element Removal Tests ==========

  @Test
  public void testRemove() {
    JsonArray array = new JsonArray();
    
    // Should throw exception when removing from empty array
    assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));

    JsonPrimitive elementA = new JsonPrimitive("a");
    array.add(elementA);
    
    // Test removing by element reference
    assertThat(array.remove(elementA)).isTrue();
    assertThat(array).doesNotContain(elementA);
    
    // Test removing by index
    array.add(elementA);
    array.add(new JsonPrimitive("b"));
    JsonElement removedElement = array.remove(1);
    assertThat(removedElement.getAsString()).isEqualTo("b");
    assertThat(array).hasSize(1);
    assertThat(array).contains(elementA);
  }

  // ========== Element Modification Tests ==========

  @Test
  public void testSet() {
    JsonArray array = new JsonArray();
    
    // Should throw exception when setting element in empty array
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));

    JsonPrimitive elementA = new JsonPrimitive("a");
    array.add(elementA);

    // Test setting with non-null element
    JsonPrimitive elementB = new JsonPrimitive("b");
    JsonElement previousValue = array.set(0, elementB);
    assertThat(previousValue).isEqualTo(elementA);
    assertThat(array.get(0).getAsString()).isEqualTo("b");

    // Test setting with null (should become JsonNull)
    previousValue = array.set(0, null);
    assertThat(previousValue).isEqualTo(elementB);
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);

    // Test setting over JsonNull
    JsonPrimitive elementC = new JsonPrimitive("c");
    previousValue = array.set(0, elementC);
    assertThat(previousValue).isEqualTo(JsonNull.INSTANCE);
    assertThat(array.get(0).getAsString()).isEqualTo("c");
    assertThat(array).hasSize(1);
  }

  // ========== Deep Copy Tests ==========

  @Test
  public void testDeepCopy() {
    JsonArray originalArray = new JsonArray();
    JsonArray nestedArray = new JsonArray();
    originalArray.add(nestedArray);

    JsonArray copiedArray = originalArray.deepCopy();
    
    // Modifications to original should not affect copy
    originalArray.add(new JsonPrimitive("y"));
    assertThat(copiedArray).hasSize(1);
    
    // Modifications to nested elements in original should not affect copy
    nestedArray.add(new JsonPrimitive("z"));
    assertThat(originalArray.get(0).getAsJsonArray()).hasSize(1);
    assertThat(copiedArray.get(0).getAsJsonArray()).hasSize(0);
  }

  // ========== Size and Empty Tests ==========

  @Test
  public void testIsEmpty() {
    JsonArray array = new JsonArray();
    assertThat(array).isEmpty();

    JsonPrimitive element = new JsonPrimitive("a");
    array.add(element);
    assertThat(array).isNotEmpty();

    array.remove(0);
    assertThat(array).isEmpty();
  }

  // ========== Error Handling Tests ==========

  @Test
  public void testInvalidGetAsOperations_WithJsonObject() {
    JsonArray arrayWithObject = new JsonArray();
    String jsonObjectString = "{"
        + "\"key1\":\"value1\","
        + "\"key2\":\"value2\","
        + "\"key3\":\"value3\","
        + "\"key4\":\"value4\""
        + "}";
    arrayWithObject.add(JsonParser.parseString(jsonObjectString));

    // JsonArray containing JsonObject should not be convertible to primitives
    Exception exception = assertThrows(UnsupportedOperationException.class, 
        () -> arrayWithObject.getAsBoolean());
    assertThat(exception).hasMessageThat().isEqualTo("JsonObject");

    exception = assertThrows(UnsupportedOperationException.class, 
        () -> arrayWithObject.getAsString());
    assertThat(exception).hasMessageThat().isEqualTo("JsonObject");
  }

  @Test
  public void testInvalidGetAsOperations_WithInvalidString() {
    JsonArray arrayWithString = new JsonArray();
    arrayWithString.add("hello");

    // String "hello" cannot be converted to numbers
    Exception exception = assertThrows(NumberFormatException.class, 
        () -> arrayWithString.getAsDouble());
    assertThat(exception).hasMessageThat().isEqualTo("For input string: \"hello\"");

    exception = assertThrows(NumberFormatException.class, 
        () -> arrayWithString.getAsInt());
    assertThat(exception).hasMessageThat().isEqualTo("For input string: \"hello\"");

    exception = assertThrows(NumberFormatException.class, 
        () -> arrayWithString.getAsLong());
    assertThat(exception).hasMessageThat().isEqualTo("For input string: \"hello\"");
  }

  @Test
  public void testInvalidGetAsOperations_WithWrongTypes() {
    JsonArray arrayWithString = new JsonArray();
    arrayWithString.add("hello");

    // String element cannot be accessed as JsonArray
    Exception exception = assertThrows(IllegalStateException.class, 
        () -> arrayWithString.get(0).getAsJsonArray());
    assertThat(exception).hasMessageThat().isEqualTo("Not a JSON Array: \"hello\"");

    // JsonArray cannot be accessed as JsonObject
    exception = assertThrows(IllegalStateException.class, 
        () -> arrayWithString.getAsJsonObject());
    assertThat(exception).hasMessageThat().isEqualTo("Not a JSON Object: [\"hello\"]");
  }

  @Test
  public void testInvalidArrayAccess() {
    JsonArray array = new JsonArray();
    array.add(JsonParser.parseString("{\"key\":\"value\"}"));

    // Negative index should throw exception
    Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
    assertThat(exception).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");
  }

  @Test
  public void testGetAs_RequiresSingleElement() {
    JsonArray emptyArray = new JsonArray();
    Exception exception = assertThrows(IllegalStateException.class, () -> emptyArray.getAsByte());
    assertThat(exception).hasMessageThat().isEqualTo("Array must have size 1, but has size 0");

    JsonArray arrayWithTwoElements = new JsonArray();
    arrayWithTwoElements.add(true);
    arrayWithTwoElements.add(false);
    exception = assertThrows(IllegalStateException.class, () -> arrayWithTwoElements.getAsByte());
    assertThat(exception).hasMessageThat().isEqualTo("Array must have size 1, but has size 2");
  }

  // ========== Primitive Addition Tests ==========

  @Test
  public void testAddStringPrimitives() {
    JsonArray array = new JsonArray();

    array.add("Hello");
    array.add("Goodbye");
    array.add("Thank you");
    array.add((String) null);  // Should become JsonNull
    array.add("Yes");

    String expectedJson = "[\"Hello\",\"Goodbye\",\"Thank you\",null,\"Yes\"]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddIntegerPrimitives() {
    JsonArray array = new JsonArray();

    array.add(1);
    array.add(2);
    array.add(-3);
    array.add((Integer) null);  // Should become JsonNull
    array.add(4);
    array.add(0);

    String expectedJson = "[1,2,-3,null,4,0]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddDoublePrimitives() {
    JsonArray array = new JsonArray();

    array.add(1.0);
    array.add(2.13232);
    array.add(0.121);
    array.add((Double) null);  // Should become JsonNull
    array.add(-0.00234);
    array.add((Double) null);  // Should become JsonNull

    String expectedJson = "[1.0,2.13232,0.121,null,-0.00234,null]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddBooleanPrimitives() {
    JsonArray array = new JsonArray();

    array.add(true);
    array.add(true);
    array.add(false);
    array.add(false);
    array.add((Boolean) null);  // Should become JsonNull
    array.add(true);

    String expectedJson = "[true,true,false,false,null,true]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddCharacterPrimitives() {
    JsonArray array = new JsonArray();

    array.add('a');
    array.add('e');
    array.add('i');
    array.add((char) 111);  // ASCII 'o'
    array.add((Character) null);  // Should become JsonNull
    array.add('u');
    array.add("and sometimes Y");

    String expectedJson = "[\"a\",\"e\",\"i\",\"o\",null,\"u\",\"and sometimes Y\"]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddMixedPrimitives() {
    JsonArray array = new JsonArray();

    array.add('a');
    array.add("apple");
    array.add(12121);
    array.add((char) 111);  // ASCII 'o'

    // Verify null values become JsonNull instances
    array.add((Boolean) null);
    assertThat(array.get(array.size() - 1)).isEqualTo(JsonNull.INSTANCE);

    array.add((Character) null);
    assertThat(array.get(array.size() - 1)).isEqualTo(JsonNull.INSTANCE);

    array.add(12.232);
    array.add(BigInteger.valueOf(2323));

    String expectedJson = "[\"a\",\"apple\",12121,\"o\",null,null,12.232,2323]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  @Test
  public void testAddNullPrimitives_BecomesJsonNull() {
    JsonArray array = new JsonArray();

    // All null primitive types should become JsonNull
    array.add((Character) null);
    array.add((Boolean) null);
    array.add((Integer) null);
    array.add((Double) null);
    array.add((Float) null);
    array.add((BigInteger) null);
    array.add((String) null);
    array.add((Boolean) null);
    array.add((Number) null);

    String expectedJson = "[null,null,null,null,null,null,null,null,null]";
    assertThat(array.toString()).isEqualTo(expectedJson);
    
    // Verify all elements are JsonNull instances, not Java null
    for (int i = 0; i < array.size(); i++) {
      assertThat(array.get(i)).isEqualTo(JsonNull.INSTANCE);
    }
  }

  @Test
  public void testAddNullJsonElement_BecomesJsonNull() {
    JsonArray array = new JsonArray();
    array.add((JsonElement) null);
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);
  }

  @Test
  public void testAddDuplicateValues() {
    JsonArray array = new JsonArray();

    // Arrays should allow duplicate values
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

    String expectedJson = "[\"a\",\"a\",true,true,1212,1212,34.34,34.34,null,null]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }

  // ========== String Representation Tests ==========

  @Test
  public void testToString_WithSpecialCharacters() {
    JsonArray array = new JsonArray();
    assertThat(array.toString()).isEqualTo("[]");

    // Test various special values and characters
    array.add(JsonNull.INSTANCE);
    array.add(Float.NaN);
    array.add("a\0");  // String with null character
    
    JsonArray nestedArray = new JsonArray();
    nestedArray.add('"');  // Quote character
    array.add(nestedArray);
    
    JsonObject nestedObject = new JsonObject();
    nestedObject.addProperty("n\0", 1);  // Property name with null character
    array.add(nestedObject);
    
    String expectedJson = "[null,NaN,\"a\\u0000\",[\"\\\"\"],{\"n\\u0000\":1}]";
    assertThat(array.toString()).isEqualTo(expectedJson);
  }
}