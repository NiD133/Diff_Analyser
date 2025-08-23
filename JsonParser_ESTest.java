package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Readable tests for JsonParser covering:
 * - Basic happy paths for string, Reader, and JsonReader inputs
 * - Error handling for nulls, malformed input, and trailing data
 * - Behavior differences between parseReader(Reader) and parseReader(JsonReader)
 * - Backward-compatible instance methods delegating to static methods
 */
public class JsonParserTest {

  // ---------------------------
  // Happy-path parsing
  // ---------------------------

  @Test
  public void parseString_validObject_returnsJsonObject() {
    JsonElement el = JsonParser.parseString("{\"key\":\"value\"}");
    assertTrue(el.isJsonObject());
    assertEquals("value", el.getAsJsonObject().get("key").getAsString());
  }

  @Test
  public void parseString_validArray_returnsJsonArray() {
    JsonElement el = JsonParser.parseString("[\"a\",\"b\"]");
    assertTrue(el.isJsonArray());
    assertEquals(2, el.getAsJsonArray().size());
  }

  @Test
  public void parseReader_reader_validObject_returnsJsonObject() {
    Reader reader = new StringReader("{\"n\":42}");
    JsonElement el = JsonParser.parseReader(reader);
    assertTrue(el.isJsonObject());
    assertEquals(42, el.getAsJsonObject().get("n").getAsInt());
  }

  @Test
  public void parseReader_jsonReader_multipleTopLevelElements_isAllowed() {
    // JsonReader variant returns the next value and allows multiple top-level elements
    JsonReader jsonReader = new JsonReader(new StringReader("1 true"));
    JsonElement first = JsonParser.parseReader(jsonReader);
    assertTrue(first.isJsonPrimitive());
    assertEquals(1, first.getAsInt());

    JsonElement second = JsonParser.parseReader(jsonReader);
    assertTrue(second.isJsonPrimitive());
    assertTrue(second.getAsBoolean());

    // Subsequent call after consuming all input should fail because the document has ended
    assertThrows(IllegalStateException.class, () -> JsonParser.parseReader(jsonReader));
  }

  // ---------------------------
  // Error handling
  // ---------------------------

  @Test
  public void parseString_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseString(null));
  }

  @Test
  public void parseReader_readerNull_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((Reader) null));
  }

  @Test
  public void parseReader_jsonReaderNull_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JsonParser.parseReader((JsonReader) null));
  }

  @Test
  public void parseReader_reader_emptyInput_throwsJsonSyntaxException() {
    Reader reader = new StringReader("");
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
  }

  @Test
  public void parseString_trailingData_throwsJsonSyntaxException() {
    // Multiple top-level values are not allowed for parseString
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseString("1 true"));
  }

  @Test
  public void parseReader_reader_trailingData_throwsJsonSyntaxException() {
    // Multiple top-level values are not allowed for parseReader(Reader)
    Reader reader = new StringReader("true false");
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(reader));
  }

  @Test
  public void parseReader_jsonReader_strictMode_rejectsLenientFeatures() {
    // Unquoted token is rejected in STRICT mode
    JsonReader strictReader = new JsonReader(new StringReader("unquoted"));
    strictReader.setStrictness(Strictness.STRICT);
    assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(strictReader));
  }

  // ---------------------------
  // Deprecated instance methods delegate to static methods
  // ---------------------------

  @Test
  public void deprecated_parse_string_delegatesToStatic() {
    String json = "[1,2,3]";
    JsonParser parser = new JsonParser(); // Deprecated constructor is still supported
    JsonElement viaInstance = parser.parse(json);
    JsonElement viaStatic = JsonParser.parseString(json);

    assertTrue(viaInstance.isJsonArray());
    assertEquals(viaStatic, viaInstance);
    assertFalse(viaInstance.getAsJsonArray().isEmpty());
  }

  @Test
  public void deprecated_parse_reader_delegatesToStatic() {
    String json = "{\"a\":true}";
    JsonParser parser = new JsonParser();
    Reader r1 = new StringReader(json);
    Reader r2 = new StringReader(json);

    JsonElement viaInstance = parser.parse(r1);
    JsonElement viaStatic = JsonParser.parseReader(r2);

    assertEquals(viaStatic, viaInstance);
    assertTrue(viaInstance.getAsJsonObject().get("a").getAsBoolean());
  }

  @Test
  public void deprecated_parse_jsonReader_delegatesToStatic() {
    String json = "false";
    JsonParser parser = new JsonParser();
    JsonReader jr1 = new JsonReader(new StringReader(json));
    JsonReader jr2 = new JsonReader(new StringReader(json));

    JsonElement viaInstance = parser.parse(jr1);
    JsonElement viaStatic = JsonParser.parseReader(jr2);

    assertEquals(viaStatic, viaInstance);
    assertFalse(viaInstance.getAsBoolean());
  }
}