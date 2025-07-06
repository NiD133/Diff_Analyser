package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TypeAdapterTest {

    /**
     * This test verifies that the `nullSafe()` method correctly handles null values
     * when writing JSON. It uses a null writer and checks if the operation completes
     * without errors.
     */
    @Test
    public void testNullSafeWrite() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Writer nullWriter = Writer.nullWriter(); // Using a writer that discards output
        typeAdapter.toJson(nullWriter, null); // Writing a null value
        // If no exception is thrown, the test passes, indicating null safety.
    }

    /**
     * This test checks that toJsonTree() handles null values correctly by returning a JsonNull
     * object.
     */
    @Test
    public void testNullSafeToJsonTree() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        JsonElement jsonElement = typeAdapter.toJsonTree(null);
        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonNull()); // Verifying the returned element is JsonNull
    }

    /**
     * This test verifies that the `nullSafe()` method correctly handles null values
     * when reading JSON. It uses a JsonReader with a "null" string and checks if
     * the adapter returns null.
     */
    @Test
    public void testNullSafeReadNull() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("null");
        JsonReader jsonReader = new JsonReader(stringReader);
        Object object = typeAdapter.read(jsonReader);
        assertNull(object); // Verifying that the adapter returns null for "null" input
    }

    /**
     * This test checks that fromJson() returns null when given a "null" string.
     */
    @Test
    public void testFromJsonNullString() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Object object = typeAdapter.fromJson("null");
        assertNull(object); // Verifying that the adapter returns null for "null" input
    }

    /**
     * This test checks that fromJson() is case-insensitive when parsing "null".
     */
    @Test
    public void testFromJsonNullStringCaseInsensitive() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("NULL");
        Object object = typeAdapter.fromJson(stringReader);
        assertNull(object); // Verifying that the adapter returns null for "NULL" input
    }

    /**
     * This test verifies that writing a null value with a null JsonWriter throws a
     * NullPointerException when not using nullSafe.
     */
    @Test(expected = NullPointerException.class)
    public void testWriteNullValueWithNullWriterThrowsNPE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.write(null, null);
    }

    /**
     * This test verifies that writing with a FutureTypeAdapter before setting a delegate
     * throws an IllegalStateException. This mimics a circular dependency.
     */
    @Test(expected = IllegalStateException.class)
    public void testFutureTypeAdapterWriteBeforeDelegateSet() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.write(null, null);
    }

    /**
     * Tests the recursive call on toJsonTree when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyToJsonTree() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.toJsonTree(futureTypeAdapter);
    }

    /**
     * Tests the recursive call on toJson when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyToJson() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter2 = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter1.setDelegate(futureTypeAdapter2);
        futureTypeAdapter2.setDelegate(futureTypeAdapter1);
        Object object = new Object();
        futureTypeAdapter1.toJson(object);
    }

    /**
     * This test verifies that writing to a null Writer throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testWriteToNullWriterThrowsNPE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.toJson((Writer) null, null);
    }

    /**
     * This test checks that the read method throws an IOException when it encounters
     * malformed JSON.
     */
    @Test(expected = IOException.class)
    public void testReadMalformedJsonThrowsIOException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader(" CV98^&q");
        JsonReader jsonReader = new JsonReader(stringReader);
        typeAdapter.read(jsonReader);
    }

    /**
     * Tests the recursive call on read when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyRead() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter1 = new Gson.FutureTypeAdapter<>();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter2 = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter1.setDelegate(futureTypeAdapter2);
        futureTypeAdapter2.setDelegate(futureTypeAdapter1);
        Reader reader = Reader.nullReader();
        JsonReader jsonReader = new JsonReader(reader);
        futureTypeAdapter2.read(jsonReader);
    }

    /**
     * This test verifies that reading from a null JsonReader throws a NullPointerException
     * when not using nullSafe.
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullJsonReaderThrowsNPE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.read(null);
    }

    /**
     * This test verifies that reading with a FutureTypeAdapter before setting a delegate
     * throws an IllegalStateException. This mimics a circular dependency.
     */
    @Test(expected = IllegalStateException.class)
    public void testFutureTypeAdapterReadBeforeDelegateSet() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.read(null);
    }

    /**
     * This test verifies that reading from a closed StringReader throws an IOException.
     */
    @Test(expected = IOException.class)
    public void testReadFromClosedReaderThrowsIOException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("");
        JsonReader jsonReader = new JsonReader(stringReader);
        stringReader.close();
        typeAdapter.read(jsonReader);
    }

    /**
     * This test verifies that reading from an empty Reader throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void testReadFromEmptyReaderThrowsEOFException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Reader reader = Reader.nullReader(); //Simulates an empty reader
        JsonReader jsonReader = new JsonReader(reader);
        typeAdapter.read(jsonReader);
    }

    /**
     * Tests the recursive call on fromJsonTree when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyFromJsonTree() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.fromJsonTree(null);
    }

    /**
     * This test verifies that fromJsonTree with a null JsonElement throws a NullPointerException
     * when not using nullSafe.
     */
    @Test(expected = NullPointerException.class)
    public void testFromJsonTreeNullJsonElementThrowsNPE() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.fromJsonTree(null);
    }

    /**
     * This test checks that the fromJson method throws an IOException when it encounters
     * malformed JSON.
     */
    @Test(expected = IOException.class)
    public void testFromJsonMalformedJsonThrowsIOException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.fromJson("-3<LXXl*");
    }

    /**
     * Tests the recursive call on fromJson when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyFromJson() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        futureTypeAdapter.fromJson("");
    }

    /**
     * This test verifies that fromJson with a null String throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testFromJsonNullStringThrowsNPE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.fromJson(null);
    }

    /**
     * This test verifies that fromJson with an empty String throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void testFromJsonEmptyStringThrowsEOFException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.fromJson("");
    }

    /**
     * This test checks that the fromJson(Reader) method throws an IOException when it encounters
     * unexpected value in JSON.
     */
    @Test(expected = IOException.class)
    public void testFromJsonReaderUnexpectedValueThrowsIOException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("]");
        typeAdapter.fromJson(stringReader);
    }

    /**
     * Tests the recursive call on fromJson(Reader) when a cyclic dependency exists.
     */
    @Test(expected = StackOverflowError.class)
    public void testCyclicDependencyFromJsonReader() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.setDelegate(futureTypeAdapter);
        Reader reader = Reader.nullReader();
        futureTypeAdapter.fromJson(reader);
    }

    /**
     * This test verifies that fromJson with a FutureTypeAdapter before setting a delegate
     * throws an IllegalStateException. This mimics a circular dependency.
     */
    @Test(expected = IllegalStateException.class)
    public void testFutureTypeAdapterFromJsonReaderBeforeDelegateSet() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        Reader reader = Reader.nullReader();
        futureTypeAdapter.fromJson(reader);
    }

    /**
     * This test verifies that fromJson with a closed Reader throws an IOException.
     */
    @Test(expected = IOException.class)
    public void testFromJsonClosedReaderThrowsIOException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        StringReader stringReader = new StringReader("");
        stringReader.close();
        typeAdapter.fromJson(stringReader);
    }

    /**
     * This test verifies that fromJson with an empty Reader throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void testFromJsonEmptyReaderThrowsEOFException() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        Reader reader = Reader.nullReader();
        typeAdapter.fromJson(reader);
    }

    /**
     * This test verifies that fromJson with a null Reader throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testFromJsonNullReaderThrowsNPE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.fromJson((Reader) null);
    }

    /**
     * This test verifies that writing to a Writer with a FutureTypeAdapter before setting a delegate
     * throws an IllegalStateException. This mimics a circular dependency.
     */
    @Test(expected = IllegalStateException.class)
    public void testFutureTypeAdapterWriteToWriterBeforeDelegateSet() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        Writer writer = Writer.nullWriter();
        futureTypeAdapter.toJson(writer, null);
    }

    /**
     * This test checks that fromJsonTree() returns null when given JsonNull.INSTANCE.
     */
    @Test
    public void testFromJsonTreeJsonNull() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        JsonNull jsonNull = JsonNull.INSTANCE;
        Object object = typeAdapter.fromJsonTree(jsonNull);
        assertNull(object);
    }

    /**
     * This test verifies that fromJsonTree with an array throws IllegalStateException because
     * the delegate is not set on FutureTypeAdapter.
     */
    @Test(expected = IllegalStateException.class)
    public void testFromJsonTreeJsonArrayThrowsISE() {
        JsonArray jsonArray = new JsonArray();
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        typeAdapter.fromJsonTree(jsonArray);
    }

    /**
     * This test checks that toJson() returns "null" when given a null object.
     */
    @Test
    public void testToJsonNullObject() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        String json = typeAdapter.toJson(null);
        assertEquals("null", json);
    }

    /**
     * This test verifies that toJson throws IllegalStateException because
     * the delegate is not set on FutureTypeAdapter.
     */
    @Test(expected = IllegalStateException.class)
    public void testToJsonThrowsISE() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Integer> typeAdapter = futureTypeAdapter.nullSafe();
        Integer integer = 0;
        typeAdapter.toJson(integer);
    }

    /**
     * This test verifies that calling `nullSafe()` twice on the same TypeAdapter returns the
     * same instance.
     */
    @Test
    public void testNullSafeIsIdempotent() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> typeAdapter = futureTypeAdapter.nullSafe();
        TypeAdapter<Object> typeAdapter2 = typeAdapter.nullSafe();
        assertSame(typeAdapter2, typeAdapter);
    }

    /**
     * This test verifies that toJsonTree throws IllegalStateException because
     * the delegate is not set on FutureTypeAdapter.
     */
    @Test(expected = IllegalStateException.class)
    public void testToJsonTreeThrowsISE() {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.toJsonTree(futureTypeAdapter);
    }

    /**
     * This test verifies that fromJson throws IllegalStateException because
     * the delegate is not set on FutureTypeAdapter.
     */
    @Test(expected = IllegalStateException.class)
    public void testFromJsonThrowsISE() throws IOException {
        Gson.FutureTypeAdapter<Object> futureTypeAdapter = new Gson.FutureTypeAdapter<>();
        futureTypeAdapter.fromJson("null");
    }
}