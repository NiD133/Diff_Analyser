package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void writeArray_withNumericValues_buildsCorrectJsonArray() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonArray expectedJson = new JsonArray();
        expectedJson.add(1);
        expectedJson.add(2);
        expectedJson.add(3);

        // Act
        writer.beginArray();
        writer.value(1);
        writer.value(2);
        writer.value(3);
        writer.endArray();
        JsonElement actualJson = writer.get();

        // Assert
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}