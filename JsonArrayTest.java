package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.EqualsTester;
import com.google.gson.common.MoreAsserts;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Unit tests for JsonArray class, focusing on JSON array handling.
 */
public final class JsonArrayTest {

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

    new EqualsTester().addEqualityGroup(array1).testEquals();

    array1.add(new JsonObject());
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();

    array2.add(new JsonObject());
    MoreAsserts.assertEqualsAndHashCode(array1, array2);

    array1.add(new JsonObject());
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();

    array2.add(JsonNull.INSTANCE);
    assertThat(array1.equals(array2)).isFalse();
    assertThat(array2.equals(array1)).isFalse();
  }

  @Test
  public void testRemoveElement() {
    JsonArray array = new JsonArray();
    assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));

    JsonPrimitive elementA = new JsonPrimitive("a");
    array.add(elementA);
    assertThat(array.remove(elementA)).isTrue();
    assertThat(array).doesNotContain(elementA);

    array.add(elementA);
    array.add(new JsonPrimitive("b"));
    assertThat(array.remove(1).getAsString()).isEqualTo("b");
    assertThat(array).hasSize(1);
    assertThat(array).contains(elementA);
  }

  @Test
  public void testSetElement() {
    JsonArray array = new JsonArray();
    assertThrows(IndexOutOfBoundsException.class, () -> array.set(0, new JsonPrimitive(1)));

    JsonPrimitive elementA = new JsonPrimitive("a");
    array.add(elementA);

    JsonPrimitive elementB = new JsonPrimitive("b");
    JsonElement oldValue = array.set(0, elementB);
    assertThat(oldValue).isEqualTo(elementA);
    assertThat(array.get(0).getAsString()).isEqualTo("b");

    oldValue = array.set(0, null);
    assertThat(oldValue).isEqualTo(elementB);
    assertThat(array.get(0)).isEqualTo(JsonNull.INSTANCE);

    oldValue = array.set(0, new JsonPrimitive("c"));
    assertThat(oldValue).isEqualTo(JsonNull.INSTANCE);
    assertThat(array.get(0).getAsString()).isEqualTo("c");
    assertThat(array).hasSize(1);
  }

  @Test
  public void testDeepCopy() {
    JsonArray originalArray = new JsonArray();
    JsonArray nestedArray = new JsonArray();
    originalArray.add(nestedArray);

    JsonArray copiedArray = originalArray.deepCopy();
    originalArray.add(new JsonPrimitive("y"));

    assertThat(copiedArray).hasSize(1);
    nestedArray.add(new JsonPrimitive("z"));

    assertThat(originalArray.get(0).getAsJsonArray()).hasSize(1);
    assertThat(copiedArray.get(0).getAsJsonArray()).hasSize(0);
  }

  @Test
  public void testIsEmpty() {
    JsonArray array = new JsonArray();
    assertThat(array).isEmpty();

    JsonPrimitive elementA = new JsonPrimitive("a");
    array.add(elementA);
    assertThat(array).isNotEmpty();

    array.remove(0);
    assertThat(array).isEmpty();
  }

  @Test
  public void testFailedGetArrayValues() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(JsonParser.parseString("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":\"value4\"}"));

    Exception e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsBoolean());
    assertThat(e).hasMessageThat().isEqualTo("JsonObject");

    e = assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
    assertThat(e).hasMessageThat().isEqualTo("Index -1 out of bounds for length 1");

    e = assertThrows(UnsupportedOperationException.class, () -> jsonArray.getAsString());
    assertThat(e).hasMessageThat().isEqualTo("JsonObject");

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
  public void testGetAs_WrongArraySize() {
    JsonArray jsonArray = new JsonArray();
    var e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
    assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 0");

    jsonArray.add(true);
    jsonArray.add(false);
    e = assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
    assertThat(e).hasMessageThat().isEqualTo("Array must have size 1, but has size 2");
  }

  @Test
  public void testStringPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add("Hello");
    jsonArray.add("Goodbye");
    jsonArray.add("Thank you");
    jsonArray.add((String) null);
    jsonArray.add("Yes");

    assertThat(jsonArray.toString()).isEqualTo("[\"Hello\",\"Goodbye\",\"Thank you\",null,\"Yes\"]");
  }

  @Test
  public void testIntegerPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add(1);
    jsonArray.add(2);
    jsonArray.add(-3);
    jsonArray.add((Integer) null);
    jsonArray.add(4);
    jsonArray.add(0);

    assertThat(jsonArray.toString()).isEqualTo("[1,2,-3,null,4,0]");
  }

  @Test
  public void testDoublePrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add(1.0);
    jsonArray.add(2.13232);
    jsonArray.add(0.121);
    jsonArray.add((Double) null);
    jsonArray.add(-0.00234);
    jsonArray.add((Double) null);

    assertThat(jsonArray.toString()).isEqualTo("[1.0,2.13232,0.121,null,-0.00234,null]");
  }

  @Test
  public void testBooleanPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add(true);
    jsonArray.add(true);
    jsonArray.add(false);
    jsonArray.add(false);
    jsonArray.add((Boolean) null);
    jsonArray.add(true);

    assertThat(jsonArray.toString()).isEqualTo("[true,true,false,false,null,true]");
  }

  @Test
  public void testCharPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add('a');
    jsonArray.add('e');
    jsonArray.add('i');
    jsonArray.add((char) 111);
    jsonArray.add((Character) null);
    jsonArray.add('u');
    jsonArray.add("and sometimes Y");

    assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"e\",\"i\",\"o\",null,\"u\",\"and sometimes Y\"]");
  }

  @Test
  public void testMixedPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add('a');
    jsonArray.add("apple");
    jsonArray.add(12121);
    jsonArray.add((char) 111);
    jsonArray.add((Boolean) null);
    jsonArray.add((Character) null);
    jsonArray.add(12.232);
    jsonArray.add(BigInteger.valueOf(2323));

    assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"apple\",12121,\"o\",null,null,12.232,2323]");
  }

  @Test
  public void testNullPrimitiveAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add((Character) null);
    jsonArray.add((Boolean) null);
    jsonArray.add((Integer) null);
    jsonArray.add((Double) null);
    jsonArray.add((Float) null);
    jsonArray.add((BigInteger) null);
    jsonArray.add((String) null);
    jsonArray.add((Boolean) null);
    jsonArray.add((Number) null);

    assertThat(jsonArray.toString()).isEqualTo("[null,null,null,null,null,null,null,null,null]");
    for (int i = 0; i < jsonArray.size(); i++) {
      assertThat(jsonArray.get(i)).isEqualTo(JsonNull.INSTANCE);
    }
  }

  @Test
  public void testNullJsonElementAddition() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add((JsonElement) null);
    assertThat(jsonArray.get(0)).isEqualTo(JsonNull.INSTANCE);
  }

  @Test
  public void testSameAddition() {
    JsonArray jsonArray = new JsonArray();

    jsonArray.add('a');
    jsonArray.add('a');
    jsonArray.add(true);
    jsonArray.add(true);
    jsonArray.add(1212);
    jsonArray.add(1212);
    jsonArray.add(34.34);
    jsonArray.add(34.34);
    jsonArray.add((Boolean) null);
    jsonArray.add((Boolean) null);

    assertThat(jsonArray.toString()).isEqualTo("[\"a\",\"a\",true,true,1212,1212,34.34,34.34,null,null]");
  }

  @Test
  public void testToString() {
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