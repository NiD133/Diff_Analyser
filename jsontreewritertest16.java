package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

  @Test
  public void value_withBoolean_writesBooleanPrimitive() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act
    writer.value(true);

    // Assert
    JsonElement result = writer.get();
    assertThat(result).isEqualTo(new JsonPrimitive(true));
  }
}