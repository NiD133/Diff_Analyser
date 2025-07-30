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
 * This test suite verifies the behavior of the JsonParser class, ensuring it correctly parses
 * various JSON inputs and handles errors appropriately.
 */
public class JsonParserTest {

  @Test
  public void testParseInvalidJson() {
    // Test that parsing an invalid JSON string throws a JsonSyntaxException
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString("[[]"));
  }

  @Test
  public void testParseUnquotedStringArrayFails() {
    // Test parsing a JSON array with unquoted strings
    JsonElement element = JsonParser.parseString("[a,b,c]");
    assertThat(element.getAsJsonArray().get(0).getAsString()).isEqualTo("a");
    assertThat(element.getAsJsonArray().get(1).getAsString()).isEqualTo("b");
    assertThat(element.getAsJsonArray().get(2).getAsString()).isEqualTo("c");
    assertThat(element.getAsJsonArray()).hasSize(3);
  }

  @Test
  public void testParseString() {
    // Test parsing a JSON object with mixed types
    String json = "{a:10,b:'c'}";
    JsonElement element = JsonParser.parseString(json);
    assertThat(element.isJsonObject()).isTrue();
    assertThat(element.getAsJsonObject().get("a").getAsInt()).isEqualTo(10);
    assertThat(element.getAsJsonObject().get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testParseEmptyString() {
    // Test parsing a JSON string with spaces
    JsonElement element = JsonParser.parseString("\"   \"");
    assertThat(element.isJsonPrimitive()).isTrue();
    assertThat(element.getAsString()).isEqualTo("   ");
  }

  @Test
  public void testParseEmptyWhitespaceInput() {
    // Test parsing a JSON input with only whitespace
    JsonElement element = JsonParser.parseString("     ");
    assertThat(element.isJsonNull()).isTrue();
  }

  @Test
  public void testParseUnquotedSingleWordStringFails() {
    // Test parsing an unquoted single word string
    assertThat(JsonParser.parseString("Test").getAsString()).isEqualTo("Test");
  }

  @Test
  public void testParseUnquotedMultiWordStringFails() {
    // Test that parsing an unquoted multi-word string throws a JsonSyntaxException
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString("Test is a test..blah blah"));
  }

  @Test
  public void testParseMixedArray() {
    // Test parsing a JSON array with mixed types
    String json = "[{},13,\"stringValue\"]";
    JsonElement element = JsonParser.parseString(json);
    assertThat(element.isJsonArray()).isTrue();

    JsonArray array = element.getAsJsonArray();
    assertThat(array.get(0).toString()).isEqualTo("{}");
    assertThat(array.get(1).getAsInt()).isEqualTo(13);
    assertThat(array.get(2).getAsString()).isEqualTo("stringValue");
  }

  /** Deeply nested JSON arrays should not cause {@link StackOverflowError} */
  @Test
  public void testParseDeeplyNestedArrays() throws IOException {
    // Test parsing deeply nested JSON arrays
    int nestingLevel = 10000;
    String json = "[".repeat(nestingLevel) + "]".repeat(nestingLevel);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    int actualNestingLevel = 0;
    JsonArray currentArray = JsonParser.parseReader(jsonReader).getAsJsonArray();
    while (true) {
      actualNestingLevel++;
      if (currentArray.isEmpty()) {
        break;
      }
      assertThat(currentArray.size()).isEqualTo(1);
      currentArray = currentArray.get(0).getAsJsonArray();
    }
    assertThat(actualNestingLevel).isEqualTo(nestingLevel);
  }

  /** Deeply nested JSON objects should not cause {@link StackOverflowError} */
  @Test
  public void testParseDeeplyNestedObjects() throws IOException {
    // Test parsing deeply nested JSON objects
    int nestingLevel = 10000;
    String json = "{\"a\":".repeat(nestingLevel) + "null" + "}".repeat(nestingLevel);
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setNestingLimit(Integer.MAX_VALUE);

    int actualNestingLevel = 0;
    JsonObject currentObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
    while (true) {
      assertThat(currentObject.size()).isEqualTo(1);
      actualNestingLevel++;
      JsonElement nextElement = currentObject.get("a");
      if (nextElement.isJsonNull()) {
        break;
      } else {
        currentObject = nextElement.getAsJsonObject();
      }
    }
    assertThat(actualNestingLevel).isEqualTo(nestingLevel);
  }

  @Test
  public void testParseReader() {
    // Test parsing JSON from a StringReader
    StringReader reader = new StringReader("{a:10,b:'c'}");
    JsonElement element = JsonParser.parseReader(reader);
    assertThat(element.isJsonObject()).isTrue();
    assertThat(element.getAsJsonObject().get("a").getAsInt()).isEqualTo(10);
    assertThat(element.getAsJsonObject().get("b").getAsString()).isEqualTo("c");
  }

  @Test
  public void testReadWriteTwoObjects() throws Exception {
    // Test reading and writing two JSON objects
    Gson gson = new Gson();
    CharArrayWriter writer = new CharArrayWriter();
    BagOfPrimitives firstObject = new BagOfPrimitives(1, 1, true, "one");
    writer.write(gson.toJson(firstObject).toCharArray());
    BagOfPrimitives secondObject = new BagOfPrimitives(2, 2, false, "two");
    writer.write(gson.toJson(secondObject).toCharArray());
    CharArrayReader reader = new CharArrayReader(writer.toCharArray());

    JsonReader jsonReader = new JsonReader(reader);
    jsonReader.setStrictness(Strictness.LENIENT);
    JsonElement element1 = Streams.parse(jsonReader);
    JsonElement element2 = Streams.parse(jsonReader);
    BagOfPrimitives actualFirstObject = gson.fromJson(element1, BagOfPrimitives.class);
    assertThat(actualFirstObject.stringValue).isEqualTo("one");
    BagOfPrimitives actualSecondObject = gson.fromJson(element2, BagOfPrimitives.class);
    assertThat(actualSecondObject.stringValue).isEqualTo("two");
  }

  @Test
  public void testLegacyStrict() {
    // Test parsing with LEGACY_STRICT mode
    JsonReader reader = new JsonReader(new StringReader("unquoted"));
    Strictness strictness = Strictness.LEGACY_STRICT;
    reader.setStrictness(strictness);

    assertThat(JsonParser.parseReader(reader)).isEqualTo(new JsonPrimitive("unquoted"));
    assertThat(reader.getStrictness()).isEqualTo(strictness);
  }

  @Test
  public void testStrict() {
    // Test parsing with STRICT mode, expecting a JsonSyntaxException
    JsonReader reader = new JsonReader(new StringReader("faLsE"));
    Strictness strictness = Strictness.STRICT;
    reader.setStrictness(strictness);

    var exception = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
    assertThat(exception)
        .hasCauseThat()
        .hasMessageThat()
        .startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    assertThat(reader.getStrictness()).isEqualTo(strictness);
  }
}