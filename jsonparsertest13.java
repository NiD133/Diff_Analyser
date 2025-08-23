package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link JsonParser#parseReader(JsonReader)}, focusing on its handling of
 * {@link Strictness} settings.
 */
public class JsonParserTestTest13 {

  /**
   * Tests that {@code JsonParser.parseReader(JsonReader)} ignores the {@link Strictness#LEGACY_STRICT}
   * setting on the provided reader, parses leniently, and then restores the reader's original
   * strictness setting.
   */
  @Test
  public void parseReader_withLegacyStrictReader_parsesLenientlyAndRestoresStrictness() {
    // Arrange: Create a reader for an unquoted string, which is invalid in strict JSON.
    // Set its mode to LEGACY_STRICT.
    String unquotedJson = "unquoted";
    JsonReader reader = new JsonReader(new StringReader(unquotedJson));

    Strictness originalStrictness = Strictness.LEGACY_STRICT;
    reader.setStrictness(originalStrictness);

    // Act: Parse the content using the reader.
    // The JsonParser is expected to temporarily use lenient mode for this operation.
    JsonElement result = JsonParser.parseReader(reader);

    // Assert
    // 1. Verify that the unquoted string was parsed successfully, confirming lenient behavior.
    assertThat(result).isEqualTo(new JsonPrimitive(unquotedJson));

    // 2. Verify that the reader's original strictness was restored after the call.
    assertThat(reader.getStrictness()).isEqualTo(originalStrictness);
  }
}