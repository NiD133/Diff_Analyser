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
 * Unit tests for {@link JsonParser} functionality including parsing various JSON formats,
 * handling malformed JSON, and testing strictness modes.
 *
 * @author Inderjeet Singh
 */
public class JsonParserTest {

  // Constants for test data
  private static final String VALID_JSON_OBJECT = "{a:10,b:'c'}";
  private static final String MIXED_ARRAY_JSON = "[{},13,\"stringValue\"]";
  private static final String UNQUOTED_ARRAY_JSON = "[a,b,c]";
  private static final String MALFORMED_JSON = "[[]";
  private static final String MULTI_WORD_UNQUOTED = "Test is a test..blah blah";
  private static final String SINGLE_WORD_UNQUOTED = "Test";
  private static final String QUOTED_WHITESPACE = "\"   \"";
  private static final String ONLY_WHITESPACE = "     ";
  private static final String MALFORMED_BOOLEAN = "faLsE";
  private static final String UNQUOTED_STRING = "unquoted";
  
  private static final int DEEP_NESTING_LEVEL = 10000;

  // ========== Basic Parsing Tests ==========

  @Test
  public void parseString_withValidJsonObject_shouldReturnCorrectJsonElement() {
    JsonElement element = JsonParser.parseString(VALID_JSON_OBJECT);
    
    assertThat(element.isJsonObject()).isTrue();
    JsonObject jsonObject = element.getAsJsonObject();
    assertThat(jsonObject.get("a").getAsInt()).isEqualTo(10);
    assertThat(jsonObject.get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void parseString_withMixedArray_shouldParseAllElementsCorrectly() {
    JsonElement element = JsonParser.parseString(MIXED_ARRAY_JSON);
    
    assertThat(element.isJsonArray()).isTrue();
    JsonArray array = element.getAsJsonArray();
    assertThat(array.get(0).toString()).isEqualTo("{}");
    assertThat(array.get(1).getAsInt()).isEqualTo(13);
    assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
  }

  @Test
  public void parseReader_withValidJsonObject_shouldReturnCorrectJsonElement() {
    StringReader reader = new StringReader(VALID_JSON_OBJECT);
    
    JsonElement element = JsonParser.parseReader(reader);
    
    assertThat(element.isJsonObject()).isTrue();
    JsonObject jsonObject = element.getAsJsonObject();
    assertThat(jsonObject.get("a").getAsInt()).isEqualTo(10);
    assertThat(jsonObject.get("b").getAsString()).isEqualTo("c");
  }

  // ========== Edge Case Tests ==========

  @Test
  public void parseString_withQuotedWhitespace_shouldPreserveWhitespace() {
    JsonElement element = JsonParser.parseString(QUOTED_WHITESPACE);
    
    assertThat(element.isJsonPrimitive()).isTrue();
    assertThat(element.getAsString()).isEqualTo("   ");
  }

  @Test
  public void parseString_withOnlyWhitespace_shouldReturnJsonNull() {
    JsonElement element = JsonParser.parseString(ONLY_WHITESPACE);
    
    assertThat(element.isJsonNull()).isTrue();
  }

  // ========== Lenient Parsing Tests ==========

  @Test
  public void parseString_withUnquotedArrayElements_shouldParseInLenientMode() {
    JsonElement element = JsonParser.parseString(UNQUOTED_ARRAY_JSON);
    
    JsonArray array = element.getAsJsonArray();
    assertThat(array).hasSize(3);
    assertThat(array.get(0).getAsString()).isEqualTo("a");
    assertThat(array.get(1).getAsString()).isEqualTo("b");
    assertThat(array.get(2).getAsString()).isEqualTo("c");
  }

  @Test
  public void parseString_withSingleUnquotedWord_shouldParseSuccessfully() {
    JsonElement element = JsonParser.parseString(SINGLE_WORD_UNQUOTED);
    
    assertThat(element.getAsString()).isEqualTo("Test");
  }

  // ========== Error Handling Tests ==========

  @Test
  public void parseString_withMalformedJson_shouldThrowJsonSyntaxException() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(MALFORMED_JSON));
  }

  @Test
  public void parseString_withMultiWordUnquotedString_shouldThrowJsonSyntaxException() {
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString(MULTI_WORD_UNQUOTED));
  }

  // ========== Deep Nesting Tests ==========

  /**
   * Tests that deeply nested JSON arrays don't cause {@link StackOverflowError}.
   * Creates an array structure like: [[[[...]]]] with 10,000 levels of nesting.
   */
  @Test
  public void parseReader_withDeeplyNestedArrays_shouldNotCauseStackOverflow() throws IOException {
    String deeplyNestedArrayJson = createDeeplyNestedArrayJson(DEEP_NESTING_LEVEL);
    JsonReader jsonReader = createJsonReaderWithUnlimitedNesting(deeplyNestedArrayJson);

    JsonArray rootArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
    
    int actualDepth = countArrayNestingDepth(rootArray);
    assertThat(actualDepth).isEqualTo(DEEP_NESTING_LEVEL);
  }

  /**
   * Tests that deeply nested JSON objects don't cause {@link StackOverflowError}.
   * Creates an object structure like: {"a":{"a":{"a":...null}}} with 10,000 levels of nesting.
   */
  @Test
  public void parseReader_withDeeplyNestedObjects_shouldNotCauseStackOverflow() throws IOException {
    String deeplyNestedObjectJson = createDeeplyNestedObjectJson(DEEP_NESTING_LEVEL);
    JsonReader jsonReader = createJsonReaderWithUnlimitedNesting(deeplyNestedObjectJson);

    JsonObject rootObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
    
    int actualDepth = countObjectNestingDepth(rootObject);
    assertThat(actualDepth).isEqualTo(DEEP_NESTING_LEVEL);
  }

  // ========== Multiple Object Parsing Tests ==========

  @Test
  public void parseMultipleObjects_withStreams_shouldParseEachObjectCorrectly() throws Exception {
    BagOfPrimitives firstObject = new BagOfPrimitives(1, 1, true, "one");
    BagOfPrimitives secondObject = new BagOfPrimitives(2, 2, false, "two");
    CharArrayReader reader = createReaderWithMultipleObjects(firstObject, secondObject);

    JsonReader parser = new JsonReader(reader);
    parser.setStrictness(Strictness.LENIENT);
    
    JsonElement firstElement = Streams.parse(parser);
    JsonElement secondElement = Streams.parse(parser);
    
    Gson gson = new Gson();
    BagOfPrimitives parsedFirst = gson.fromJson(firstElement, BagOfPrimitives.class);
    BagOfPrimitives parsedSecond = gson.fromJson(secondElement, BagOfPrimitives.class);
    
    assertThat(parsedFirst.stringValue).isEqualTo("one");
    assertThat(parsedSecond.stringValue).isEqualTo("two");
  }

  // ========== Strictness Mode Tests ==========

  @Test
  public void parseReader_withLegacyStrictMode_shouldIgnoreStrictnessAndParseInLenientMode() {
    JsonReader reader = new JsonReader(new StringReader(UNQUOTED_STRING));
    Strictness originalStrictness = Strictness.LEGACY_STRICT;
    reader.setStrictness(originalStrictness);

    // LEGACY_STRICT is ignored by JsonParser; it parses in lenient mode instead
    JsonElement result = JsonParser.parseReader(reader);
    
    assertThat(result).isEqualTo(new JsonPrimitive(UNQUOTED_STRING));
    assertThat(reader.getStrictness()).isEqualTo(originalStrictness); // Original strictness restored
  }

  @Test
  public void parseReader_withStrictMode_shouldThrowExceptionForMalformedJson() {
    JsonReader reader = new JsonReader(new StringReader(MALFORMED_BOOLEAN));
    Strictness originalStrictness = Strictness.STRICT;
    reader.setStrictness(originalStrictness);

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, 
        () -> JsonParser.parseReader(reader));
    
    assertThat(exception)
        .hasCauseThat()
        .hasMessageThat()
        .startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    assertThat(reader.getStrictness()).isEqualTo(originalStrictness); // Original strictness preserved
  }

  // ========== Helper Methods ==========

  private String createDeeplyNestedArrayJson(int nestingLevel) {
    return "[".repeat(nestingLevel) + "]".repeat(nestingLevel);
  }

  private String createDeeplyNestedObjectJson(int nestingLevel) {
    return "{\"a\":".repeat(nestingLevel) + "null" + "}".repeat(nestingLevel);
  }

  private JsonReader createJsonReaderWithUnlimitedNesting(String json) {
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);
    return jsonReader;
  }

  private int countArrayNestingDepth(JsonArray rootArray) {
    int depth = 0;
    JsonArray current = rootArray;
    
    while (true) {
      depth++;
      if (current.isEmpty()) {
        break;
      }
      assertThat(current.size()).isEqualTo(1);
      current = current.get(0).getAsJsonArray();
    }
    
    return depth;
  }

  private int countObjectNestingDepth(JsonObject rootObject) {
    int depth = 0;
    JsonObject current = rootObject;
    
    while (true) {
      assertThat(current.size()).isEqualTo(1);
      depth++;
      JsonElement next = current.get("a");
      if (next.isJsonNull()) {
        break;
      }
      current = next.getAsJsonObject();
    }
    
    return depth;
  }

  private CharArrayReader createReaderWithMultipleObjects(BagOfPrimitives first, BagOfPrimitives second) 
      throws Exception {
    Gson gson = new Gson();
    CharArrayWriter writer = new CharArrayWriter();
    writer.write(gson.toJson(first).toCharArray());
    writer.write(gson.toJson(second).toCharArray());
    return new CharArrayReader(writer.toCharArray());
  }
}