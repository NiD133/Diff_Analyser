/**
 * Test suite for {@link TypeAdapter}.
 */
public class TypeAdapterTest extends TypeAdapter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNullObjectSerialization() throws Throwable {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();
        Writer writer = Writer.nullWriter();

        // Act
        nullSafeAdapter.toJson(writer, null);
    }

    @Test(timeout = 4000)
    public void testNullObjectToJsonTree() throws Throwable {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();

        // Act and Assert
        JsonElement jsonElement = nullSafeAdapter.toJsonTree(null);
        assertFalse(jsonElement.isJsonArray());
    }

    @Test(timeout = 4000)
    public void testReadNullObject() throws Throwable {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();
        StringReader stringReader = new StringReader("null");
        JsonReader jsonReader = new JsonReader(stringReader);

        // Act and Assert
        Object object = nullSafeAdapter.read(jsonReader);
        assertNull(object);
    }

    @Test(timeout = 4000)
    public void testReadNullObjectFromString() throws Throwable {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();

        // Act and Assert
        Object object = nullSafeAdapter.fromJson("null");
        assertNull(object);
    }

    @Test(timeout = 4000)
    public void testReadNullObjectFromStringReader() throws Throwable {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();
        StringReader stringReader = new StringReader("NULL");

        // Act and Assert
        Object object = nullSafeAdapter.fromJson(stringReader);
        assertNull(object);
    }

    @Test(timeout = 4000)
    public void testWriteNullObjectToNullWriter() {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();

        // Act and Assert
        try {
            adapter.write(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteToNullWriter() {
        // Arrange
        Gson.FutureTypeAdapter<Object> adapter = new Gson.FutureTypeAdapter<>();
        TypeAdapter<Object> nullSafeAdapter = adapter.nullSafe();

        // Act and Assert
        try {
            nullSafeAdapter.write(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Add more test methods for other scenarios...
}