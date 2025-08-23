package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void writeNullProperty_whenSerializeNullsIsEnabled_succeeds() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(true);

        // Act
        writer.beginObject();
        writer.name("A");
        writer.nullValue();
        writer.endObject();

        // Assert
        JsonObject expectedJson = new JsonObject();
        expectedJson.add("A", JsonNull.INSTANCE);
        
        JsonElement actualJson = writer.get();
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}