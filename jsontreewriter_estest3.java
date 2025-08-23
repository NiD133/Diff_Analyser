package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void newWriterIsNotLenientByDefault() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act & Assert
        // By default, a writer should adhere to strict JSON rules.
        assertFalse("A newly created JsonTreeWriter should not be lenient by default", writer.isLenient());
    }
}