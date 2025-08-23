package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the JsonTreeReader class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that nextName() correctly returns the name of a property
     * after beginning to read a JSON object.
     */
    @Test
    public void nextName_afterBeginObject_returnsCorrectPropertyName() throws IOException {
        // Arrange: Create a JSON object with a single property and a reader for it.
        String expectedPropertyName = "user_name";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(expectedPropertyName, "JohnDoe");

        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        // Act: Start reading the object and get the first property name.
        reader.beginObject();
        String actualPropertyName = reader.nextName();

        // Assert: The returned name should match the one we added.
        assertEquals(expectedPropertyName, actualPropertyName);
    }
}