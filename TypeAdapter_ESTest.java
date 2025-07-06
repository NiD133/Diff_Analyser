package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TypeAdapter_ESTest extends TypeAdapter_ESTest_scaffolding {

    private static final String NULL_STRING = "null";
    private static final String EMPTY_STRING = "";
    private static final String MALFORMED_JSON = " CV98^&q";
    private static final String INVALID_JSON = "-3<LXXl*";

    @Test(timeout = 4000)
    public void testNullSafeToJsonWithNullValue() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Writer writer = Writer.nullWriter();
        typeAdapter.toJson(writer, null);
    }

    @Test(timeout = 4000)
    public void testNullSafeToJsonTreeWithNullValue() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        JsonElement jsonElement = typeAdapter.toJsonTree(null);
        assertFalse(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testNullSafeReadWithNullString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader(NULL_STRING);
        JsonReader jsonReader = new JsonReader(stringReader);
        Object result = typeAdapter.read(jsonReader);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithNullString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Object result = typeAdapter.fromJson(NULL_STRING);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("NULL");
        Object result = typeAdapter.fromJson(stringReader);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNullSafeWriteWithNullJsonWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.write(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.TypeAdapter$NullSafeTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterWriteWithNullJsonWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.write(null, null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterToJsonTreeWithSelfDelegate() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.toJsonTree(futureTypeAdapter);
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterToJsonWithCyclicDependency() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter0 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter1 = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter1.setDelegate(futureTypeAdapter0);
        futureTypeAdapter0.setDelegate(futureTypeAdapter1);
        Object object = new Object();
        futureTypeAdapter0.toJson(object);
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterToJsonWithNullWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.toJson(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeReadWithMalformedJson() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader(MALFORMED_JSON);
        JsonReader jsonReader = new JsonReader(stringReader);
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterReadWithCyclicDependency() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter0 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter1 = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter1.setDelegate(futureTypeAdapter0);
        futureTypeAdapter0.setDelegate(futureTypeAdapter1);
        Reader reader = Reader.nullReader();
        JsonReader jsonReader = new JsonReader(reader);
        futureTypeAdapter1.read(jsonReader);
    }

    @Test(timeout = 4000)
    public void testNullSafeReadWithNullJsonReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.read(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.TypeAdapter$NullSafeTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterReadWithNullJsonReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.read(null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeReadWithClosedReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader(EMPTY_STRING);
        JsonReader jsonReader = new JsonReader(stringReader);
        stringReader.close();
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeReadWithEOF() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Reader reader = Reader.nullReader();
        JsonReader jsonReader = new JsonReader(reader);
        try {
            typeAdapter.read(jsonReader);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonTreeWithSelfDelegate() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.fromJsonTree(null);
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonTreeWithNullElement() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJsonTree(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.bind.JsonTreeReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithInvalidJson() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJson(INVALID_JSON);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonWithSelfDelegate() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.fromJson(EMPTY_STRING);
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonWithNullString() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.fromJson((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithEOF() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJson(EMPTY_STRING);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithUnexpectedValue() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("]");
        try {
            typeAdapter.fromJson(stringReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonWithSelfDelegateReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        Reader reader = Reader.nullReader();
        futureTypeAdapter.fromJson(reader);
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonWithNullReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.fromJson((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithClosedReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader(EMPTY_STRING);
        stringReader.close();
        try {
            typeAdapter.fromJson(stringReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonWithEOFReader() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Reader reader = Reader.nullReader();
        try {
            typeAdapter.fromJson(reader);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterToJsonWithNullWriter() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        Writer writer = Writer.nullWriter();
        try {
            futureTypeAdapter.toJson(writer, null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonTreeWithJsonNull() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        JsonNull jsonNull = JsonNull.INSTANCE;
        Object result = typeAdapter.fromJsonTree(jsonNull);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNullSafeFromJsonTreeWithJsonArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        try {
            typeAdapter.fromJsonTree(jsonArray);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeToJsonWithNullObject() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        String result = typeAdapter.toJson(null);
        assertEquals("null", result);
    }

    @Test(timeout = 4000)
    public void testNullSafeToJsonWithInteger() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Integer integer = 0;
        try {
            typeAdapter.toJson(integer);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullSafeTypeAdapterIsSame() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        TypeAdapter<Object> sameTypeAdapter = typeAdapter.nullSafe();
        assertSame(sameTypeAdapter, typeAdapter);
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterToJsonTreeWithCyclicDependency() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.toJsonTree(futureTypeAdapter);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testFutureTypeAdapterFromJsonWithCyclicDependency() throws Throwable {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        try {
            futureTypeAdapter.fromJson(NULL_STRING);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }
}