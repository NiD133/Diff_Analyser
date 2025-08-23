package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeWriterTest {

  /**
   * Verifies that the {@link JsonTreeWriter#value(String)} method returns the same
   * writer instance, which allows for a fluent, chainable API.
   */
  @Test
  public void value_withString_returnsSameWriterInstanceForChaining() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    String testString = "some value";

    // Act
    JsonWriter returnedWriter = writer.value(testString);

    // Assert
    assertThat(returnedWriter).isSameInstanceAs(writer);
  }
}