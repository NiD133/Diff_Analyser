package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.io.StringReader;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonParser_ESTest extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testParseEmptyStringAsJsonNull() throws Throwable {
        JsonElement jsonElement = JsonParser.parseString("");
        assertTrue(jsonElement.isJsonNull());
    }

    @Test(timeout = 4000)
    public void testParseStringAsJsonArray() throws Throwable {
        JsonElement jsonElement = JsonParser.parseString("[GoyG.G]");
        assertTrue(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonPrimitive() throws Throwable {
        StringReader reader = new StringReader("MUB_qz@d");
        JsonPrimitive jsonPrimitive = (JsonPrimitive) JsonParser.parseReader(reader);
        assertFalse(jsonPrimitive.isBoolean());
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonObject() throws Throwable {
        StringReader reader = new StringReader("{}3+1M-tu;%QX!~Y");
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        assertFalse(jsonElement.isJsonNull());
    }

    @Test(timeout = 4000)
    public void testParseReaderWithTrailingData() throws Throwable {
        StringReader reader = new StringReader("[GoyG.G]");
        JsonParser.parseReader(reader); // Consumes the reader
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test(timeout = 4000)
    public void testParseEmptyStringUsingDeprecatedMethod() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse("");
        assertTrue(jsonElement.isJsonNull());
    }

    @Test(timeout = 4000)
    public void testParseStringAsJsonPrimitiveNotNumber() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonParser.parse("N");
        assertFalse(jsonPrimitive.isNumber());
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonPrimitiveNotBoolean() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("M(");
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonParser.parse(reader);
        assertFalse(jsonPrimitive.isBoolean());
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonObject() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("{}/GH6t)T:8sOB3 #");
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        assertTrue(jsonElement.isJsonObject());
    }

    @Test(timeout = 4000)
    public void testParseReaderWithInvalidJson() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("|h|[8&ZGQ1Fbg]Xp");
        JsonReader jsonReader = new JsonReader(reader);
        jsonParser.parse(jsonReader); // Consumes the reader
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        assertFalse(jsonElement.isJsonObject());
    }

    @Test(timeout = 4000)
    public void testParseNullStringThrowsNullPointerException() throws Throwable {
        try {
            JsonParser.parseString(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseMalformedJsonStringThrowsRuntimeException() throws Throwable {
        try {
            JsonParser.parseString("lP ?");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.JsonParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullReaderThrowsNullPointerException() throws Throwable {
        try {
            JsonParser.parseReader((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderWithInvalidJsonThrowsRuntimeException() throws Throwable {
        StringReader reader = new StringReader("=PC x;DM1DK}c/;");
        try {
            JsonParser.parseReader(reader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.internal.Streams", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullJsonReaderThrowsNullPointerException() throws Throwable {
        try {
            JsonParser.parseReader((JsonReader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.JsonParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderTwiceThrowsIllegalStateException() throws Throwable {
        StringReader reader = new StringReader("R3P?4");
        JsonReader jsonReader = new JsonReader(reader);
        JsonParser.parseReader(jsonReader); // Consumes the reader
        try {
            JsonParser.parseReader(jsonReader);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.internal.bind.JsonElementTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullStringUsingDeprecatedMethodThrowsNullPointerException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        try {
            jsonParser.parse((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseMalformedJsonStringUsingDeprecatedMethodThrowsRuntimeException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        try {
            jsonParser.parse("Did not consume the entire document.");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.JsonParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullReaderUsingDeprecatedMethodThrowsNullPointerException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        try {
            jsonParser.parse((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderWithInvalidJsonUsingDeprecatedMethodThrowsRuntimeException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("}Yz");
        try {
            jsonParser.parse(reader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.internal.Streams", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullJsonReaderUsingDeprecatedMethodThrowsNullPointerException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        try {
            jsonParser.parse((JsonReader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.JsonParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderTwiceUsingDeprecatedMethodThrowsIllegalStateException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("?3p");
        JsonReader jsonReader = new JsonReader(reader);
        jsonParser.parse(jsonReader); // Consumes the reader
        try {
            jsonParser.parse(jsonReader);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.gson.internal.bind.JsonElementTypeAdapter", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderWithInvalidJsonUsingDeprecatedMethodThrowsRuntimeException() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        StringReader reader = new StringReader("}pm");
        JsonReader jsonReader = new JsonReader(reader);
        try {
            jsonParser.parse(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.internal.Streams", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonArray() throws Throwable {
        StringReader reader = new StringReader("[GoyG.G]");
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        assertFalse(jsonElement.isJsonPrimitive());
    }

    @Test(timeout = 4000)
    public void testParseEmptyReaderAsJsonArray() throws Throwable {
        StringReader reader = new StringReader("");
        JsonElement jsonElement = JsonParser.parseReader(reader);
        assertFalse(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testParseStringAsJsonPrimitiveNotNumber() throws Throwable {
        JsonPrimitive jsonPrimitive = (JsonPrimitive) JsonParser.parseString("A");
        assertFalse(jsonPrimitive.isNumber());
    }

    @Test(timeout = 4000)
    public void testParseStrictReaderWithMalformedJsonThrowsRuntimeException() throws Throwable {
        StringReader reader = new StringReader("MUB_qz@d");
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setStrictness(Strictness.STRICT);
        try {
            JsonParser.parseReader(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.internal.Streams", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonArrayUsingDeprecatedMethod() throws Throwable {
        StringReader reader = new StringReader("[GoyG.G]");
        JsonParser.parseReader(reader); // Consumes the reader
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(reader);
        assertFalse(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testParseEmptyReaderAsJsonArrayUsingDeprecatedMethod() throws Throwable {
        StringReader reader = new StringReader("");
        JsonParser jsonParser = new JsonParser();
        JsonReader jsonReader = new JsonReader(reader);
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        assertFalse(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testParseReaderAsJsonArrayUsingDeprecatedMethod() throws Throwable {
        StringReader reader = new StringReader("[GoyG.G]");
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(reader);
        assertFalse(jsonArray.isEmpty());
    }

    @Test(timeout = 4000)
    public void testParseStringAsJsonArrayUsingDeprecatedMethod() throws Throwable {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse("[GoyG.G]");
        assertFalse(jsonElement.isJsonObject());
    }
}