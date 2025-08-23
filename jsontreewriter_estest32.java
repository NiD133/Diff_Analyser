package com.google.gson.internal.bind;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the `serializeNulls` setting is preserved after
     * writing a complete JSON object.
     */
    @Test
    public void serializeNullsSettingShouldBePreservedAfterWritingObject() throws IOException {
        // Arrange: Create a writer and configure it to not serialize nulls.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(false);

        // Act: Write an empty JSON object. The fluent API should return the same writer instance.
        writer.beginObject().endObject();

        // Assert: The original setting should remain unchanged.
        assertFalse("The serializeNulls setting should be preserved", writer.getSerializeNulls());
    }
}