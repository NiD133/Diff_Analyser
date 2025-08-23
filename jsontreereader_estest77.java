package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the JsonTreeReader.
 * This specific test focuses on the promoteNameToValue() method.
 */
public class JsonTreeReaderTest {

    @Test
    public void promoteNameToValue_whenNameIsNumericString_canBeReadAsDouble() throws IOException {
        // Arrange
        // Create a JSON object {"5": "value"}. We want to test if the name "5" can be
        // promoted to a value and then parsed as a number. The actual property value is irrelevant.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("5", "this value is ignored");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject(); // Position the reader after the opening '{'

        // Act
        // Instead of reading the name "5" with nextName(), we promote it to a value.
        reader.promoteNameToValue();
        // Now, we attempt to read that promoted value as a double.
        double actualDouble = reader.nextDouble();

        // Assert
        double expectedDouble = 5.0;
        assertEquals("The promoted name '5' should be correctly parsed as a double.", expectedDouble, actualDouble, 0.01);
    }
}