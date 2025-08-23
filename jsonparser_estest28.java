package com.google.gson;

import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test suite for the {@link JsonParser} class.
 */
public class JsonParserTest {

    /**
     * Tests that parsing an empty reader results in a JsonNull element.
     * This is for backward compatibility with earlier versions of Gson.
     */
    @Test
    public void parseReader_withEmptyReader_returnsJsonNull() {
        // Arrange
        Reader emptyReader = new StringReader("");

        // Act
        JsonElement result = JsonParser.parseReader(emptyReader);

        // Assert
        assertTrue("Parsing an empty reader should yield JsonNull", result.isJsonNull());
    }
}