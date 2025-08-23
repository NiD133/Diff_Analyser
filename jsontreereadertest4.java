package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

  /**
   * Verifies that calling {@code skipValue()} when the reader is at the end of the document
   * has no effect. The reader's state should remain unchanged.
   */
  @Test
  public void skipValue_atEndOfDocument_isNoOp() throws IOException {
    // Arrange: Create a reader and consume an empty JSON object to reach the end.
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();

    // Verify the initial state is END_DOCUMENT before the main action.
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");

    // Act: Attempt to skip a value when there is nothing left to read.
    reader.skipValue();

    // Assert: The reader's state and path should be unchanged.
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }
}