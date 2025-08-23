package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link JsonTreeWriter}.
 * Note: The original class name 'JsonTreeWriter_ESTestTest63' suggests it was
 * auto-generated. A descriptive name is used here for better clarity.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the writer's isHtmlSafe property remains false (its default state)
     * after writing a null value to an object. This test also confirms that the
     * null value is correctly added to the JSON structure.
     */
    @Test
    public void isHtmlSafe_shouldReturnFalseByDefault_afterWritingNullValue() throws IOException {
        // Arrange: Create a JsonTreeWriter to build a JSON structure in memory.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Write a JSON object with a single property whose value is null.
        writer.beginObject();
        writer.name("property");
        writer.nullValue();
        writer.endObject();

        // Assert
        // 1. Verify the primary outcome: The JSON object was built correctly.
        JsonObject expectedJson = new JsonObject();
        expectedJson.add("property", JsonNull.INSTANCE);
        JsonElement actualJson = writer.get();
        assertEquals("The writer should produce a correct JSON object.", expectedJson, actualJson);

        // 2. Verify the behavior from the original test: The isHtmlSafe flag is unaffected
        //    and remains false by default.
        assertFalse("The isHtmlSafe property should be false by default.", writer.isHtmlSafe());
    }
}