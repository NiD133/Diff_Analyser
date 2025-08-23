package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonTreeReader} class.
 * This class demonstrates a refactored, more understandable version of an auto-generated test.
 */
public class JsonTreeReaderTest {

    /**
     * Tests that calling {@link JsonTreeReader#nextDouble()} on a reader
     * created from a {@link JsonPrimitive} containing a long value
     * correctly returns the equivalent double value.
     */
    @Test
    public void nextDouble_forLongPrimitive_returnsEquivalentDouble() throws IOException {
        // Arrange: Create a JsonTreeReader with a JsonPrimitive holding the long value 0.
        JsonPrimitive jsonPrimitive = new JsonPrimitive(0L);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        // Act: Read the value as a double.
        double result = jsonTreeReader.nextDouble();

        // Assert: The result should be the double equivalent of the original long.
        assertEquals(0.0, result, 0.0);
    }
}