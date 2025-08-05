package com.google.gson.internal.bind;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;

/**
 * Test suite for JsonTreeReader functionality.
 * Tests reading JSON elements as if they were coming from a character stream.
 */
public class JsonTreeReaderTest {

    private JsonObject testObject;
    private JsonArray testArray;
    private JsonTreeReader reader;

    @Before
    public void setUp() {
        testObject = new JsonObject();
        testArray = new JsonArray();
    }

    // ========== Basic Navigation Tests ==========

    @Test
    public void testReadEmptyObject() throws IOException {
        reader = new JsonTreeReader(new JsonObject());
        
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        assertFalse(reader.hasNext());
        reader.endObject();
    }

    @Test
    public void testReadEmptyArray() throws IOException {
        reader = new JsonTreeReader(new JsonArray());
        
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        assertFalse(reader.hasNext());
        reader.endArray();
    }

    @Test
    public void testReadObjectWithProperty() throws IOException {
        testObject.addProperty("name", "value");
        reader = new JsonTreeReader(testObject);
        
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("name", reader.nextName());
        assertEquals("value", reader.nextString());
        reader.endObject();
    }

    // ========== Primitive Value Tests ==========

    @Test
    public void testReadStringValue() throws IOException {
        JsonPrimitive stringPrimitive = new JsonPrimitive("test string");
        reader = new JsonTreeReader(stringPrimitive);
        
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("test string", reader.nextString());
    }

    @Test
    public void testReadEmptyString() throws IOException {
        JsonPrimitive emptyString = new JsonPrimitive("");
        reader = new JsonTreeReader(emptyString);
        
        assertEquals("", reader.nextString());
    }

    @Test
    public void testReadBooleanTrue() throws IOException {
        JsonPrimitive booleanTrue = new JsonPrimitive(true);
        reader = new JsonTreeReader(booleanTrue);
        
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertTrue(reader.nextBoolean());
    }

    @Test
    public void testReadBooleanFalse() throws IOException {
        JsonPrimitive booleanFalse = new JsonPrimitive(false);
        reader = new JsonTreeReader(booleanFalse);
        
        assertFalse(reader.nextBoolean());
    }

    @Test
    public void testReadNullValue() throws IOException {
        reader = new JsonTreeReader(JsonNull.INSTANCE);
        
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
    }

    // ========== Number Tests ==========

    @Test
    public void testReadPositiveInteger() throws IOException {
        JsonPrimitive number = new JsonPrimitive(42);
        reader = new JsonTreeReader(number);
        
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(42, reader.nextInt());
    }

    @Test
    public void testReadNegativeInteger() throws IOException {
        JsonPrimitive number = new JsonPrimitive(-1977);
        reader = new JsonTreeReader(number);
        
        assertEquals(-1977, reader.nextInt());
    }

    @Test
    public void testReadZeroInteger() throws IOException {
        JsonPrimitive zero = new JsonPrimitive(0);
        reader = new JsonTreeReader(zero);
        
        assertEquals(0, reader.nextInt());
    }

    @Test
    public void testReadLongValue() throws IOException {
        JsonPrimitive longValue = new JsonPrimitive(123456789L);
        reader = new JsonTreeReader(longValue);
        
        assertEquals(123456789L, reader.nextLong());
    }

    @Test
    public void testReadDoubleValue() throws IOException {
        JsonPrimitive doubleValue = new JsonPrimitive(3.14159);
        reader = new JsonTreeReader(doubleValue);
        
        assertEquals(3.14159, reader.nextDouble(), 0.00001);
    }

    @Test
    public void testReadZeroDouble() throws IOException {
        JsonPrimitive zero = new JsonPrimitive(0.0);
        reader = new JsonTreeReader(zero);
        
        assertEquals(0.0, reader.nextDouble(), 0.01);
    }

    // ========== Array Tests ==========

    @Test
    public void testReadArrayWithElements() throws IOException {
        testArray.add("first");
        testArray.add(42);
        testArray.add(true);
        reader = new JsonTreeReader(testArray);
        
        reader.beginArray();
        assertEquals("first", reader.nextString());
        assertEquals(42, reader.nextInt());
        assertTrue(reader.nextBoolean());
        reader.endArray();
    }

    @Test
    public void testArrayHasNext() throws IOException {
        testArray.add("element");
        reader = new JsonTreeReader(testArray);
        
        assertTrue(reader.hasNext()); // Array itself
        reader.beginArray();
        assertTrue(reader.hasNext()); // Element inside
        reader.nextString();
        assertFalse(reader.hasNext()); // No more elements
        reader.endArray();
    }

    // ========== Object Tests ==========

    @Test
    public void testReadObjectWithMultipleProperties() throws IOException {
        testObject.addProperty("string", "value");
        testObject.addProperty("number", 123);
        testObject.addProperty("boolean", true);
        reader = new JsonTreeReader(testObject);
        
        reader.beginObject();
        
        // Note: Order might vary, so we check all properties exist
        for (int i = 0; i < 3; i++) {
            assertTrue(reader.hasNext());
            String name = reader.nextName();
            assertTrue(name.equals("string") || name.equals("number") || name.equals("boolean"));
            reader.skipValue(); // Skip the value for this test
        }
        
        assertFalse(reader.hasNext());
        reader.endObject();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        testObject.addProperty("123", "ignored");
        reader = new JsonTreeReader(testObject);
        
        reader.beginObject();
        reader.promoteNameToValue();
        assertEquals(123, reader.nextInt()); // Name "123" promoted to integer value
    }

    // ========== JsonElement Tests ==========

    @Test
    public void testNextJsonElementArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        
        JsonElement element = reader.nextJsonElement();
        assertTrue(element.isJsonArray());
    }

    @Test
    public void testNextJsonElementObject() throws IOException {
        reader = new JsonTreeReader(testObject);
        
        JsonElement element = reader.nextJsonElement();
        assertTrue(element.isJsonObject());
    }

    // ========== Skip Value Tests ==========

    @Test
    public void testSkipArrayValue() throws IOException {
        reader = new JsonTreeReader(testArray);
        
        reader.skipValue();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipObjectValue() throws IOException {
        reader = new JsonTreeReader(testObject);
        
        reader.skipValue();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipElementInArray() throws IOException {
        testArray.add("skip me");
        testArray.add("read me");
        reader = new JsonTreeReader(testArray);
        
        reader.beginArray();
        reader.skipValue(); // Skip first element
        assertEquals("read me", reader.nextString());
        reader.endArray();
    }

    // ========== Path Tests ==========

    @Test
    public void testGetPathRoot() {
        reader = new JsonTreeReader(testObject);
        assertEquals("$", reader.getPath());
    }

    @Test
    public void testGetPathInArray() throws IOException {
        testArray.add("element");
        reader = new JsonTreeReader(testArray);
        
        reader.beginArray();
        assertEquals("$[0]", reader.getPreviousPath());
    }

    @Test
    public void testGetPathAfterReadingArrayElement() throws IOException {
        testArray.add("element");
        reader = new JsonTreeReader(testArray);
        
        reader.beginArray();
        reader.nextJsonElement();
        assertEquals("$[0]", reader.getPreviousPath());
    }

    // ========== Error Condition Tests ==========

    @Test(expected = IllegalStateException.class)
    public void testReadBooleanFromArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.nextBoolean(); // Should fail - array is not boolean
    }

    @Test(expected = IllegalStateException.class)
    public void testReadStringFromArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.nextString(); // Should fail - array is not string
    }

    @Test(expected = IllegalStateException.class)
    public void testReadIntFromArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.nextInt(); // Should fail - array is not number
    }

    @Test(expected = IllegalStateException.class)
    public void testReadNameFromArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.beginArray();
        reader.nextName(); // Should fail - no names in arrays
    }

    @Test(expected = IllegalStateException.class)
    public void testPromoteNameInArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.promoteNameToValue(); // Should fail - arrays don't have names
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayOnObject() throws IOException {
        reader = new JsonTreeReader(testObject);
        reader.endArray(); // Should fail - not in array
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectOnArray() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.endObject(); // Should fail - not in object
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayOnNull() throws IOException {
        reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.beginArray(); // Should fail - null is not array
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectOnNull() throws IOException {
        reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.beginObject(); // Should fail - null is not object
    }

    @Test(expected = NumberFormatException.class)
    public void testReadInvalidInteger() throws IOException {
        JsonPrimitive invalidNumber = new JsonPrimitive("not a number");
        reader = new JsonTreeReader(invalidNumber);
        reader.nextInt(); // Should fail - invalid number format
    }

    @Test(expected = NumberFormatException.class)
    public void testReadInvalidLong() throws IOException {
        JsonPrimitive invalidNumber = new JsonPrimitive("not a number");
        reader = new JsonTreeReader(invalidNumber);
        reader.nextLong(); // Should fail - invalid number format
    }

    @Test(expected = NumberFormatException.class)
    public void testReadInvalidDouble() throws IOException {
        JsonPrimitive invalidNumber = new JsonPrimitive("not a number");
        reader = new JsonTreeReader(invalidNumber);
        reader.nextDouble(); // Should fail - invalid number format
    }

    @Test
    public void testReadInfinityThrowsException() throws IOException {
        JsonPrimitive infinity = new JsonPrimitive("-Infinity");
        reader = new JsonTreeReader(infinity);
        
        try {
            reader.nextDouble();
            fail("Expected IOException for infinity value");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("JSON forbids NaN and infinities"));
        }
    }

    // ========== Closed Reader Tests ==========

    @Test(expected = IllegalStateException.class)
    public void testOperationOnClosedReader() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.close();
        reader.peek(); // Should fail - reader is closed
    }

    @Test(expected = IllegalStateException.class)
    public void testHasNextOnClosedReader() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.close();
        reader.hasNext(); // Should fail - reader is closed
    }

    @Test(expected = IllegalStateException.class)
    public void testSkipValueOnClosedReader() throws IOException {
        reader = new JsonTreeReader(testArray);
        reader.close();
        reader.skipValue(); // Should fail - reader is closed
    }

    // ========== Null Element Tests ==========

    @Test(expected = NullPointerException.class)
    public void testNullElementThrowsNPE() throws IOException {
        reader = new JsonTreeReader(null);
        reader.peek(); // Should fail - null element
    }

    // ========== Utility Tests ==========

    @Test
    public void testToString() {
        reader = new JsonTreeReader(JsonNull.INSTANCE);
        assertEquals("JsonTreeReader at path $", reader.toString());
    }

    @Test
    public void testDefaultNestingLimit() {
        reader = new JsonTreeReader(testObject);
        assertEquals(255, reader.getNestingLimit());
    }

    @Test
    public void testDefaultStrictness() {
        reader = new JsonTreeReader(testObject);
        assertEquals(Strictness.LEGACY_STRICT, reader.getStrictness());
    }

    @Test
    public void testIsNotLenientByDefault() {
        reader = new JsonTreeReader(testObject);
        assertFalse(reader.isLenient());
    }

    @Test
    public void testLenientDoubleReading() throws IOException {
        JsonPrimitive numberString = new JsonPrimitive("5");
        reader = new JsonTreeReader(numberString);
        reader.setStrictness(Strictness.LENIENT);
        
        assertEquals(5.0, reader.nextDouble(), 0.01);
    }

    // ========== Complex Structure Tests ==========

    @Test
    public void testNestedArrayAndObject() throws IOException {
        JsonObject nested = new JsonObject();
        nested.addProperty("inner", "value");
        testArray.add(nested);
        reader = new JsonTreeReader(testArray);
        
        reader.beginArray();
        reader.beginObject();
        assertEquals("inner", reader.nextName());
        assertEquals("value", reader.nextString());
        reader.endObject();
        reader.endArray();
    }

    @Test
    public void testReadAfterEndDocument() throws IOException {
        reader = new JsonTreeReader(JsonNull.INSTANCE);
        reader.nextNull(); // Consume the null
        
        try {
            reader.nextJsonElement();
            fail("Expected IllegalStateException for END_DOCUMENT");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Unexpected END_DOCUMENT"));
        }
    }
}