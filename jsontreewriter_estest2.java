package com.google.gson.internal.bind;

import static org.junit.Assert.assertTrue;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that setting the strictness to LENIENT correctly updates the writer's internal state.
     */
    @Test
    public void setStrictness_whenLenient_writerBecomesLenient() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        // The setStrictness method is inherited from the parent JsonWriter class.
        writer.setStrictness(Strictness.LENIENT);

        // Assert
        assertTrue("The writer should be in lenient mode after being configured as such.", writer.isLenient());
    }
}