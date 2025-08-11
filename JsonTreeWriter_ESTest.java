/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This test suite has been refactored from an auto-generated version to use modern
 * testing practices with JUnit 5, focusing on readability and clear, behavior-driven tests.
 */
@DisplayName("JsonTreeWriter")
class JsonTreeWriterTest {

    private JsonTreeWriter writer;

    @BeforeEach
    void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @DisplayName("get() on new writer returns JsonNull")
    void get_onNewWriter_returnsJsonNull() {
        assertEquals(JsonNull.INSTANCE, writer.get());
    }

    @Nested
    @DisplayName("Top-level value writing")
    class TopLevelValueTests {

        @Test
        @DisplayName("should write a string value")
        void writeStringValue() throws IOException {
            writer.value("hello");
            assertEquals(new JsonPrimitive("hello"), writer.get());
        }

        @Test
        @DisplayName("should write a null string value as JsonNull")
        void writeNullStringValue() throws IOException {
            writer.value((String) null);
            assertEquals(JsonNull.INSTANCE, writer.get());
        }

        @Test
        @DisplayName("should write a number value")
        void writeNumberValue() throws IOException {
            writer.value(123.45);
            assertEquals(new JsonPrimitive(123.45), writer.get());
        }

        @Test
        @DisplayName("should write a null Number value as JsonNull")
        void writeNullNumberValue() throws IOException {
            writer.value((Number) null);
            assertEquals(JsonNull.INSTANCE, writer.get());
        }

        @Test
        @DisplayName("should write a boolean value")
        void writeBooleanValue() throws IOException {
            writer.value(true);
            assertEquals(new JsonPrimitive(true), writer.get());
        }

        @Test
        @DisplayName("should write a null Boolean value as JsonNull")
        void writeNullBooleanValue() throws IOException {
            writer.value((Boolean) null);
            assertEquals(JsonNull.INSTANCE, writer.get());
        }

        @Test
        @DisplayName("should write a null value directly")
        void writeNullValue() throws IOException {
            writer.nullValue();
            assertEquals(JsonNull.INSTANCE, writer.get());
        }
    }

    @Nested
    @DisplayName("Array writing")
    class ArrayTests {

        @Test
        @DisplayName("should create an empty array")
        void createEmptyArray() throws IOException {
            writer.beginArray();
            writer.endArray();
            assertEquals(new JsonArray(), writer.get());
        }

        @Test
        @DisplayName("should create an array with various primitive values")
        void createArrayWithPrimitives() throws IOException {
            writer.beginArray();
            writer.value("a");
            writer.value(1);
            writer.value(true);
            writer.nullValue();
            writer.endArray();

            JsonArray expected = new JsonArray();
            expected.add("a");
            expected.add(1);
            expected.add(true);
            expected.add(JsonNull.INSTANCE);

            assertEquals(expected, writer.get());
        }

        @Test
        @DisplayName("should create a nested array")
        void createNestedArray() throws IOException {
            writer.beginArray();
            writer.value(1);
            writer.beginArray();
            writer.value(2);
            writer.endArray();
            writer.endArray();

            JsonArray inner = new JsonArray();
            inner.add(2);
            JsonArray outer = new JsonArray();
            outer.add(1);
            outer.add(inner);

            assertEquals(outer, writer.get());
        }

        @Test
        @DisplayName("should create an array containing an object")
        void createArrayWithObject() throws IOException {
            writer.beginArray();
            writer.beginObject();
            writer.name("key").value("value");
            writer.endObject();
            writer.endArray();

            JsonObject obj = new JsonObject();
            obj.addProperty("key", "value");
            JsonArray expected = new JsonArray();
            expected.add(obj);

            assertEquals(expected, writer.get());
        }
    }

    @Nested
    @DisplayName("Object writing")
    class ObjectTests {

        @Test
        @DisplayName("should create an empty object")
        void createEmptyObject() throws IOException {
            writer.beginObject();
            writer.endObject();
            assertEquals(new JsonObject(), writer.get());
        }

        @Test
        @DisplayName("should create an object with various properties")
        void createObjectWithProperties() throws IOException {
            writer.beginObject();
            writer.name("string").value("value");
            writer.name("number").value(123);
            writer.name("boolean").value(true);
            writer.name("array").beginArray().value(1).endArray();
            writer.name("object").beginObject().name("nested").value("deep").endObject();
            writer.endObject();

            JsonObject expected = new JsonObject();
            expected.addProperty("string", "value");
            expected.addProperty("number", 123);
            expected.addProperty("boolean", true);
            JsonArray innerArray = new JsonArray();
            innerArray.add(1);
            expected.add("array", innerArray);
            JsonObject innerObject = new JsonObject();
            innerObject.addProperty("nested", "deep");
            expected.add("object", innerObject);

            assertEquals(expected, writer.get());
        }

        @Test
        @DisplayName("should write null property value when serializeNulls is true")
        void writeNullPropertyWhenSerializeNullsIsTrue() throws IOException {
            writer.setSerializeNulls(true); // Default, but explicit for clarity
            writer.beginObject();
            writer.name("a").nullValue();
            writer.endObject();

            JsonObject expected = new JsonObject();
            expected.add("a", JsonNull.INSTANCE);

            assertEquals(expected, writer.get());
        }

        @Test
        @DisplayName("should write null property value even when serializeNulls is false")
        void writeNullPropertyWhenSerializeNullsIsFalse() throws IOException {
            // JsonTreeWriter writes JsonNull regardless of this setting.
            // The setting is for stream-based writers that can omit the name/value pair.
            writer.setSerializeNulls(false);
            writer.beginObject();
            writer.name("a").nullValue();
            writer.endObject();

            JsonObject expected = new JsonObject();
            expected.add("a", JsonNull.INSTANCE);

            assertEquals(expected, writer.get());
        }
    }

    @Nested
    @DisplayName("API behavior and state management")
    class ApiAndStateTests {

        @Test
        @DisplayName("close() on a complete document should succeed")
        void closeOnCompleteDocument() throws IOException {
            writer.value("done");
            writer.close();
            assertEquals(new JsonPrimitive("done"), writer.get());
        }

        @Test
        @DisplayName("close() should make the writer unusable for subsequent writes")
        void closeMakesWriterUnusable() throws IOException {
            writer.value("done");
            writer.close();

            assertThrows(IllegalStateException.class, () -> writer.beginArray());
            assertThrows(IllegalStateException.class, () -> writer.beginObject());
            assertThrows(IllegalStateException.class, () -> writer.value("another"));
        }

        @Test
        @DisplayName("flush() should be a no-op and not throw")
        void flushIsNoOp() {
            assertDoesNotThrow(() -> writer.flush());
        }

        @Test
        @DisplayName("jsonValue() should throw UnsupportedOperationException")
        void jsonValueThrowsUnsupported() {
            assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("{\"a\":1}"));
        }
    }

    @Nested
    @DisplayName("Illegal state transitions")
    class IllegalStateTests {

        @Test
        @DisplayName("writing a value in an object without a name should fail")
        void writeValueInObjectWithoutName() throws IOException {
            writer.beginObject();
            assertThrows(IllegalStateException.class, () -> writer.value("no name"));
        }

        @Test
        @DisplayName("writing a name at the top level should fail")
        void writeNameAtTopLevel() {
            assertThrows(IllegalStateException.class, () -> writer.name("a"));
        }

        @Test
        @DisplayName("writing a name in an array should fail")
        void writeNameInArray() throws IOException {
            writer.beginArray();
            assertThrows(IllegalStateException.class, () -> writer.name("a"));
        }

        @Test
        @DisplayName("writing a name twice in an object should fail")
        void writeNameTwice() throws IOException {
            writer.beginObject();
            writer.name("a");
            assertThrows(IllegalStateException.class, () -> writer.name("b"));
        }

        @Test
        @DisplayName("ending an array when in an object should fail")
        void endArrayInObject() throws IOException {
            writer.beginObject();
            assertThrows(IllegalStateException.class, () -> writer.endArray());
        }

        @Test
        @DisplayName("ending an object when in an array should fail")
        void endObjectInArray() throws IOException {
            writer.beginArray();
            assertThrows(IllegalStateException.class, () -> writer.endObject());
        }

        @Test
        @DisplayName("ending an object before its property value is written should fail")
        void endObjectBeforeValue() throws IOException {
            writer.beginObject();
            writer.name("a");
            assertThrows(IllegalStateException.class, () -> writer.endObject());
        }

        @Test
        @DisplayName("getting the result of an incomplete document should fail")
        void getOnIncompleteDocument() throws IOException {
            writer.beginArray();
            assertThrows(IllegalStateException.class, () -> writer.get());
        }

        @Test
        @DisplayName("closing an incomplete document should fail")
        void closeOnIncompleteDocument() throws IOException {
            writer.beginArray();
            IOException e = assertThrows(IOException.class, () -> writer.close());
            assertEquals("Incomplete document", e.getMessage());
        }

        @Test
        @DisplayName("calling name(null) should fail")
        void nameNull() throws IOException {
            writer.beginObject();
            assertThrows(NullPointerException.class, () -> writer.name(null));
        }
    }
}