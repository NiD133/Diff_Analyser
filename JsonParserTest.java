/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.common.TestTypes.BagOfPrimitives;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for {@link JsonParser}
 *
 * The tests prefer explicit naming, clear Arrange-Act-Assert structure, and small helpers
 * to keep assertions focused on behavior rather than parsing mechanics.
 */
public class JsonParserTest {

  // Test data used across multiple tests
  private static final String SIMPLE_OBJECT_JSON = "{a:10,b:'c'}";
  private static final String UNQUOTED_STRING_ARRAY_JSON = "[a,b,c]";
  private static final String MIXED_ARRAY_JSON = "[{},13,\"stringValue\"]";
  private static final int DEEP_NESTING = 10_000;

  // Small helpers for readability

  private static JsonObject parseObject(String json) {
    return JsonParser.parseString(json).getAsJsonObject();
  }

  private static JsonArray parseArray(String json) {
    return JsonParser.parseString(json).getAsJsonArray();
  }

  private static JsonReader newJsonReader(String json) {
    return new JsonReader(new StringReader(json));
  }

  @Test
  public void parseInvalidJson_throwsSyntaxException() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString("[[]"));
  }

  @Test
  public void parsesUnquotedStringArray_inLenientMode() {
    // Arrange + Act
    JsonArray array = parseArray(UNQUOTED_STRING_ARRAY_JSON);

    // Assert
    assertThat(array).hasSize(3);
    assertThat(array.get(0).getAsString()).isEqualTo("a");
    assertThat(array.get(1).getAsString()).isEqualTo("b");
    assertThat(array.get(2).getAsString()).isEqualTo("c");
  }

  @Test
  public void parsesObjectWithUnquotedKeysAndSingleQuotedStrings_inLenientMode() {
    // Arrange + Act
    JsonObject obj = parseObject(SIMPLE_OBJECT_JSON);

    // Assert
    assertThat(obj.get("a").getAsInt()).isEqualTo(10);
    assertThat(obj.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void parseQuotedEmptyString_returnsPrimitive() {
    // Arrange + Act
    JsonElement e = JsonParser.parseString("\"   \"");

    // Assert
    assertThat(e.isJsonPrimitive()).isTrue();
    assertThat(e.getAsString()).isEqualTo("   ");
  }

  @Test
  public void parseWhitespaceOnlyInput_returnsJsonNull() {
    // Arrange + Act
    JsonElement e = JsonParser.parseString("     ");

    // Assert
    assertThat(e.isJsonNull()).isTrue();
  }

  @Test
  public void parsesUnquotedSingleWordString_inLenientMode() {
    // Arrange + Act
    JsonElement e = JsonParser.parseString("Test");

    // Assert
    assertThat(e.getAsString()).isEqualTo("Test");
  }

  @Test
  public void parseUnquotedMultiWordString_rejected() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString("Test is a test..blah blah"));
  }

  @Test
  public void parseMixedArray_typesPreserved() {
    // Arrange + Act
    JsonArray array = parseArray(MIXED_ARRAY_JSON);

    // Assert
    assertThat(array.get(0).toString()).isEqualTo("{}");
    assertThat(array.get(1).getAsInt()).isEqualTo(13);
    assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
  }

  /**
   * Deeply nested JSON arrays should not cause StackOverflowError.
   * This uses a very high nesting depth and a JsonReader with a large nesting limit.
   */
  @Test
  public void parseDeeplyNestedArrays_noStackOverflow() throws IOException {
    // [[[ ... ]]]
    String json = "[".repeat(DEEP_NESTING) + "]".repeat(DEEP_NESTING);
    JsonReader jsonReader = newJsonReader(json);
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    int levelsSeen = 0;

    // Act
    JsonArray current = JsonParser.parseReader(jsonReader).getAsJsonArray();

    // Assert: walk down the nested arrays counting each level
    while (true) {
      levelsSeen++;
      if (current.isEmpty()) {
        break;
      }
      assertThat(current.size()).isEqualTo(1);
      current = current.get(0).getAsJsonArray();
    }
    assertThat(levelsSeen).isEqualTo(DEEP_NESTING);
  }

  /**
   * Deeply nested JSON objects should not cause StackOverflowError.
   * Nesting pattern: {"a":{"a": ... {"a":null} ... }}
   */
  @Test
  public void parseDeeplyNestedObjects_noStackOverflow() throws IOException {
    String json = "{\"a\":".repeat(DEEP_NESTING) + "null" + "}".repeat(DEEP_NESTING);
    JsonReader jsonReader = newJsonReader(json);
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    int levelsSeen = 0;

    // Act
    JsonObject current = JsonParser.parseReader(jsonReader).getAsJsonObject();

    // Assert: follow the "a" chain until reaching null
    while (true) {
      assertThat(current.size()).isEqualTo(1);
      levelsSeen++;
      JsonElement next = current.get("a");
      if (next.isJsonNull()) {
        break;
      }
      current = next.getAsJsonObject();
    }
    assertThat(levelsSeen).isEqualTo(DEEP_NESTING);
  }

  @Test
  public void parseReader_withStringReader_parsesObject() {
    // Arrange
    StringReader reader = new StringReader(SIMPLE_OBJECT_JSON);

    // Act
    JsonElement e = JsonParser.parseReader(reader);

    // Assert
    assertThat(e.isJsonObject()).isTrue();
    JsonObject obj = e.getAsJsonObject();
    assertThat(obj.get("a").getAsInt()).isEqualTo(10);
    assertThat(obj.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void readWriteTwoConcatenatedObjects_lenientMode_parsesBoth() throws Exception {
    // Arrange: write two JSON objects back-to-back (no delimiter)
    Gson gson = new Gson();
    CharArrayWriter writer = new CharArrayWriter();
    BagOfPrimitives expectedOne = new BagOfPrimitives(1, 1, true, "one");
    BagOfPrimitives expectedTwo = new BagOfPrimitives(2, 2, false, "two");
    writer.write(gson.toJson(expectedOne).toCharArray());
    writer.write(gson.toJson(expectedTwo).toCharArray());

    // Act: read them sequentially
    CharArrayReader reader = new CharArrayReader(writer.toCharArray());
    JsonReader jsonReader = new JsonReader(reader);
    jsonReader.setStrictness(Strictness.LENIENT);
    JsonElement element1 = Streams.parse(jsonReader);
    JsonElement element2 = Streams.parse(jsonReader);

    // Assert
    BagOfPrimitives actualOne = gson.fromJson(element1, BagOfPrimitives.class);
    BagOfPrimitives actualTwo = gson.fromJson(element2, BagOfPrimitives.class);
    assertThat(actualOne.stringValue).isEqualTo("one");
    assertThat(actualTwo.stringValue).isEqualTo("two");
  }

  @Test
  public void parseReader_withLegacyStrict_parsesLenientAndRestoresOriginalStrictness() {
    // Arrange
    JsonReader reader = newJsonReader("unquoted");
    Strictness original = Strictness.LEGACY_STRICT;
    // LEGACY_STRICT is ignored by JsonParser; parsing is performed leniently instead.
    reader.setStrictness(original);

    // Act + Assert
    assertThat(JsonParser.parseReader(reader)).isEqualTo(new JsonPrimitive("unquoted"));
    // Original strictness was restored
    assertThat(reader.getStrictness()).isEqualTo(original);
  }

  @Test
  public void parseReader_inStrictMode_rejectsMalformedAndKeepsStrictness() {
    // Arrange
    JsonReader reader = newJsonReader("faLsE");
    Strictness original = Strictness.STRICT;
    reader.setStrictness(original);

    // Act
    JsonSyntaxException e = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));

    // Assert
    assertThat(e)
        .hasCauseThat()
        .hasMessageThat()
        .startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    // Original strictness was kept
    assertThat(reader.getStrictness()).isEqualTo(original);
  }
}