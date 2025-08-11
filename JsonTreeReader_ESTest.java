package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.util.ConcurrentModificationException;

public class JsonTreeReaderTest {

    private static final long TEST_LONG_ZERO = 0L;
    private static final long TEST_LONG_NEGATIVE = -1977L;
    private static final String TEST_STRING_EMPTY = "";
    private static final String TEST_STRING_NON_NUMERIC = "||9{dvk.\"Ana";
    private static final String TEST_STRING_NUMERIC = "5";

    @Test
    public void testBeginObjectWithEmptyName() throws Throwable {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TEST_STRING_EMPTY, TEST_STRING_EMPTY);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.beginObject();
        String name = jsonTreeReader.nextName();
        assertEquals(TEST_STRING_EMPTY, name);
    }

    @Test
    public void testNextLongWithZero() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_LONG_ZERO);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        long value = jsonTreeReader.nextLong();
        assertEquals(TEST_LONG_ZERO, value);
    }

    @Test
    public void testNextJsonElementWithEmptyArray() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        JsonElement jsonElement = jsonTreeReader.nextJsonElement();
        assertFalse(jsonElement.isJsonObject());
    }

    @Test
    public void testNextIntWithZero() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_LONG_ZERO);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        int value = jsonTreeReader.nextInt();
        assertEquals(0, value);
    }

    @Test
    public void testNextIntWithNegativeValue() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_LONG_NEGATIVE);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        int value = jsonTreeReader.nextInt();
        assertEquals((int) TEST_LONG_NEGATIVE, value);
    }

    @Test
    public void testNextDoubleWithZero() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_LONG_ZERO);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        double value = jsonTreeReader.nextDouble();
        assertEquals(0.0, value, 0.01);
    }

    @Test
    public void testNextDoubleWithNegativeValue() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_LONG_NEGATIVE);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        double value = jsonTreeReader.nextDouble();
        assertEquals((double) TEST_LONG_NEGATIVE, value, 0.01);
    }

    @Test
    public void testSkipValueWithConcurrentModificationException() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.beginArray();
        jsonArray.add((JsonElement) null);

        try {
            jsonTreeReader.skipValue();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSkipValueWithNullPointerException() {
        JsonTreeReader jsonTreeReader = new JsonTreeReader((JsonElement) null);

        try {
            jsonTreeReader.skipValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testCloseWithIllegalStateException() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.close();

        try {
            jsonTreeReader.skipValue();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testPromoteNameToValueWithConcurrentModificationException() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.beginArray();
        jsonArray.add("$[0]");

        try {
            jsonTreeReader.promoteNameToValue();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextLongWithNumberFormatException() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_STRING_NON_NUMERIC);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        try {
            jsonTreeReader.nextLong();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextIntWithNumberFormatException() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_STRING_NON_NUMERIC);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        try {
            jsonTreeReader.nextInt();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextDoubleWithNumberFormatException() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_STRING_NON_NUMERIC);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        try {
            jsonTreeReader.nextDouble();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextBooleanWithIllegalStateException() {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

        try {
            jsonTreeReader.nextBoolean();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextStringWithIllegalStateException() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

        try {
            jsonTreeReader.nextString();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextNameWithIllegalStateException() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.beginArray();

        try {
            jsonTreeReader.nextName();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testPeekWithNullPointerException() {
        JsonTreeReader jsonTreeReader = new JsonTreeReader((JsonElement) null);

        try {
            jsonTreeReader.peek();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testPeekWithJsonToken() throws Throwable {
        JsonNull jsonNull = JsonNull.INSTANCE;
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonNull);
        JsonToken jsonToken = jsonTreeReader.peek();
        assertEquals(JsonToken.NULL, jsonToken);
    }

    @Test
    public void testNextJsonElementWithIllegalStateException() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("h", "h");
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.beginObject();

        try {
            jsonTreeReader.nextJsonElement();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testNextIntWithPromotedName() throws Throwable {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TEST_STRING_NUMERIC, TEST_STRING_NUMERIC);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.beginObject();
        jsonTreeReader.promoteNameToValue();
        int value = jsonTreeReader.nextInt();
        assertEquals(5, value);
    }

    @Test
    public void testNextDoubleWithPromotedName() throws Throwable {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TEST_STRING_NUMERIC, TEST_STRING_NUMERIC);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
        jsonTreeReader.beginObject();
        jsonTreeReader.promoteNameToValue();
        double value = jsonTreeReader.nextDouble();
        assertEquals(5.0, value, 0.01);
    }

    @Test
    public void testNextBoolean() throws Throwable {
        Boolean booleanValue = Boolean.TRUE;
        JsonPrimitive jsonPrimitive = new JsonPrimitive(booleanValue);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        boolean value = jsonTreeReader.nextBoolean();
        assertTrue(value);
    }

    @Test
    public void testNextString() throws Throwable {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(TEST_STRING_EMPTY);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
        String value = jsonTreeReader.nextString();
        assertEquals(TEST_STRING_EMPTY, value);
    }

    @Test
    public void testHasNext() throws Throwable {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
        jsonTreeReader.beginArray();
        boolean hasNext = jsonTreeReader.hasNext();
        assertFalse(hasNext);
    }

    @Test
    public void testToString() throws Throwable {
        JsonNull jsonNull = JsonNull.INSTANCE;
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonNull);
        String description = jsonTreeReader.toString();
        assertEquals("JsonTreeReader at path $", description);
    }
}