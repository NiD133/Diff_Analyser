package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link TypeAdapter}.
 */
public class TypeAdapterTest {

  private static final TypeAdapter<String> STRING_ADAPTER = new TypeAdapter<>() {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  };

  @Test
  public void fromJsonString_withTrailingData_ignoresTrailingData() throws IOException {
    // This test verifies the documented behavior of fromJson(String), which is lenient
    // and ignores any data that follows the first complete JSON value.
    // The input contains a valid JSON string "a" followed by the character '1'.
    String jsonWithTrailingData = "\"a\"1";
    String expectedValue = "a";

    // Act
    String result = STRING_ADAPTER.fromJson(jsonWithTrailingData);

    // Assert
    assertThat(result).isEqualTo(expectedValue);
  }
}