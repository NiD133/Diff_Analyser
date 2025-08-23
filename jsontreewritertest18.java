package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeWriterTestTest18 {

    @Test
    public void write_allowsSpecialFloatingPointValues_whenLenient() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);

        JsonArray expectedJson = new JsonArray();
        expectedJson.add(new JsonPrimitive(Float.NaN));
        expectedJson.add(new JsonPrimitive(Float.NEGATIVE_INFINITY));
        expectedJson.add(new JsonPrimitive(Float.POSITIVE_INFINITY));
        expectedJson.add(new JsonPrimitive(Double.NaN));
        expectedJson.add(new JsonPrimitive(Double.NEGATIVE_INFINITY));
        expectedJson.add(new JsonPrimitive(Double.POSITIVE_INFINITY));

        // Act
        writer.beginArray();
        writer.value(Float.NaN);
        writer.value(Float.NEGATIVE_INFINITY);
        writer.value(Float.POSITIVE_INFINITY);
        writer.value(Double.NaN);
        writer.value(Double.NEGATIVE_INFINITY);
        writer.value(Double.POSITIVE_INFINITY);
        writer.endArray();

        JsonElement actualJson = writer.get();

        // Assert
        assertThat(actualJson).isEqualTo(expectedJson);
    }
}