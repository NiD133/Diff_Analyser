package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for JsonTreeWriter class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonTreeWriter_ESTest extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Test that beginning and ending an object returns the same writer instance.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndObject() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        JsonWriter jsonWriterEnd = jsonTreeWriter.endObject();
        assertSame(jsonWriter, jsonWriterEnd);
    }

    /**
     * Test setting strictness to LENIENT and writing a boolean value.
     */
    @Test(timeout = 4000)
    public void testSetStrictnessLenientAndWriteBoolean() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriter = jsonTreeWriter.value(true);
        assertTrue(jsonWriter.isLenient());
    }

    /**
     * Test setting HTML safe mode and writing a boolean value.
     */
    @Test(timeout = 4000)
    public void testSetHtmlSafeAndWriteBoolean() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.value(true);
        assertFalse(jsonWriter.isLenient());
    }

    /**
     * Test setting serialize nulls to false and writing a boolean value.
     */
    @Test(timeout = 4000)
    public void testSetSerializeNullsFalseAndWriteBoolean() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setSerializeNulls(false);
        JsonWriter jsonWriter = jsonTreeWriter.value(false);
        assertFalse(jsonWriter.isHtmlSafe());
    }

    /**
     * Test setting strictness to LENIENT and writing a string value.
     */
    @Test(timeout = 4000)
    public void testSetStrictnessLenientAndWriteString() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriter = jsonTreeWriter.value("Kv)DQr");
        assertFalse(jsonWriter.isHtmlSafe());
    }

    /**
     * Test setting HTML safe mode and writing an empty string.
     */
    @Test(timeout = 4000)
    public void testSetHtmlSafeAndWriteEmptyString() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.value("");
        assertFalse(jsonWriter.isLenient());
    }

    /**
     * Test setting serialize nulls to false and writing a long value.
     */
    @Test(timeout = 4000)
    public void testSetSerializeNullsFalseAndWriteLong() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setSerializeNulls(false);
        JsonWriter jsonWriter = jsonTreeWriter.value(27L);
        assertFalse(jsonWriter.isLenient());
    }

    /**
     * Test setting HTML safe mode and writing a float value.
     */
    @Test(timeout = 4000)
    public void testSetHtmlSafeAndWriteFloat() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.value(-1.0F);
        assertSame(jsonWriter, jsonTreeWriter);
    }

    /**
     * Test beginning an object, setting serialize nulls to false, and writing a float value.
     */
    @Test(timeout = 4000)
    public void testBeginObjectSetSerializeNullsFalseAndWriteFloat() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        JsonWriter jsonWriter = jsonTreeWriter.name("").setSerializeNulls(false).value(331.5379F);
        assertSame(jsonTreeWriter, jsonWriter);
    }

    /**
     * Test setting HTML safe mode and writing a double value.
     */
    @Test(timeout = 4000)
    public void testSetHtmlSafeAndWriteDouble() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.value(0.0);
        assertSame(jsonTreeWriter, jsonWriter);
    }

    /**
     * Test setting serialize nulls to false and writing a negative double value.
     */
    @Test(timeout = 4000)
    public void testSetSerializeNullsFalseAndWriteNegativeDouble() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setSerializeNulls(false);
        JsonWriter jsonWriter = jsonTreeWriter.value(-6037.94604733024);
        assertEquals(Strictness.LEGACY_STRICT, jsonWriter.getStrictness());
    }

    /**
     * Test setting strictness to LENIENT and writing a null value.
     */
    @Test(timeout = 4000)
    public void testSetStrictnessLenientAndWriteNull() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriter = jsonTreeWriter.nullValue();
        assertTrue(jsonWriter.isLenient());
    }

    /**
     * Test setting HTML safe mode and writing a null value.
     */
    @Test(timeout = 4000)
    public void testSetHtmlSafeAndWriteNull() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.nullValue();
        assertEquals(Strictness.LEGACY_STRICT, jsonWriter.getStrictness());
    }

    /**
     * Test beginning an object, setting strictness to LENIENT, and writing a name.
     */
    @Test(timeout = 4000)
    public void testBeginObjectSetStrictnessLenientAndWriteName() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        jsonWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriterName = jsonWriter.name("");
        assertSame(jsonWriter, jsonWriterName);
    }

    /**
     * Test writing a double value and checking if the result is not a boolean.
     */
    @Test(timeout = 4000)
    public void testWriteDoubleAndCheckNotBoolean() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.value(495.5255);
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeWriter.get();
        assertFalse(jsonPrimitive.isBoolean());
    }

    /**
     * Test beginning and ending an object, and checking if the result is not null.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndObjectAndCheckNotNull() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonTreeWriter jsonTreeWriterBegin = (JsonTreeWriter) jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonObject jsonObject = (JsonObject) jsonTreeWriterBegin.get();
        assertFalse(jsonObject.isJsonNull());
    }

    /**
     * Test getting the JSON element and checking if it is not an array.
     */
    @Test(timeout = 4000)
    public void testGetJsonElementAndCheckNotArray() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertFalse(jsonElement.isJsonArray());
    }

    /**
     * Test beginning and ending an object with LENIENT strictness.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndObjectWithLenientStrictness() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        jsonWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriterEnd = jsonWriter.endObject();
        assertSame(jsonWriter, jsonWriterEnd);
    }

    /**
     * Test beginning and ending an object with HTML safe mode.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndObjectWithHtmlSafe() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.endObject();
        assertTrue(jsonWriter.getSerializeNulls());
    }

    /**
     * Test beginning and ending an array with LENIENT strictness.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndArrayWithLenientStrictness() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginArray();
        jsonWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriterEnd = jsonTreeWriter.endArray();
        assertTrue(jsonWriterEnd.getSerializeNulls());
    }

    /**
     * Test beginning and ending an array with HTML safe mode.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndArrayWithHtmlSafe() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.endArray();
        assertSame(jsonTreeWriter, jsonWriter);
    }

    /**
     * Test beginning an object with LENIENT strictness.
     */
    @Test(timeout = 4000)
    public void testBeginObjectWithLenientStrictness() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        assertTrue(jsonWriter.isLenient());
    }

    /**
     * Test beginning an object with HTML safe mode.
     */
    @Test(timeout = 4000)
    public void testBeginObjectWithHtmlSafe() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setHtmlSafe(true);
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        assertFalse(jsonWriter.isLenient());
    }

    /**
     * Test beginning and ending an object with serialize nulls set to false.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndObjectWithSerializeNullsFalse() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setSerializeNulls(false);
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        JsonWriter jsonWriterEnd = jsonWriter.endObject();
        assertFalse(jsonWriterEnd.getSerializeNulls());
    }

    /**
     * Test beginning an array with LENIENT strictness.
     */
    @Test(timeout = 4000)
    public void testBeginArrayWithLenientStrictness() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setStrictness(Strictness.LENIENT);
        JsonWriter jsonWriter = jsonTreeWriter.beginArray();
        assertFalse(jsonWriter.isHtmlSafe());
    }

    /**
     * Test beginning an object, setting HTML safe mode, and writing a name.
     */
    @Test(timeout = 4000)
    public void testBeginObjectSetHtmlSafeAndWriteName() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        JsonWriter jsonWriter = jsonTreeWriter.beginObject();
        jsonWriter.setHtmlSafe(true);
        jsonWriter.name("C,[}efl!G5<Uxw~gz");
        JsonWriter jsonWriterArray = jsonTreeWriter.beginArray();
        assertSame(jsonTreeWriter, jsonWriterArray);
    }

    /**
     * Test beginning and ending an array with serialize nulls set to false.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndArrayWithSerializeNullsFalse() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.setSerializeNulls(false);
        JsonWriter jsonWriter = jsonTreeWriter.beginArray();
        JsonWriter jsonWriterEnd = jsonWriter.endArray();
        assertFalse(jsonWriterEnd.isHtmlSafe());
    }

    /**
     * Test beginning an object and writing a boolean value, expecting an exception.
     */
    @Test(timeout = 4000)
    public void testBeginObjectAndWriteBooleanExpectException() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginObject();
        try {
            jsonTreeWriter.value(false);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.internal.bind.JsonTreeWriter", e);
        }
    }

    // Additional tests for other exceptions and edge cases can be added here following the same pattern.

    /**
     * Test beginning an array and ending it, checking if the result is not null.
     */
    @Test(timeout = 4000)
    public void testBeginAndEndArrayAndCheckNotNull() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement jsonElement = jsonTreeWriter.get();
        assertFalse(jsonElement.isJsonNull());
    }

    /**
     * Test flushing the writer and checking strictness.
     */
    @Test(timeout = 4000)
    public void testFlushAndCheckStrictness() throws Throwable {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        jsonTreeWriter.flush();
        assertEquals(Strictness.LEGACY_STRICT, jsonTreeWriter.getStrictness());
    }
}