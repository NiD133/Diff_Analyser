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
 * Unit tests for the {@link JsonTreeWriter} class.
 */
public final class JsonTreeWriterTest {

  @Test
  public void testSimpleArraySerialization() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    assertThat(writer.get().toString()).isEqualTo("[1,2,3]");
  }

  @Test
  public void testNestedArraySerialization() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.endArray();
    writer.endArray();
    assertThat(writer.get().toString()).isEqualTo("[[],[[]]]");
  }

  @Test
  public void testSimpleObjectSerialization() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("A").value(1);
    writer.name("B").value(2);
    writer.endObject();
    assertThat(writer.get().toString()).isEqualTo("{\"A\":1,\"B\":2}");
  }

  @Test
  public void testNestedObjectSerialization() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
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
    assertThat(writer.get().toString()).isEqualTo("{\"A\":{\"B\":{}},\"C\":{}}");
  }

  @Test
  public void testWriteAfterCloseThrowsException() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    writer.beginArray();
    writer.value("A");
    writer.endArray();
    writer.close();
    assertThrows(IllegalStateException.class, () -> writer.beginArray());
  }

  @Test
  public void testPrematureCloseThrowsException() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    writer.beginArray();
    IOException exception = assertThrows(IOException.class, () -> writer.close());
    assertThat(exception).hasMessageThat().isEqualTo("Incomplete document");
  }

  @Test
  public void testNameAsTopLevelValueThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(exception).hasMessageThat().isEqualTo("Did not expect a name");

    writer.value(12);
    writer.close();

    exception = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
  }

  @Test
  public void testNameInArrayThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    writer.value(12);
    exception = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    writer.endArray();

    assertThat(writer.get().toString()).isEqualTo("[12]");
  }

  @Test
  public void testDuplicateNameThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("a");
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("a"));
    assertThat(exception).hasMessageThat().isEqualTo("Did not expect a name");
  }

  @Test
  public void testSerializeNullsFalse() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(false);
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    assertThat(writer.get().toString()).isEqualTo("{}");
  }

  @Test
  public void testSerializeNullsTrue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(true);
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    assertThat(writer.get().toString()).isEqualTo("{\"A\":null}");
  }

  @Test
  public void testEmptyWriterReturnsJsonNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThat(writer.get()).isEqualTo(JsonNull.INSTANCE);
  }

  @Test
  public void testBeginArrayReturnsWriter() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThat(writer.beginArray()).isEqualTo(writer);
  }

  @Test
  public void testBeginObjectReturnsWriter() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThat(writer.beginObject()).isEqualTo(writer);
  }

  @Test
  public void testValueStringReturnsWriter() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    String value = "as";
    assertThat(writer.value(value)).isEqualTo(writer);
  }

  @Test
  public void testValueBooleanReturnsWriter() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    boolean value = true;
    assertThat(writer.value(value)).isEqualTo(writer);
  }

  @Test
  public void testValueBoxedBooleanReturnsWriter() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    Boolean value = true;
    assertThat(writer.value(value)).isEqualTo(writer);
  }

  @Test
  public void testLenientNansAndInfinities() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    writer.beginArray();
    writer.value(Float.NaN);
    writer.value(Float.NEGATIVE_INFINITY);
    writer.value(Float.POSITIVE_INFINITY);
    writer.value(Double.NaN);
    writer.value(Double.NEGATIVE_INFINITY);
    writer.value(Double.POSITIVE_INFINITY);
    writer.endArray();
    assertThat(writer.get().toString())
        .isEqualTo("[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]");
  }

  @Test
  public void testStrictNansAndInfinitiesThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
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
  public void testStrictBoxedNansAndInfinitiesThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
  }

  @Test
  public void testJsonValueThrowsUnsupportedOperationException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("test"));
  }

  /**
   * Tests that all relevant methods of {@link JsonWriter} are overridden in {@link JsonTreeWriter}.
   */
  @Test
  public void testOverridesAllRelevantMethods() {
    List<String> ignoredMethods = Arrays.asList(
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
        "getSerializeNulls()"
    );
    MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, ignoredMethods);
  }
}