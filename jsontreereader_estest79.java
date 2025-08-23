package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextDouble_onNegativeInfinity_throwsIOException() {
        // Arrange: Create a JsonTreeReader with a JSON primitive representing negative infinity.
        // According to the JSON standard, infinity values are not permitted.
        JsonPrimitive negativeInfinity = new JsonPrimitive("-Infinity");
        JsonTreeReader jsonTreeReader = new JsonTreeReader(negativeInfinity);

        // Act & Assert: Verify that calling nextDouble() throws an IOException with a specific message.
        try {
            jsonTreeReader.nextDouble();
            fail("Expected an IOException because JSON forbids infinity values, but no exception was thrown.");
        } catch (IOException e) {
            String expectedMessage = "JSON forbids NaN and infinities: -Infinity";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}