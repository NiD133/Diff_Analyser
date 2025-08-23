package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 * This class focuses on verifying the state and behavior of the writer.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the writer is not lenient by default after beginning an object,
     * even when other settings like htmlSafe have been configured.
     */
    @Test
    public void beginObjectShouldNotChangeDefaultLeniency() throws Exception {
        // Arrange: Create a writer and configure a non-default setting.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setHtmlSafe(true);

        // Act: Start writing a JSON object.
        writer.beginObject();

        // Assert: The writer should retain its default non-lenient state.
        assertFalse("A new writer should not be lenient by default", writer.isLenient());
    }
}