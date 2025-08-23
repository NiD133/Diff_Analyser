package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void newJsonTreeWriter_shouldHaveLegacyStrictnessByDefault() {
        // Arrange: Create a new instance of the writer.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Retrieve the strictness level.
        Strictness actualStrictness = writer.getStrictness();

        // Assert: Verify that the default strictness is LEGACY_STRICT.
        assertEquals(Strictness.LEGACY_STRICT, actualStrictness);
    }
}