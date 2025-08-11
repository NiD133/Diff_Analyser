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

package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link TypeAdapter}.
 * The tests use {@link Gson.FutureTypeAdapter} as a concrete, testable
 * implementation of the abstract {@code TypeAdapter} class.
 */
public class TypeAdapterTest {

    // A concrete TypeAdapter implementation for testing.
    // It delegates calls to another adapter, which can be set later.
    private Gson.FutureTypeAdapter<Object> futureAdapter;

    @Before
    public void setUp() {
        futureAdapter = new Gson.FutureTypeAdapter<>();
    }

    // --- Tests for NullSafeTypeAdapter ---

    @Test
    public void nullSafeAdapter_toJsonWithWriter_writesNullForNullInput() throws IOException {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        StringWriter stringWriter = new StringWriter();
        nullSafeAdapter.toJson((Writer) stringWriter, null);
        assertEquals("null", stringWriter.toString());
    }

    @Test
    public void nullSafeAdapter_toJsonTree_returnsJsonNullForNullInput() {
        TypeAdapter<Integer> nullSafeAdapter = new Gson.FutureTypeAdapter<Integer>().nullSafe();
        JsonElement jsonElement = nullSafeAdapter.toJsonTree(null);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test
    public void nullSafeAdapter_toJson_returnsNullStringForNullInput() {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        String json = nullSafeAdapter.toJson(null);
        assertEquals("null", json);
    }

    @Test
    public void nullSafeAdapter_read_returnsNullForNullToken() throws IOException {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        JsonReader jsonReader = new JsonReader(new StringReader("null"));
        Object result = nullSafeAdapter.read(jsonReader);
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_fromJsonTree_returnsNullForJsonNull() {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        Object result = nullSafeAdapter.fromJsonTree(JsonNull.INSTANCE);
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_fromJsonWithReader_returnsNullForNullString() throws IOException {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        Object result = nullSafeAdapter.fromJson(new StringReader("null"));
        assertNull(result);
    }

    @Test
    public void nullSafeAdapter_fromJsonString_returnsNullForNullString() throws IOException {
        TypeAdapter<Object> nullSafeAdapter = futureAdapter.nullSafe();
        Object result = nullSafeAdapter.fromJson("null");
        assertNull(result);
    }

    @Test
    public void nullSafe_whenCalledTwice_returnsSameInstance() {
        TypeAdapter<Integer> nullSafeAdapter = new Gson.FutureTypeAdapter<Integer>().nullSafe();
        assertSame("Calling nullSafe() multiple times should return the same instance",
            nullSafeAdapter, nullSafeAdapter.nullSafe());
    }

    // --- Tests for Delegate Not Set (IllegalStateException) ---

    @Test(expected = IllegalStateException.class)
    public void write_withoutDelegate_throwsIllegalStateException() throws IOException {
        futureAdapter.write(new JsonWriter(new StringWriter()), null);
    }

    @Test(expected = IllegalStateException.class)
    public void read_withoutDelegate_throwsIllegalStateException() throws IOException {
        futureAdapter.read(new JsonReader(new StringReader("\"test\"")));
    }

    @Test(expected = IllegalStateException.class)
    public void toJson_withoutDelegate_throwsIllegalStateException() {
        futureAdapter.toJson(new Object());
    }

    @Test(expected = IllegalStateException.class)
    public void toJsonWithWriter_withoutDelegate_throwsIllegalStateException() throws IOException {
        // The value (null or non-null) does not matter as the exception is thrown before it's used.
        futureAdapter.toJson(new StringWriter(), new Object());
    }

    @Test(expected = IllegalStateException.class)
    public void toJsonTree_withoutDelegate_throwsIllegalStateException() {
        futureAdapter.toJsonTree(new Object());
    }

    @Test(expected = IllegalStateException.class)
    public void fromJsonString_withoutDelegate_throwsIllegalStateException() throws IOException {
        futureAdapter.fromJson("\"test\"");
    }

    @Test(expected = IllegalStateException.class)
    public void fromJsonReader_withoutDelegate_throwsIllegalStateException() throws IOException {
        futureAdapter.fromJson(new StringReader("\"test\""));
    }

    @Test(expected = IllegalStateException.class)
    public void fromJsonTree_withoutDelegate_throwsIllegalStateException() {
        futureAdapter.fromJsonTree(new JsonPrimitive("test"));
    }

    // --- Tests for Cyclic Dependencies (StackOverflowError) ---

    @Test(expected = StackOverflowError.class)
    public void toJson_withSelfReferencingDelegate_throwsStackOverflowError() {
        futureAdapter.setDelegate(futureAdapter);
        futureAdapter.toJson(new Object());
    }



    @Test(expected = StackOverflowError.class)
    public void toJsonTree_withCyclicDependency_throwsStackOverflowError() {
        Gson.FutureTypeAdapter<Object> anotherAdapter = new Gson.FutureTypeAdapter<>();
        futureAdapter.setDelegate(anotherAdapter);
        anotherAdapter.setDelegate(futureAdapter);
        futureAdapter.toJsonTree(new Object());
    }

    @Test(expected = StackOverflowError.class)
    public void read_withSelfReferencingDelegate_throwsStackOverflowError() throws IOException {
        futureAdapter.setDelegate(futureAdapter);
        // Reader can be null as the recursion happens before it's used.
        futureAdapter.read(null);
    }

    @Test(expected = StackOverflowError.class)
    public void fromJsonTree_withSelfReferencingDelegate_throwsStackOverflowError() {
        futureAdapter.setDelegate(futureAdapter);
        futureAdapter.fromJsonTree(JsonNull.INSTANCE);
    }

    @Test(expected = StackOverflowError.class)
    public void fromJsonString_withCyclicDependency_throwsStackOverflowError() throws IOException {
        Gson.FutureTypeAdapter<Integer> adapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Integer> adapter2 = new Gson.FutureTypeAdapter<>();
        adapter1.setDelegate(adapter2);
        adapter2.setDelegate(adapter1);
        adapter1.fromJson("\"test\"");
    }

    @Test(expected = StackOverflowError.class)
    public void fromJsonReader_withSelfReferencingDelegate_throwsStackOverflowError() throws IOException {
        futureAdapter.setDelegate(futureAdapter);
        futureAdapter.fromJson(new StringReader("\"test\""));
    }

    // --- Tests for Invalid Input and IO Errors ---

    @Test(expected = NullPointerException.class)
    public void toJson_withNullWriter_throwsNullPointerException() throws IOException {
        futureAdapter.toJson(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void fromJsonString_withNullString_throwsNullPointerException() throws IOException {
        futureAdapter.fromJson((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void fromJsonReader_withNullReader_throwsNullPointerException() throws IOException {
        futureAdapter.fromJson((Reader) null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_read_withNullReader_throwsNullPointerException() throws IOException {
        futureAdapter.nullSafe().read(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_write_withNullWriter_throwsNullPointerException() throws IOException {
        futureAdapter.nullSafe().write(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void nullSafeAdapter_fromJsonTree_withNullJsonElement_throwsNullPointerException() {
        futureAdapter.nullSafe().fromJsonTree(null);
    }

    @Test(expected = IOException.class)
    public void fromJsonString_withMalformedJson_throwsIOException() throws IOException {
        futureAdapter.nullSafe().fromJson("not-a-valid-json");
    }

    @Test(expected = EOFException.class)
    public void fromJsonString_withEmptyString_throwsEOFException() throws IOException {
        futureAdapter.nullSafe().fromJson("");
    }

    @Test(expected = IOException.class)
    public void read_fromClosedReader_throwsIOException() throws IOException {
        StringReader reader = new StringReader("\"test\"");
        reader.close();
        futureAdapter.nullSafe().read(new JsonReader(reader));
    }

    @Test(expected = EOFException.class)
    public void read_fromEmptyReader_throwsEOFException() throws IOException {
        futureAdapter.nullSafe().read(new JsonReader(new StringReader("")));
    }
}