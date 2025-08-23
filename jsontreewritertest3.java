package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

// The class name was improved from the unusual "JsonTreeWriterTestTest3"
// to the standard convention "JsonTreeWriterTest".
public class JsonTreeWriterTest {

    /**
     * Tests that writing a complete JSON object with properties results in a
     * corresponding JsonObject when get() is called.
     */
    @Test
    public void get_afterWritingObject_returnsCorrectJsonObject() throws IOException {
        // Arrange: Create the writer and the expected JSON structure for comparison.
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject expected = new JsonObject();
        expected.addProperty("A", 1);
        expected.addProperty("B", 2);

        // Act: Use the writer to build the JSON object.
        writer.beginObject();
        writer.name("A").value(1);
        writer.name("B").value(2);
        writer.endObject();

        JsonElement result = writer.get();

        // Assert: Verify that the generated JsonElement is structurally equal to the expected one.
        assertThat(result).isEqualTo(expected);
    }
}