import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JsonTreeReader}.
 * This class focuses on verifying the reader's behavior when navigating and parsing a JsonElement tree.
 */
@DisplayName("JsonTreeReader")
class JsonTreeReaderTest {

    @Nested
    @DisplayName("Navigation and State")
    class NavigationAndStateTests {

        @Test
        @DisplayName("should correctly navigate an empty object")
        void navigateEmptyObject() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonObject());
            reader.beginObject();
            assertFalse(reader.hasNext());
            reader.endObject();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        }

        @Test
        @DisplayName("should correctly navigate an empty array")
        void navigateEmptyArray() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonArray());
            reader.beginArray();
            assertFalse(reader.hasNext());
            reader.endArray();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        }

        @Test
        @DisplayName("hasNext() should return false after consuming the last element")
        void hasNext_afterConsumingLastElement_returnsFalse() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
            reader.nextNull();
            assertFalse(reader.hasNext());
        }

        @Test
        @DisplayName("skipValue() should consume an entire nested structure")
        void skipValue_onNestedStructure_consumesStructure() throws IOException {
            // Arrange
            JsonObject nestedObject = new JsonObject();
            nestedObject.addProperty("b", true);
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(new JsonPrimitive("a"));
            jsonArray.add(nestedObject);

            JsonObject rootObject = new JsonObject();
            rootObject.add("array", jsonArray);
            rootObject.addProperty("final", "done");

            JsonTreeReader reader = new JsonTreeReader(rootObject);

            // Act
            reader.beginObject();
            assertEquals("array", reader.nextName());
            reader.skipValue(); // Skip the entire array

            // Assert
            assertTrue(reader.hasNext());
            assertEquals("final", reader.nextName());
            assertEquals("done", reader.nextString());
            reader.endObject();
        }

        @Test
        @DisplayName("close() should put the reader in a closed state")
        void close_marksReaderAsClosed() throws IOException {
            // Arrange
            JsonTreeReader reader = new JsonTreeReader(new JsonArray());
            
            // Act
            reader.close();

            // Assert
            IllegalStateException exception = assertThrows(IllegalStateException.class, reader::peek);
            assertEquals("JsonReader is closed", exception.getMessage());
            
            // Verify other methods also throw
            assertThrows(IllegalStateException.class, reader::hasNext);
            assertThrows(IllegalStateException.class, reader::skipValue);
        }
    }

    @Nested
    @DisplayName("Peeking")
    class PeekTests {

        @Test
        @DisplayName("on different JSON types should return the correct token")
        void peek_onVariousTypes_returnsCorrectToken() throws IOException {
            assertEquals(JsonToken.BEGIN_OBJECT, new JsonTreeReader(new JsonObject()).peek());
            assertEquals(JsonToken.BEGIN_ARRAY, new JsonTreeReader(new JsonArray()).peek());
            assertEquals(JsonToken.NUMBER, new JsonTreeReader(new JsonPrimitive(123)).peek());
            assertEquals(JsonToken.STRING, new JsonTreeReader(new JsonPrimitive("hello")).peek());
            assertEquals(JsonToken.BOOLEAN, new JsonTreeReader(new JsonPrimitive(true)).peek());
            assertEquals(JsonToken.NULL, new JsonTreeReader(JsonNull.INSTANCE).peek());
        }

        @Test
        @DisplayName("inside an object should return NAME then the value's token")
        void peek_insideObject_returnsNameThenValueToken() throws IOException {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("key", "value");
            JsonTreeReader reader = new JsonTreeReader(jsonObject);

            reader.beginObject();
            assertEquals(JsonToken.NAME, reader.peek());
            reader.nextName();
            assertEquals(JsonToken.STRING, reader.peek());
            reader.nextString();
            assertEquals(JsonToken.END_OBJECT, reader.peek());
        }

        @Test
        @DisplayName("inside an array should return the element's token")
        void peek_insideArray_returnsElementToken() throws IOException {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(new JsonPrimitive(1));
            JsonTreeReader reader = new JsonTreeReader(jsonArray);

            reader.beginArray();
            assertEquals(JsonToken.NUMBER, reader.peek());
            reader.nextInt();
            assertEquals(JsonToken.END_ARRAY, reader.peek());
        }

        @Test
        @DisplayName("after consuming all tokens should return END_DOCUMENT")
        void peek_atEnd_returnsEndDocument() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(1));
            reader.skipValue();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        }
    }

    @Nested
    @DisplayName("Value Reading")
    class ValueReadingTests {

        @Test
        @DisplayName("nextString() should return the string value")
        void nextString_returnsValue() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("hello"));
            assertEquals("hello", reader.nextString());
        }

        @Test
        @DisplayName("nextString() on a non-string primitive should return its string representation")
        void nextString_onNumber_returnsStringRepresentation() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123.45));
            assertEquals("123.45", reader.nextString());
        }

        @Test
        @DisplayName("nextBoolean() should return the boolean value")
        void nextBoolean_returnsValue() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
            assertTrue(reader.nextBoolean());
        }

        @Test
        @DisplayName("nextNull() should consume a null value")
        void nextNull_consumesNull() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
            reader.nextNull();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        }

        @Test
        @DisplayName("nextDouble() should return the double value")
        void nextDouble_returnsValue() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(-19.77));
            assertEquals(-19.77, reader.nextDouble(), 0.0);
        }

        @Test
        @DisplayName("nextLong() should return the long value")
        void nextLong_returnsValue() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(-1977L));
            assertEquals(-1977L, reader.nextLong());
        }

        @Test
        @DisplayName("nextInt() should return the integer value")
        void nextInt_returnsValue() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123));
            assertEquals(123, reader.nextInt());
        }

        @Test
        @DisplayName("nextName() should return the property name of an object")
        void nextName_onObject_returnsPropertyName() throws IOException {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("testName", "testValue");
            JsonTreeReader reader = new JsonTreeReader(jsonObject);
            reader.beginObject();
            assertEquals("testName", reader.nextName());
        }
    }

    @Nested
    @DisplayName("Advanced Reading")
    class AdvancedReadingTests {

        @Test
        @DisplayName("nextJsonElement() should return the current element and advance the reader")
        void nextJsonElement_returnsElementAndAdvances() throws IOException {
            // Arrange
            JsonPrimitive primitive = new JsonPrimitive("test");
            JsonArray array = new JsonArray();
            array.add(primitive);
            JsonTreeReader reader = new JsonTreeReader(array);

            // Act
            reader.beginArray();
            JsonElement result = reader.nextJsonElement();
            
            // Assert
            assertSame(primitive, result);
            assertEquals(JsonToken.END_ARRAY, reader.peek());
        }

        @Test
        @DisplayName("promoteNameToValue() should allow reading a property name as a value")
        void promoteNameToValue_allowsReadingNameAsValue() throws IOException {
            // Arrange
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nameAsValue", "actualValue");
            JsonTreeReader reader = new JsonTreeReader(jsonObject);

            reader.beginObject();
            assertEquals(JsonToken.NAME, reader.peek());

            // Act
            reader.promoteNameToValue();

            // Assert
            // The name "nameAsValue" is now treated as a string value.
            assertEquals(JsonToken.STRING, reader.peek());
            assertEquals("nameAsValue", reader.nextString());

            // The original value "actualValue" follows.
            assertEquals(JsonToken.STRING, reader.peek());
            assertEquals("actualValue", reader.nextString());
        }
    }

    @Nested
    @DisplayName("Path Information")
    class PathTests {

        @Test
        @DisplayName("getPath() should return the correct JSONPath representation")
        void getPath_returnsCorrectPath() throws IOException {
            // Arrange
            JsonObject inner = new JsonObject();
            inner.addProperty("c", 3);
            JsonArray array = new JsonArray();
            array.add(1);
            array.add(inner);
            JsonObject root = new JsonObject();
            root.add("a", array);

            JsonTreeReader reader = new JsonTreeReader(root);

            // Assert paths at different locations
            assertEquals("$", reader.getPath());
            reader.beginObject();
            assertEquals("$.", reader.getPath());
            reader.nextName(); // "a"
            assertEquals("$.a", reader.getPath());
            reader.beginArray();
            assertEquals("$.a[0]", reader.getPath());
            reader.nextInt(); // 1
            assertEquals("$.a[1]", reader.getPath());
            reader.beginObject();
            assertEquals("$.a[1].", reader.getPath());
        }

        @Test
        @DisplayName("getPreviousPath() should return the path of the most recently consumed token")
        void getPreviousPath_returnsPathOfConsumedToken() throws IOException {
            // Arrange
            JsonArray array = new JsonArray();
            array.add(new JsonPrimitive("A"));
            array.add(new JsonPrimitive("B"));
            JsonTreeReader reader = new JsonTreeReader(array);

            // Assert
            assertNull(reader.getPreviousPath());
            reader.beginArray();
            // The path of beginArray is the array itself
            assertEquals("$", reader.getPreviousPath());
            reader.nextString(); // Consumes "A"
            assertEquals("$[0]", reader.getPreviousPath());
            reader.nextString(); // Consumes "B"
            assertEquals("$[1]", reader.getPreviousPath());
        }

        @Test
        @DisplayName("toString() should return a string with the current path")
        void toString_returnsReaderAndPath() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonObject());
            assertEquals("JsonTreeReader at path $", reader.toString());
            reader.beginObject();
            assertEquals("JsonTreeReader at path $.", reader.toString());
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("creating a reader with null should throw NullPointerException on operations")
        void create_withNullElement_throwsNullPointerException() {
            JsonTreeReader reader = new JsonTreeReader(null);
            assertThrows(NullPointerException.class, reader::peek);
            assertThrows(NullPointerException.class, reader::beginArray);
            assertThrows(NullPointerException.class, reader::hasNext);
            assertThrows(NullPointerException.class, reader::skipValue);
        }

        @Test
        @DisplayName("calling a method for the wrong token type should throw IllegalStateException")
        void method_forWrongToken_throwsIllegalStateException() {
            assertThrows(IllegalStateException.class, () -> new JsonTreeReader(new JsonObject()).nextString());
            assertThrows(IllegalStateException.class, () -> new JsonTreeReader(new JsonArray()).endObject());
            assertThrows(IllegalStateException.class, () -> new JsonTreeReader(JsonNull.INSTANCE).beginArray());
        }

        @Test
        @DisplayName("modifying a JsonArray while iterating should throw ConcurrentModificationException")
        void iterating_onModifiedArray_throwsCME() throws IOException {
            JsonArray jsonArray = new JsonArray();
            JsonTreeReader reader = new JsonTreeReader(jsonArray);
            reader.beginArray();

            // Modify the array after iteration has started
            jsonArray.add("new element");

            assertThrows(ConcurrentModificationException.class, reader::hasNext);
        }

        @Test
        @DisplayName("modifying a JsonObject while iterating should throw ConcurrentModificationException")
        void iterating_onModifiedObject_throwsCME() throws IOException {
            JsonObject jsonObject = new JsonObject();
            JsonTreeReader reader = new JsonTreeReader(jsonObject);
            reader.beginObject();

            // Modify the object after iteration has started
            jsonObject.addProperty("key", "value");

            assertThrows(ConcurrentModificationException.class, reader::hasNext);
        }

        @Test
        @DisplayName("reading a non-numeric string as a number should throw NumberFormatException")
        void read_nonNumericStringAsNumber_throwsNumberFormatException() {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("not a number"));
            assertThrows(NumberFormatException.class, reader::nextInt);
        }

        @Test
        @DisplayName("reading non-finite numbers in strict mode should throw IOException")
        void read_nonFiniteNumberInStrictMode_throwsIOException() {
            // Default mode is strict
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("NaN"));
            IOException exception = assertThrows(IOException.class, reader::nextDouble);
            assertTrue(exception.getMessage().contains("JSON forbids NaN and infinities"));
        }

        @Test
        @DisplayName("reading non-finite numbers in lenient mode should succeed")
        void read_nonFiniteNumberInLenientMode_succeeds() throws IOException {
            JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("Infinity"));
            reader.setLenient(true);
            assertEquals(Double.POSITIVE_INFINITY, reader.nextDouble());
        }
    }
}