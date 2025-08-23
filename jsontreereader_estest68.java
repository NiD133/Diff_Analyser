package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling nextJsonElement() after consuming the beginning of an empty
     * object throws an IllegalStateException. The reader expects to find a name or the
     * end of the object, not another complete JSON element.
     */
    @Test
    public void nextJsonElement_atEndOfEmptyObject_throwsIllegalStateException() throws IOException {
        // Arrange: Create a reader for an empty JSON object and advance it past the opening brace.
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);
        reader.beginObject(); // Reader is now positioned inside the empty object: {|}

        // Act & Assert: Attempting to read the next element should fail with a specific message.
        try {
            reader.nextJsonElement();
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException expected) {
            assertEquals("Unexpected END_OBJECT when reading a JsonElement.", expected.getMessage());
        }
    }
}