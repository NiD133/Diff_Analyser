package com.google.gson.internal.bind;

import static org.junit.Assert.assertTrue;

import com.google.gson.Strictness;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void setStrictness_whenLenient_makesWriterLenient() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act
        writer.setStrictness(Strictness.LENIENT);

        // Assert
        assertTrue("Writer should be lenient after setting strictness to LENIENT.", writer.isLenient());
    }
}