package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the JsonTreeReader class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that hasNext() returns false after the single root element has been consumed.
     */
    @Test
    public void hasNext_afterConsumingRootElement_returnsFalse() throws IOException {
        // Arrange: Create a JsonTreeReader with a single JsonNull element.
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

        // Act: Consume the single null element from the reader.
        reader.nextNull();

        // Assert: Verify that the reader reports no more elements are available.
        assertFalse("hasNext() should return false after the only element is consumed", reader.hasNext());
    }
}