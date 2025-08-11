package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for JsonTreeReader, focused on readability and intent.
 *
 * Structure:
 * - skipValue behavior
 * - iteration/hasNext behavior
 * - error handling
 * - configuration behavior (nesting limit)
 * - API contract (overrides)
 */
@SuppressWarnings("resource")
public class JsonTreeReaderTest {

  private static final String ROOT_PATH = "$";
  private static final String SKIPPED_NAME_PATH = "$.<skipped>";

  // ---------- Helpers ----------

  private static JsonTreeReader reader(JsonElement element) {
    return new JsonTreeReader(element);
  }

  private static void assertAtEndOfDocument(JsonTreeReader r) throws IOException {
    assertThat(r.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(r.getPath()).isEqualTo(ROOT_PATH);
  }

  private static JsonObject populatedObject() {
    JsonObject obj = new JsonObject();

    JsonArray array = new JsonArray();
    array.add('c');
    array.add("text");
    obj.add("a", array);

    obj.addProperty("b", true);
    obj.addProperty("i", 1);
    obj.add("n", JsonNull.INSTANCE);

    JsonObject nested = new JsonObject();
    nested.addProperty("n", 2L);
    obj.add("o", nested);

    obj.addProperty("s", "text");
    return obj;
  }

  private static JsonArray nestedEmptyArrays(int depth) {
    JsonArray root = new JsonArray();
    JsonArray current = root;
    for (int i = 0; i < depth; i++) {
      JsonArray next = new JsonArray();
      current.add(next);
      current = next;
    }
    return root;
  }

  // ---------- skipValue behavior ----------

  @Test
  public void skipValue_onEmptyObject_consumesDocument() throws IOException {
    JsonTreeReader r = reader(new JsonObject());

    r.skipValue();

    assertAtEndOfDocument(r);
  }

  @Test
  public void skipValue_onPopulatedObject_consumesDocument() throws IOException {
    JsonTreeReader r = reader(populatedObject());

    r.skipValue();

    assertAtEndOfDocument(r);
  }

  @Test
  public void skipValue_onName_allowsReadingFollowingValue() throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("a", "value");
    JsonTreeReader r = reader(obj);

    r.beginObject();
    r.skipValue(); // skips the name "a"

    assertThat(r.peek()).isEqualTo(JsonToken.STRING);
    assertThat(r.getPath()).isEqualTo(SKIPPED_NAME_PATH);
    assertThat(r.nextString()).isEqualTo("value");
  }

  @Test
  public void skipValue_afterEndOfDocument_isNoOp() throws IOException {
    JsonTreeReader r = reader(new JsonObject());

    r.beginObject();
    r.endObject();
    assertThat(r.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(r.getPath()).isEqualTo(ROOT_PATH);

    r.skipValue(); // no-op after document end

    assertThat(r.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(r.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void skipValue_whenAtArrayEnd_advancesToEndDocument() throws IOException {
    JsonTreeReader r = reader(new JsonArray());

    r.beginArray(); // empty array => next token is END_ARRAY
    r.skipValue();  // skip the END_ARRAY

    assertAtEndOfDocument(r);
  }

  @Test
  public void skipValue_whenAtObjectEnd_advancesToEndDocument() throws IOException {
    JsonTreeReader r = reader(new JsonObject());

    r.beginObject(); // empty object => next token is END_OBJECT
    r.skipValue();   // skip the END_OBJECT

    assertAtEndOfDocument(r);
  }

  // ---------- Iteration behavior ----------

  @Test
  public void hasNext_afterEmptyObject_returnsFalse() throws IOException {
    JsonTreeReader r = reader(new JsonObject());

    r.beginObject();
    r.endObject();

    assertThat(r.hasNext()).isFalse();
  }

  // ---------- Error handling ----------

  @Test
  public void customJsonElementSubclass_throwsMeaningfulError() throws IOException {
    @SuppressWarnings("deprecation") // superclass constructor
    class CustomSubclass extends JsonElement {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    }

    JsonArray array = new JsonArray();
    array.add(new CustomSubclass());

    JsonTreeReader r = reader(array);
    r.beginArray();

    MalformedJsonException e = assertThrows(MalformedJsonException.class, r::peek);
    assertThat(e)
        .hasMessageThat()
        .isEqualTo("Custom JsonElement subclass " + CustomSubclass.class.getName() + " is not supported");
  }

  // ---------- Configuration behavior ----------

  /**
   * JsonTreeReader ignores the configured nesting limit because it reads from a
   * pre-constructed tree and not from a streaming source.
   */
  @Test
  public void nestingLimit_isIgnoredForTreeReader() throws IOException {
    int limit = 10;

    JsonArray json = nestedEmptyArrays(limit); // total arrays = limit + 1
    JsonTreeReader r = reader(json);
    r.setNestingLimit(limit);
    assertThat(r.getNestingLimit()).isEqualTo(limit);

    // Can descend one more level than the limit without error.
    for (int i = 0; i < limit; i++) {
      r.beginArray();
    }
    r.beginArray(); // does not throw

    r.endArray();
    for (int i = 0; i < limit; i++) {
      r.endArray();
    }

    assertThat(r.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    r.close();
  }

  // ---------- API contract ----------

  /**
   * JsonTreeReader replaces the core reading logic of JsonReader; therefore it must override
   * all relevant methods.
   */
  @Test
  public void overrides_allReaderMethodsThatMatter() {
    List<String> ignoredMethods =
        Arrays.asList(
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",
            "setNestingLimit(int)",
            "getNestingLimit()");
    MoreAsserts.assertOverridesMethods(JsonReader.class, JsonTreeReader.class, ignoredMethods);
  }
}