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
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonParser} class. This class tests the parsing of various JSON
 * structures, including lenient and malformed JSON, and edge cases like deep nesting.
 */
public class JsonParserTest {

  @Test
  public void testParseString_unclosedArray_throwsException() {
    // Arrange
    String json = "[[]";

    // Act & Assert
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
  public void testParseString_unquotedMultiWordString_throwsException() {
    // Arrange
    String json = "Test is a test..blah blah";

    // Act & Assert
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(json));
  }

  @Test
  public void testParseString_lenient_unquotedKeyAndSingleQuotedValue() {
    // Arrange
    // Lenient JSON: unquoted property name `a` and single-quoted string `'c'`
    String json = "{a:10,b:'c'}";

    // Act
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.isJsonObject()).isTrue();
    JsonObject obj = element.getAsJsonObject();
    assertThat(obj.get("a").getAsInt()).isEqualTo(10);
    assertThat(obj.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseString_lenient_unquotedStringsInArray() {
    // Arrange
    String json = "[a,b,c]"; // Unquoted strings are not valid in strict JSON

    // Act
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.isJsonArray()).isTrue();
    JsonArray array = element.getAsJsonArray();
    assertThat(array.get(0).getAsString()).isEqualTo("a");
    assertThat(array.get(1).getAsString()).isEqualTo("b");
    assertThat(array.get(2).getAsString()).isEqualTo("c");
    assertThat(array).hasSize(3);
  }

  @Test
  public void testParseString_lenient_unquotedStringValue() {
    // Arrange
    String json = "Test"; // An unquoted string, not valid in strict JSON

    // Act
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.getAsString()).isEqualTo("Test");
  }

  @Test
  public void testParseString_stringWithWhitespaceContent_isPreserved() {
    // Arrange
    String json = "\"   \""; // A valid JSON string containing only whitespace

    // Act
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.isJsonPrimitive()).isTrue();
    assertThat(element.getAsString()).isEqualTo("   ");
  }

  @Test
  public void testParseString_whitespaceOnlyInput_returnsJsonNull() {
    // Arrange
    String json = "     ";

    // Act
    // `parseString` uses a lenient JsonReader which consumes whitespace. An empty input
    // is then parsed as a JsonNull.
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.isJsonNull()).isTrue();
  }

  @Test
  public void testParseString_mixedArray() {
    // Arrange
    String json = "[{},13,\"stringValue\"]";

    // Act
    JsonElement element = JsonParser.parseString(json);

    // Assert
    assertThat(element.isJsonArray()).isTrue();
    JsonArray array = element.getAsJsonArray();
    assertThat(array.get(0).isJsonObject()).isTrue();
    assertThat(array.get(1).getAsInt()).isEqualTo(13);
    assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
  }

  @Test
  public void testParseReader_lenient_unquotedKeyAndSingleQuotedValue() {
    // Arrange
    // Lenient JSON: unquoted property name `a` and single-quoted string `'c'`
    StringReader reader = new StringReader("{a:10,b:'c'}");

    // Act
    JsonElement element = JsonParser.parseReader(reader);

    // Assert
    assertThat(element.isJsonObject()).isTrue();
    JsonObject obj = element.getAsJsonObject();
    assertThat(obj.get("a").getAsInt()).isEqualTo(10);
    assertThat(obj.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseReader_multipleJsonElementsFromStream() throws IOException {
    // Arrange
    Gson gson = new Gson();
    BagOfPrimitives obj1 = new BagOfPrimitives(1, 1, true, "one");
    BagOfPrimitives obj2 = new BagOfPrimitives(2, 2, false, "two");

    String json = gson.toJson(obj1) + gson.toJson(obj2);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    // `parseReader(JsonReader)` is lenient by default and allows multiple top-level elements
    
    // Act
    JsonElement element1 = JsonParser.parseReader(jsonReader);
    JsonElement element2 = JsonParser.parseReader(jsonReader);

    // Assert
    BagOfPrimitives actual1 = gson.fromJson(element1, BagOfPrimitives.class);
    assertThat(actual1.stringValue).isEqualTo("one");

    BagOfPrimitives actual2 = gson.fromJson(element2, BagOfPrimitives.class);
    assertThat(actual2.stringValue).isEqualTo("two");
  }

  @Test
  public void testParseReader_legacyStrictIsIgnored_parsesLeniently() {
    // Arrange
    JsonReader reader = new JsonReader(new StringReader("unquoted"));
    Strictness originalStrictness = Strictness.LEGACY_STRICT;
    reader.setStrictness(originalStrictness);

    // Act
    // JsonParser.parseReader should internally use lenient mode, ignoring LEGACY_STRICT
    JsonElement element = JsonParser.parseReader(reader);

    // Assert
    assertThat(element).isEqualTo(new JsonPrimitive("unquoted"));
    // Verify that the original strictness of the reader is restored
    assertThat(reader.getStrictness()).isEqualTo(originalStrictness);
  }

  @Test
  public void testParseReader_strict_failsOnMalformedJson() {
    // Arrange
    JsonReader reader = new JsonReader(new StringReader("faLsE")); // Invalid boolean in strict mode
    Strictness originalStrictness = Strictness.STRICT;
    reader.setStrictness(originalStrictness);

    // Act & Assert
    // JsonParser.parseReader should respect STRICT mode and throw an exception
    var e = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
    assertThat(e)
        .hasCauseThat()
        .hasMessageThat()
        .contains("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");

    // Verify that the original strictness of the reader was not changed
    assertThat(reader.getStrictness()).isEqualTo(originalStrictness);
  }

  /** Deeply nested JSON arrays should not cause {@link StackOverflowError}. */
  @Test
  public void testParse_deeplyNestedArrays_noStackOverflow() throws IOException {
    // Arrange
    int nestingDepth = 10000;
    String json = "[".repeat(nestingDepth) + "]".repeat(nestingDepth);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    // Act
    JsonElement element = JsonParser.parseReader(jsonReader);

    // Assert
    int actualDepth = 0;
    JsonArray current = element.getAsJsonArray();
    // Traverse the nested arrays to verify the depth
    while (true) {
      actualDepth++;
      if (current.isEmpty()) {
        break;
      }
      assertThat(current.size()).isEqualTo(1);
      current = current.get(0).getAsJsonArray();
    }
    assertThat(actualDepth).isEqualTo(nestingDepth);
  }

  /** Deeply nested JSON objects should not cause {@link StackOverflowError}. */
  @Test
  public void testParse_deeplyNestedObjects_noStackOverflow() throws IOException {
    // Arrange
    int nestingDepth = 10000;
    String json = "{\"a\":".repeat(nestingDepth) + "null" + "}".repeat(nestingDepth);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    // Act
    JsonElement element = JsonParser.parseReader(jsonReader);

    // Assert
    int actualDepth = 0;
    JsonObject current = element.getAsJsonObject();
    // Traverse the nested objects to verify the depth
    while (true) {
      assertThat(current.size()).isEqualTo(1);
      actualDepth++;
      JsonElement next = current.get("a");
      if (next.isJsonNull()) {
        break;
      } else {
        current = next.getAsJsonObject();
      }
    }
    assertThat(actualDepth).isEqualTo(nestingDepth);
  }
}