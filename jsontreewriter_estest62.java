package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that when `setSerializeNulls(false)` is configured, calling `nullValue()`
     * for an object property does not add the property to the resulting JsonObject.
     */
    @Test
    public void nullValue_whenSerializeNullsIsFalse_skipsAddingPropertyToObject() throws Exception {
        // Arrange: Create a writer and configure it to not serialize nulls.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(false);

        writer.beginObject();
        writer.name("propertyToSkip");

        // Act: Attempt to write a null value for the property.
        writer.nullValue();
        writer.endObject();

        // Assert: The final JSON object should be empty because the null property was skipped.
        JsonElement result = writer.get();
        assertTrue("The result should be a JsonObject", result.isJsonObject());

        JsonObject expected = new JsonObject(); // An empty object is expected.
        assertEquals(expected, result);
    }
}