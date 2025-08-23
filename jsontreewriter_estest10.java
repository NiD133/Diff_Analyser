package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the writer's strictness setting is preserved after a value is written.
     * This ensures that the state of the parent JsonWriter is not unintentionally modified
     * by the write operations.
     */
    @Test
    public void strictnessSettingIsPreservedAfterWritingValue() throws IOException {
        // Arrange: Create a writer and configure its strictness mode.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);

        // Act: Perform a write operation.
        writer.value(true);

        // Assert: Verify that the strictness mode remains unchanged.
        assertTrue("The writer should remain lenient after a value is written.", writer.isLenient());
    }
}