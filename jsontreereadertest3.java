package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeReaderTestTest3 {

  /**
   * Tests that calling {@link JsonReader#skipValue()} when the reader is positioned at a property
   * name skips only the name and advances the reader to the corresponding value.
   */
  @Test
  public void skipValue_whenAtName_skipsOnlyNameAndPositionsReaderAtValue() throws IOException {
    // Arrange
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("property-name", "value");
    JsonReader reader = new JsonTreeReader(jsonObject);

    // Position the reader at the property name
    reader.beginObject();
    // Verify our starting position is a NAME token, which is the scenario under test
    assertThat(reader.peek()).isEqualTo(JsonToken.NAME);

    // Act
    reader.skipValue();

    // Assert
    // The reader should now be positioned at the value token.
    assertThat(reader.peek()).isEqualTo(JsonToken.STRING);
    // The path should indicate that a name was skipped.
    assertThat(reader.getPath()).isEqualTo("$.<skipped>");
    // Consuming the next token should return the property's value, confirming it was not skipped.
    assertThat(reader.nextString()).isEqualTo("value");
  }
}