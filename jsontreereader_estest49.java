package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void peek_onJsonObject_returnsBeginObject() throws Exception {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);

        // Act
        JsonToken actualToken = jsonTreeReader.peek();

        // Assert
        assertEquals(JsonToken.BEGIN_OBJECT, actualToken);
    }
}