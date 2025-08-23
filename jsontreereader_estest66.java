package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

// The original test class name is kept for context.
// In a real-world scenario, it would be renamed to something like JsonTreeReaderTest.
public class JsonTreeReader_ESTestTest66 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Tests that calling skipValue() after the entire JSON tree has been consumed
     * is a no-op and does not throw an exception.
     */
    @Test
    public void skipValue_afterConsumingRootElement_isNoOp() throws IOException {
        // Arrange: Create a reader for a single JsonNull element.
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

        // First, consume the single element to advance the reader to the end of the document.
        reader.nextJsonElement();
        assertEquals("Reader should be at the end after consuming the only element",
            JsonToken.END_DOCUMENT, reader.peek());

        // Act: Call skipValue() when there is nothing left to read.
        reader.skipValue();

        // Assert: The reader should remain at the end of the document, confirming
        // that the skipValue() call was effectively a no-op.
        assertEquals("Reader should still be at the end after a no-op skip",
            JsonToken.END_DOCUMENT, reader.peek());
        
        // Also, verify that the operation did not unexpectedly change the reader's configuration.
        assertFalse("Reader leniency should not be affected", reader.isLenient());
    }
}