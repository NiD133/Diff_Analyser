package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonTreeWriter}.
 * This class focuses on behaviors specific to or inherited by JsonTreeWriter.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the strictness setting, a feature from the parent {@link com.google.gson.stream.JsonWriter},
     * is correctly applied to the JsonTreeWriter instance.
     */
    @Test
    public void setStrictnessToLenientShouldMakeWriterLenient() {
        // Arrange: Create a new JsonTreeWriter. By default, it is not lenient.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Set the strictness mode to LENIENT.
        writer.setStrictness(Strictness.LENIENT);

        // Assert: Verify that the writer's mode is now lenient.
        assertTrue("Writer should be in lenient mode after setting Strictness.LENIENT", writer.isLenient());
    }
}