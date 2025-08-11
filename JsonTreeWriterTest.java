package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.Strictness;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for JsonTreeWriter.
 *
 * Goals:
 * - Use clear, intention-revealing test names
 * - Reduce duplication with small helpers
 * - Add brief comments for non-obvious behaviors
 * - Keep the tested behavior identical to the original suite
 */
@SuppressWarnings("resource")
public final class JsonTreeWriterTest {

  // Helpers

  private static JsonTreeWriter newWriter() {
    return new JsonTreeWriter();
  }

  private static String toJson(JsonTreeWriter writer) {
    JsonElement element = writer.get();
    return element.toString();
  }

  // Arrays

  @Test
  public void array_simpleValues() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    assertThat(toJson(writer)).isEqualTo("[1,2,3]");
  }

  @Test
  public void array_nested() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.endArray();
    writer.endArray();

    assertThat(toJson(writer)).isEqualTo("[[],[[]]]");
  }

  // Objects

  @Test
  public void object_simpleProperties() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginObject();
    writer.name("A").value(1);
    writer.name("B").value(2);
    writer.endObject();

    assertThat(toJson(writer)).isEqualTo("{\"A\":1,\"B\":2}");
  }

  @Test
  public void object_nested() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginObject();
    writer.name("A");
    writer.beginObject();
    writer.name("B");
    writer.beginObject();
    writer.endObject();
    writer.endObject();
    writer.name("C");
    writer.beginObject();
    writer.endObject();
    writer.endObject();

    assertThat(toJson(writer)).isEqualTo("{\"A\":{\"B\":{}},\"C\":{}}");
  }

  // Writer lifecycle and state

  @Test
  public void writeAfterClose_disallowed() throws Exception {
    JsonTreeWriter writer = newWriter();
    writer.setStrictness(Strictness.LENIENT);

    writer.beginArray();
    writer.value("A");
    writer.endArray();
    writer.close();

    assertThrows(IllegalStateException.class, writer::beginArray);
  }

  @Test
  public void closeWhileDocumentIncomplete_throwsIOException() throws Exception {
    JsonTreeWriter writer = newWriter();
    writer.setStrictness(Strictness.LENIENT);

    writer.beginArray();

    IOException e = assertThrows(IOException.class, writer::close);
    assertThat(e).hasMessageThat().isEqualTo("Incomplete document");
  }

  // Name positioning rules

  @Test
  public void nameAsTopLevelValue_disallowed() throws IOException {
    JsonTreeWriter writer = newWriter();

    // At top-level, "name" has no meaning
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");

    // After writing a top-level value and closing, still disallowed
    writer.value(12);
    writer.close();

    e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
  }

  @Test
  public void nameInsideArray_disallowed() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginArray();

    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    writer.value(12);

    e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    writer.endArray();

    assertThat(toJson(writer)).isEqualTo("[12]");
  }

  @Test
  public void twoConsecutiveNames_disallowed() throws IOException {
    JsonTreeWriter writer = newWriter();

    writer.beginObject();
    writer.name("a");

    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("a"));
    assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
  }

  // Null handling

  @Test
  public void serializeNulls_false_skipsNulls() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.setSerializeNulls(false);

    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();

    assertThat(toJson(writer)).isEqualTo("{}");
  }

  @Test
  public void serializeNulls_true_writesNulls() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.setSerializeNulls(true);

    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();

    assertThat(toJson(writer)).isEqualTo("{\"A\":null}");
  }

  // Empty writer behavior

  @Test
  public void emptyWriter_returnsJsonNull() {
    JsonTreeWriter writer = newWriter();

    assertThat(writer.get()).isEqualTo(JsonNull.INSTANCE);
  }

  // Fluent API should return "this"

  @Test
  public void beginArray_returnsSelf() throws Exception {
    JsonTreeWriter writer = newWriter();
    assertThat(writer.beginArray()).isEqualTo(writer);
  }

  @Test
  public void beginObject_returnsSelf() throws Exception {
    JsonTreeWriter writer = newWriter();
    assertThat(writer.beginObject()).isEqualTo(writer);
  }

  @Test
  public void valueString_returnsSelf() throws Exception {
    JsonTreeWriter writer = newWriter();
    assertThat(writer.value("as")).isEqualTo(writer);
  }

  @Test
  public void valueBooleanPrimitive_returnsSelf() throws Exception {
    JsonTreeWriter writer = newWriter();
    assertThat(writer.value(true)).isEqualTo(writer);
  }

  @Test
  public void valueBooleanObject_returnsSelf() throws Exception {
    JsonTreeWriter writer = newWriter();
    assertThat(writer.value(Boolean.TRUE)).isEqualTo(writer);
  }

  // NaN/Infinity handling

  @Test
  public void lenient_allowsNansAndInfinities() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.setStrictness(Strictness.LENIENT);

    writer.beginArray();
    writer.value(Float.NaN);
    writer.value(Float.NEGATIVE_INFINITY);
    writer.value(Float.POSITIVE_INFINITY);
    writer.value(Double.NaN);
    writer.value(Double.NEGATIVE_INFINITY);
    writer.value(Double.POSITIVE_INFINITY);
    writer.endArray();

    assertThat(toJson(writer))
        .isEqualTo("[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]");
  }

  @Test
  public void strict_rejectsPrimitiveNansAndInfinities() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();

    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.POSITIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.POSITIVE_INFINITY));
  }

  @Test
  public void strict_rejectsBoxedNansAndInfinities() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();

    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
  }

  // Unsupported operations

  @Test
  public void jsonValue_isNotSupported() throws IOException {
    JsonTreeWriter writer = newWriter();
    writer.beginArray();

    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("test"));
  }

  /**
   * JsonTreeWriter replaces JsonWriter's writing logic; therefore it must override all relevant methods.
   * This test ensures the override coverage stays up to date.
   */
  @Test
  public void overrides_allRelevantJsonWriterMethods() {
    List<String> ignoredMethods =
        Arrays.asList(
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",
            "setIndent(java.lang.String)",
            "setHtmlSafe(boolean)",
            "isHtmlSafe()",
            "setFormattingStyle(com.google.gson.FormattingStyle)",
            "getFormattingStyle()",
            "setSerializeNulls(boolean)",
            "getSerializeNulls()");
    MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, ignoredMethods);
  }
}