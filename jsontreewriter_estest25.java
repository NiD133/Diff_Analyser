package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeWriter}.
 * Note: The original test class name 'JsonTreeWriter_ESTestTest25' and its
 * scaffolding base class were simplified for clarity.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling {@link JsonTreeWriter#get()} on a new writer,
     * before any JSON content has been written, returns a {@link JsonNull} element.
     */
    @Test
    public void getOnNewWriterShouldReturnJsonNull() {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Retrieve the top-level JSON element without writing any data.
        JsonElement result = writer.get();

        // Assert: The retrieved element should be a JsonNull.
        // The initial state of the writer's product is JsonNull.INSTANCE.
        assertTrue("A new JsonTreeWriter should produce JsonNull by default", result.isJsonNull());

        // A more specific assertion can also be used to check for the singleton instance:
        // assertEquals(JsonNull.INSTANCE, result);
    }
}