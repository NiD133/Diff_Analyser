package com.google.gson.internal.bind;

import static org.junit.Assert.assertFalse;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for {@link JsonTreeWriter}.
 * This class focuses on improving the understandability of an auto-generated test case.
 */
public class JsonTreeWriterTest {

    /**
     * Tests that the isHtmlSafe() property remains false (its default value)
     * after performing basic write operations like creating an empty array.
     */
    @Test
    public void isHtmlSafe_shouldBeFalseByDefaultAfterWriting() throws IOException {
        // Arrange: Create a new JsonTreeWriter. By default, HTML safe is false.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Write an empty JSON array.
        writer.beginArray();
        writer.endArray();

        // Assert: Verify that the isHtmlSafe property has not changed from its default value.
        assertFalse("isHtmlSafe() should be false by default", writer.isHtmlSafe());
    }
}