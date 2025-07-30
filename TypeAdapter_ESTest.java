package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for testing the behavior of the Gson FutureTypeAdapter.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TypeAdapter_ESTest extends TypeAdapter_ESTest_scaffolding {

    /**
     * Test that nullSafe() correctly serializes a null object to "null".
     */
    @Test(timeout = 4000)
    public void testNullSerialization() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, null);
        assertEquals("null", writer.toString());
    }

    /**
     * Test that nullSafe() correctly serializes a null Integer to a JsonElement that is not a JsonPrimitive.
     */
    @Test(timeout = 4000)
    public void testNullIntegerToJsonTree() throws Throwable {
        Gson.FutureTypeAdapter<Integer> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> typeAdapter = futureTypeAdapter.nullSafe();
        JsonElement jsonElement = typeAdapter.toJsonTree(null);
        assertFalse(jsonElement.isJsonPrimitive());
    }

    /**
     * Test that nullSafe() correctly serializes a null object to "null" as a String.
     */
    @Test(timeout = 4000)
    public void testNullToJsonString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        String jsonString = typeAdapter.toJson(null);
        assertEquals("null", jsonString);
    }

    /**
     * Test that nullSafe() correctly deserializes "null" to a null object.
     */
    @Test(timeout = 4000)
    public void testNullDeserialization() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("null");
        JsonReader jsonReader = new JsonReader(reader);
        Object result = typeAdapter.read(jsonReader);
        assertNull(result);
    }

    /**
     * Test that nullSafe() correctly deserializes a JsonNull to a null object.
     */
    @Test(timeout = 4000)
    public void testJsonNullDeserialization() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        JsonNull jsonNull = JsonNull.INSTANCE;
        Object result = typeAdapter.fromJsonTree(jsonNull);
        assertNull(result);
    }

    /**
     * Test that nullSafe() correctly deserializes "null" from a Reader to a null object.
     */
    @Test(timeout = 4000)
    public void testReaderNullDeserialization() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("null");
        Object result = typeAdapter.fromJson(reader);
        assertNull(result);
    }

    /**
     * Test that an IllegalStateException is thrown when write() is called before the delegate is set.
     */
    @Test(timeout = 4000)
    public void testWriteBeforeDelegateSet() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.write(null, null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    /**
     * Test cyclic dependency between two FutureTypeAdapters.
     */
    @Test(timeout = 4000)
    public void testCyclicDependency() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter2 = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter1.setDelegate(futureTypeAdapter2);
        futureTypeAdapter2.setDelegate(futureTypeAdapter1);
        try {
            futureTypeAdapter1.toJsonTree(futureTypeAdapter1);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    /**
     * Test that a NullPointerException is thrown when toJson() is called with a null Writer.
     */
    @Test(timeout = 4000)
    public void testToJsonWithNullWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.toJson(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Test that an IOException is thrown when reading malformed JSON.
     */
    @Test(timeout = 4000)
    public void testMalformedJsonReading() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("MZ8");
        JsonReader jsonReader = new JsonReader(reader);
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    /**
     * Test that a NullPointerException is thrown when read() is called with a null JsonReader.
     */
    @Test(timeout = 4000)
    public void testReadWithNullJsonReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.read(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.TypeAdapter$NullSafeTypeAdapter", e);
        }
    }

    /**
     * Test that an EOFException is thrown when reading from a closed Reader.
     */
    @Test(timeout = 4000)
    public void testReadFromClosedReader() throws Throwable {
        Gson.FutureTypeAdapter<Integer> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("");
        reader.close();
        JsonReader jsonReader = new JsonReader(reader);
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    /**
     * Test that an EOFException is thrown when reading from an empty Reader.
     */
    @Test(timeout = 4000)
    public void testReadFromEmptyReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("");
        JsonReader jsonReader = new JsonReader(reader);
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    /**
     * Test that a NullPointerException is thrown when fromJson() is called with a null String.
     */
    @Test(timeout = 4000)
    public void testFromJsonWithNullString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.fromJson((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    /**
     * Test that an EOFException is thrown when fromJson() is called with an empty String.
     */
    @Test(timeout = 4000)
    public void testFromJsonWithEmptyString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJson("");
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    /**
     * Test that an IOException is thrown when fromJson() is called with malformed JSON.
     */
    @Test(timeout = 4000)
    public void testFromJsonWithMalformedJson() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader reader = new StringReader("com.google.gson.TypeAdapter$1");
        try {
            typeAdapter.fromJson(reader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    /**
     * Test that a NullPointerException is thrown when fromJson() is called with a null Reader.
     */
    @Test(timeout = 4000)
    public void testFromJsonWithNullReader() throws Throwable {
        Gson.FutureTypeAdapter<Integer> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.fromJson((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Test that nullSafe() correctly deserializes "null" from a String to a null object.
     */
    @Test(timeout = 4000)
    public void testFromJsonStringNull() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Object result = typeAdapter.fromJson("null");
        assertNull(result);
    }

    /**
     * Test that an IllegalStateException is thrown when fromJsonTree() is called before the delegate is set.
     */
    @Test(timeout = 4000)
    public void testFromJsonTreeBeforeDelegateSet() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        Character character = 'B';
        JsonPrimitive jsonPrimitive = new JsonPrimitive(character);
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJsonTree(jsonPrimitive);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    /**
     * Test that a NullPointerException is thrown when write() is called with a null JsonWriter.
     */
    @Test(timeout = 4000)
    public void testWriteWithNullJsonWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.write(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.TypeAdapter$NullSafeTypeAdapter", e);
        }
    }

    /**
     * Test that an IllegalStateException is thrown when toJsonTree() is called before the delegate is set.
     */
    @Test(timeout = 4000)
    public void testToJsonTreeBeforeDelegateSet() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.toJsonTree(futureTypeAdapter);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    /**
     * Test that nullSafe() returns the same instance when called multiple times.
     */
    @Test(timeout = 4000)
    public void testNullSafeReturnsSameInstance() throws Throwable {
        Gson.FutureTypeAdapter<Integer> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> typeAdapter = futureTypeAdapter.nullSafe();
        TypeAdapter<Integer> sameTypeAdapter = typeAdapter.nullSafe();
        assertSame(sameTypeAdapter, typeAdapter);
    }
}