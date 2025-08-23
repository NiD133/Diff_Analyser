package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Tests that a new JsonTreeWriter instance is created with LEGACY_STRICT
     * strictness by default.
     */
    @Test
    public void jsonTreeWriterHasLegacyStrictnessByDefault() {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

        // Act & Assert
        // Verify that the default strictness level is the expected legacy value.
        assertEquals(Strictness.LEGACY_STRICT, jsonTreeWriter.getStrictness());
    }
}