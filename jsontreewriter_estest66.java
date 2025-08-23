package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void get_afterWritingEmptyArray_returnsJsonArray() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        writer.beginArray();
        writer.endArray();
        JsonElement result = writer.get();

        // Assert
        JsonArray expected = new JsonArray();
        assertEquals(expected, result);
    }
}