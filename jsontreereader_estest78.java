package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeReader}.
 * Note: The original class name `JsonTreeReader_ESTestTest78` was likely auto-generated.
 * A more conventional name would be `JsonTreeReaderTest`.
 */
public class JsonTreeReader_ESTestTest78 {

    @Test
    public void nextDouble_whenLenient_parsesNumericString() throws IOException {
        // Arrange: Create a reader for a JSON string primitive in lenient mode.
        JsonPrimitive numericString = new JsonPrimitive("5");
        JsonTreeReader reader = new JsonTreeReader(numericString);
        reader.setStrictness(Strictness.LENIENT);

        // Act: Read the value as a double.
        double result = reader.nextDouble();

        // Assert: Verify that the string was correctly parsed into a double.
        assertEquals(5.0, result, 0.01);
    }
}