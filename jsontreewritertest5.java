package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

  /**
   * Verifies that any write operation on a closed {@link JsonTreeWriter}
   * throws an {@link IllegalStateException}.
   */
  @Test
  public void write_afterClose_throwsIllegalStateException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    // Write a valid JSON structure to ensure the writer is in a used state before closing.
    writer.beginArray();
    writer.value("A");
    writer.endArray();

    writer.close();

    // Act & Assert
    // Attempting to write again should fail because the writer is closed.
    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> writer.beginArray()
    );

    // Verify the exception message for a more precise test.
    assertThat(exception).hasMessageThat().isEqualTo("JsonWriter is closed.");
  }
}