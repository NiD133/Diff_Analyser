package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

  @Test
  public void hasNext_shouldReturnFalse_afterConsumingRootElement() throws IOException {
    // Arrange: Create a reader for an empty JSON object, which is our root element.
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());

    // Act: Consume the entire root element by reading its beginning and end.
    reader.beginObject();
    reader.endObject();

    // Assert: hasNext() should now return false, as there are no more elements to read.
    assertThat(reader.hasNext()).isFalse();
  }
}