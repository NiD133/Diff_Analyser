package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonNull;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public final class JsonTreeWriterTest {

  /**
   * Verifies that a new {@link JsonTreeWriter} on which no write operations have been performed
   * represents {@link JsonNull#INSTANCE}.
   */
  @Test
  public void get_onNewWriter_returnsJsonNull() {
    // Arrange: Create a new writer.
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act & Assert: The initial state of the writer should be a JSON null.
    assertThat(writer.get()).isSameInstanceAs(JsonNull.INSTANCE);
  }
}