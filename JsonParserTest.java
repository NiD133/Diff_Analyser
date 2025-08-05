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
import com.google.gson.stream.MalformedJsonException;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for {@link JsonParser}, focusing on JSON parsing behavior in various scenarios.
 */
public class JsonParserTest {
  private static final String UNQUOTED_SINGLE_WORD = "Test";
  private static final String UNQUOTED_MULTI_WORD = "Test is a test..blah blah";
  private static final String UNQUOTED_ARRAY_JSON = "[a,b,c]";
  private static final String OBJECT_JSON = "{a:10,b:'c'}";
  private static final String EMPTY_STRING_JSON = "\"   \"";
  private static final String WHITESPACE_ONLY_JSON = "     ";
  private static final String MIXED_ARRAY_JSON = "[{},13,\"stringValue\"]";
  private static final String INVALID_JSON = "[[]";

  // ==================== parseString() Tests ====================

  @Test
  public void testParseString_invalidJsonThrowsException() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(INVALID_JSON));
  }

  @Test
  public void testParseString_unquotedStringArray() {
    JsonElement element = JsonParser.parseString(UNQUOTED_ARRAY_JSON);

    JsonArray array = element.getAsJsonArray();
    assertThat(array).hasSize(3);
    assertThat(array.get(0).getAsString()).isEqualTo("a");
    assertThat(array.get(1).getAsString()).isEqualTo("b");
    assertThat(array.get(2).getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseString_validObject() {
    JsonElement element = JsonParser.parseString(OBJECT_JSON);

    JsonObject object = element.getAsJsonObject();
    assertThat(object.get("a").getAsInt()).isEqualTo(10);
    assertThat(object.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseString_emptyStringValue() {
    JsonElement element = JsonParser.parseString(EMPTY_STRING_JSON);

    assertThat(element.getAsString()).isEqualTo("   ");
  }

  @Test
  public void testParseString_whitespaceOnlyYieldsNull() {
    JsonElement element = JsonParser.parseString(WHITESPACE_ONLY_JSON);

    assertThat(element.isJsonNull()).isTrue();
  }

  @Test
  public void testParseString_unquotedSingleWord() {
    JsonElement element = JsonParser.parseString(UNQUOTED_SINGLE_WORD);
    assertThat(element.getAsString()).isEqualTo(UNQUOTED_SINGLE_WORD);
  }

  @Test
  public void testParseString_unquotedMultiWordThrowsException() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(UNQUOTED_MULTI_WORD));
  }

  @Test
  public void testParseString_mixedArray() {
    JsonElement element = JsonParser.parseString(MIXED_ARRAY_JSON);

    JsonArray array = element.getAsJsonArray();
    assertThat(array.get(0).toString()).isEqualTo("{}");
    assertThat(array.get(1).getAsInt()).isEqualTo(13);
    assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
  }

  // ==================== parseReader() Tests ====================

  @Test
  public void testParseReader_validObject() {
    JsonElement element = JsonParser.parseReader(new StringReader(OBJECT_JSON));

    JsonObject object = element.getAsJsonObject();
    assertThat(object.get("a").getAsInt()).isEqualTo(10);
    assertThat(object.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseReader_twoConsecutiveObjects() throws Exception {
    Gson gson = new Gson();
    CharArrayWriter writer = new CharArrayWriter();

    // Write two consecutive JSON objects to the stream
    BagOfPrimitives first = new BagOfPrimitives(1, 1, true, "one");
    BagOfPrimitives second = new BagOfPrimitives(2, 2, false, "two");
    writer.write(gson.toJson(first).toCharArray());
    writer.write(gson.toJson(second).toCharArray());

    // Parse the stream containing two objects
    JsonReader parser = new JsonReader(new CharArrayReader(writer.toCharArray()));
    parser.setStrictness(Strictness.LENIENT);
    JsonElement element1 = Streams.parse(parser);
    JsonElement element2 = Streams.parse(parser);

    // Verify parsed objects
    assertThat(gson.fromJson(element1, BagOfPrimitives.class).stringValue).isEqualTo("one");
    assertThat(gson.fromJson(element2, BagOfPrimitives.class).stringValue).isEqualTo("two");
  }

  // ==================== Edge Cases ====================

  @Test
  public void testParseReader_legacyStrictModeParsesLeniently() {
    JsonReader reader = new JsonReader(new StringReader("unquoted"));
    Strictness strictness = Strictness.LEGACY_STRICT;
    reader.setStrictness(strictness);

    // LEGACY_STRICT is internally treated as LENIENT by JsonParser
    JsonElement result = JsonParser.parseReader(reader);
    assertThat(result).isEqualTo(new JsonPrimitive("unquoted"));
    assertThat(reader.getStrictness()).isEqualTo(strictness); // Original strictness restored
  }

  @Test
  public void testParseReader_strictModeThrowsException() {
    JsonReader reader = new JsonReader(new StringReader("faLsE")); // Malformed boolean
    Strictness strictness = Strictness.STRICT;
    reader.setStrictness(strictness);

    // Should throw because strict mode requires correct casing
    JsonSyntaxException e = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
    assertThat(e)
        .hasCauseThat()
        .isInstanceOf(MalformedJsonException.class);
    assertThat(reader.getStrictness()).isEqualTo(strictness); // Original strictness maintained
  }

  // ==================== Deeply Nested Structures ====================

  @Test
  public void testParseDeeplyNestedArrays() throws IOException {
    int depth = 10000;
    String json = createDeeplyNestedArrayJson(depth);

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    JsonArray current = JsonParser.parseReader(jsonReader).getAsJsonArray();
    int actualDepth = countNestedArrays(current);

    assertThat(actualDepth).isEqualTo(depth);
  }

  @Test
  public void testParseDeeplyNestedObjects() throws IOException {
    int depth = 10000;
    String json = createDeeplyNestedObjectJson(depth);

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    JsonObject current = JsonParser.parseReader(jsonReader).getAsJsonObject();
    int actualDepth = countNestedObjects(current);

    assertThat(actualDepth).isEqualTo(depth);
  }

  // ==================== Helper Methods ====================

  private static String createDeeplyNestedArrayJson(int depth) {
    return "[".repeat(depth) + "]".repeat(depth);
  }

  private static String createDeeplyNestedObjectJson(int depth) {
    return "{\"a\":".repeat(depth) + "null" + "}".repeat(depth);
  }

  private static int countNestedArrays(JsonArray current) {
    int depth = 0;
    while (!current.isEmpty()) {
      depth++;
      assertThat(current.size()).isEqualTo(1);
      current = current.get(0).getAsJsonArray();
    }
    return depth;
  }

  private static int countNestedObjects(JsonObject current) {
    int depth = 0;
    while (true) {
      depth++;
      JsonElement next = current.get("a");
      if (next.isJsonNull()) {
        break;
      }
      current = next.getAsJsonObject();
    }
    return depth;
  }
}