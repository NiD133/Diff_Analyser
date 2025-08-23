package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void writingNumberValue_shouldNotAlterSerializeNullsSetting() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setSerializeNulls(false);

        // Act
        // The value() method returns the same writer instance for chaining,
        // so we can perform the action and then assert on the original object.
        writer.value(Short.valueOf((short) 85));

        // Assert
        assertFalse("The serializeNulls setting should be preserved after writing a value.",
                writer.getSerializeNulls());
    }
}