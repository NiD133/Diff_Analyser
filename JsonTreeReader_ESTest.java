package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for JsonTreeReader.
 *
 * These tests intentionally avoid mutating the underlying JsonElement structure
 * while iterating (which can cause ConcurrentModificationException) and focus on
 * clear, deterministic reader behavior.
 */
public class JsonTreeReaderTest {

  // --------- peek() behavior ---------

  @Test
  public void peek_onNullPrimitive_returnsNULL() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertEquals(JsonToken.NULL, reader.peek());
  }

  @Test
  public void peek_onBooleanPrimitive_returnsBOOLEAN() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
    assertEquals(JsonToken.BOOLEAN, reader.peek());
  }

  @Test
  public void peek_onNumberPrimitive_returnsNUMBER() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(42));
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test
  public void peek_onStringPrimitive_returnsSTRING() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("gson"));
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void peek_onArray_returnsBEGIN_ARRAY() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void peek_onObject_returnsBEGIN_OBJECT() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void peek_afterBeginArray_onEmptyArray_returnsEND_ARRAY() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void peek_afterBeginObject_onEmptyObject_returnsEND_OBJECT() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  // --------- begin*/end* and hasNext() ---------

  @Test
  public void beginArray_thenEndArray_onEmptyArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray(); // should not throw
  }

  @Test
  public void beginObject_thenEndObject_onEmptyObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.endObject(); // should not throw
  }

  @Test
  public void hasNext_onArrayWithOneElement() throws Exception {
    JsonArray array = new JsonArray();
    array.add(true);
    JsonTreeReader reader = new JsonTreeReader(array);
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertTrue(reader.nextBoolean());
    assertFalse(reader.hasNext());
    reader.endArray();
  }

  // --------- nextName / promoteNameToValue ---------

  @Test
  public void nextName_readsFirstPropertyName() throws Exception {
    JsonObject obj = new JsonObject();
    obj.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(obj);
    reader.beginObject();
    assertEquals("name", reader.nextName());
  }

  @Test
  public void promoteNameToValue_allowsReadingObjectNameAsString() throws Exception {
    JsonObject obj = new JsonObject();
    obj.addProperty("h", "h");
    JsonTreeReader reader = new JsonTreeReader(obj);
    reader.beginObject();
    reader.promoteNameToValue();
    assertEquals("h", reader.nextString());
  }

  @Test
  public void promoteNameToValue_allowsReadingNumericNameAsInt() throws Exception {
    JsonObject obj = new JsonObject();
    obj.addProperty("5", "5");
    JsonTreeReader reader = new JsonTreeReader(obj);
    reader.beginObject();
    reader.promoteNameToValue();
    assertEquals(5, reader.nextInt());
  }

  // --------- next* on primitives ---------

  @Test
  public void nextBoolean_readsBooleanPrimitive() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(false));
    assertFalse(reader.nextBoolean());
  }

  @Test
  public void nextString_readsStringPrimitive() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("hello"));
    assertEquals("hello", reader.nextString());
  }

  @Test
  public void nextString_readsNumberAsString() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123));
    assertEquals("123", reader.nextString());
  }

  @Test
  public void nextInt_readsIntPrimitive() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(7));
    assertEquals(7, reader.nextInt());
  }

  @Test
  public void nextLong_readsLongPrimitive() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(1234567890123L));
    assertEquals(1234567890123L, reader.nextLong());
  }

  @Test
  public void nextDouble_readsDoublePrimitive() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(3.14));
    assertEquals(3.14, reader.nextDouble(), 0.0);
  }

  @Test(expected = IOException.class)
  public void nextDouble_rejectsInfinityInStrictMode() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("-Infinity"));
    reader.nextDouble(); // strict by default -> IOException
  }

  // --------- type mismatch errors ---------

  @Test(expected = IllegalStateException.class)
  public void nextString_onArray_throws() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void nextName_inArrayScope_throws() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.nextName();
  }

  @Test(expected = IllegalStateException.class)
  public void beginArray_onNull_throws() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void beginObject_onNull_throws() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.beginObject();
  }

  // --------- skipValue ---------

  @Test
  public void skipValue_skipsSingleNameAndValueInObject() throws Exception {
    JsonObject obj = new JsonObject();
    obj.addProperty("a", 1);
    JsonTreeReader reader = new JsonTreeReader(obj);

    reader.beginObject();
    // At NAME token -> skip should skip name and value
    reader.skipValue();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
  }

  // --------- paths and toString ---------

  @Test
  public void getPath_atRoot_isDollar() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    assertEquals("$", reader.getPath());
  }

  @Test
  public void getPreviousPath_afterReadingFirstArrayElement() throws Exception {
    JsonArray array = new JsonArray();
    array.add(true);
    JsonTreeReader reader = new JsonTreeReader(array);

    reader.beginArray();
    reader.nextBoolean();
    assertEquals("$[0]", reader.getPreviousPath());
    reader.endArray();
  }

  @Test
  public void toString_containsPath() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertEquals("JsonTreeReader at path $", reader.toString());
  }

  // --------- close() ---------

  @Test
  public void close_disallowsFurtherUse() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.close();
    try {
      reader.peek();
      fail("peek() after close() should throw IllegalStateException");
    } catch (IllegalStateException expected) {
      // ok
    }
  }

  // --------- nextJsonElement() ---------

  @Test
  public void nextJsonElement_onPrimitive_returnsSameElement() throws Exception {
    JsonPrimitive prim = new JsonPrimitive(10);
    JsonTreeReader reader = new JsonTreeReader(prim);
    JsonElement element = reader.nextJsonElement();
    assertTrue(element.isJsonPrimitive());
    assertEquals(10, element.getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void nextJsonElement_onEmptyArrayAfterBegin_throws() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.nextJsonElement();
  }

  // --------- defaults ---------

  @Test
  public void defaultNestingLimit_is255() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertEquals(255, reader.getNestingLimit());
  }

  @Test
  public void defaultStrictness_isLegacyStrict_andNotLenient() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    assertEquals(Strictness.LEGACY_STRICT, reader.getStrictness());
    assertFalse(reader.isLenient());
  }
}