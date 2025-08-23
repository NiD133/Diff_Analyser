package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

// Note: The original test class name "JsonTreeWriter_ESTestTest33" and its
// inheritance from "JsonTreeWriter_ESTest_scaffolding" suggest it was
// auto-generated. A more conventional name would be "JsonTreeWriterTest".
public class JsonTreeWriter_ESTestTest33 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that a JsonTreeWriter is not HTML-safe by default after an array has been started.
     * The isHtmlSafe property is inherited from the base JsonWriter class and its default
     * value is false. This test ensures that behavior is preserved.
     */
    @Test
    public void beginArray_returnsWriterThatIsNotHtmlSafeByDefault() {
        // Arrange
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

        // Act
        // The beginArray() method returns the same writer instance.
        JsonWriter writer = jsonTreeWriter.beginArray();

        // Assert
        assertFalse("JsonTreeWriter should not be HTML-safe by default", writer.isHtmlSafe());
    }
}