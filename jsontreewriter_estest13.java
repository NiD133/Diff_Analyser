package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the strictness setting on a JsonTreeWriter is not affected
     * by subsequent write operations.
     */
    @Test
    public void strictnessSettingShouldPersistAfterWritingValue() {
        // Arrange: Create a writer and set its strictness to lenient.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setStrictness(Strictness.LENIENT);

        // Act: Perform a write operation.
        writer.value(-336L);

        // Assert: The writer should still be in lenient mode.
        assertTrue("The writer's strictness should remain lenient after a value is written.", writer.isLenient());
    }
}