package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

  @Test
  public void writingNullObjectProperty_whenSerializeNullsIsFalse_omitsProperty() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(false);

    // Act: Write an object with a single property whose value is null.
    writer.beginObject();
    writer.name("a");
    writer.nullValue();
    writer.endObject();

    // Assert
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject(); // An empty JSON object

    assertThat(result).isEqualTo(expected);
  }
}