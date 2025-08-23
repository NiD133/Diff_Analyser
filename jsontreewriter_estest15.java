package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the JsonTreeWriter.
 * The original test class name `JsonTreeWriter_ESTestTest15` suggests it was
 * automatically generated. A more conventional name would be `JsonTreeWriterTest`.
 */
public class JsonTreeWriter_ESTestTest15 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that a new JsonTreeWriter instance is configured to be strict by default.
     * Strict writers enforce the JSON specification more rigorously than lenient ones.
     */
    @Test
    public void newJsonTreeWriterIsStrictByDefault() {
        // Arrange: Create a new instance of the writer.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Assert: Check that the writer is not in lenient mode.
        assertFalse("A new JsonTreeWriter should be strict (not lenient) by default.", writer.isLenient());
    }
}