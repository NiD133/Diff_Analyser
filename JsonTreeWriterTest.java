/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.Strictness;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}, which writes JSON to a {@link JsonElement} tree.
 */
@SuppressWarnings("resource")
public final class JsonTreeWriterTest {

  @Test
  public void writeArray_producesCorrectJson() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    // Assert
    assertThat(writer.get().toString()).isEqualTo("[1,2,3]");
  }

  @Test
  public void writeNestedArray_producesCorrectJson() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.endArray();
    writer.endArray();

    // Assert
    assertThat(writer.get().toString()).isEqualTo("[[],[[]]]");
  }

  @Test
  public void writeObject_producesCorrectJson() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act
    writer.beginObject();
    writer.name("A").value(1);
    writer.name("B").value(2);
    writer.endObject();

    // Assert
    assertThat(writer.get().toString()).isEqualTo("{\"A\":1,\"B\":2}");
  }

  @Test
  public void writeNestedObject_producesCorrectJson() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act
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

    // Assert
    assertThat(writer.get().toString()).isEqualTo("{\"A\":{\"B\":{}},\"C\":{}}");
  }

  @Test
  public void beginArray_afterClose_throwsIllegalStateException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.close();

    // Act & Assert
    assertThrows(IllegalStateException.class, writer::beginArray);
  }

  @Test
  public void close_onUnclosedDocument_throwsIOException() {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    // Act & Assert
    IOException e = assertThrows(IOException.class, writer::close);
    assertThat(e).hasMessageThat().isEqualTo("Incomplete document");
  }

  @Test
  public void name_atTopLevel_throwsIllegalStateException() {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act & Assert
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
  }

  @Test
  public void name_afterDocumentClosed_throwsIllegalStateException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(12);
    writer.close();

    // Act & Assert
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
  }

  @Test
  public void name_insideArray_throwsIllegalStateException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    // Act & Assert
    IllegalStateException e1 =
        assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e1).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    // Act & Assert after writing a value
    writer.value(12);
    IllegalStateException e2 =
        assertThrows(IllegalStateException.class, () -> writer.name("hello"));
    assertThat(e2).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
  }

  @Test
  public void name_calledTwiceForObjectProperty_throwsIllegalStateException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("a");

    // Act & Assert
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("a"));
    assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
  }

  @Test
  public void writeNullProperty_whenSerializeNullsIsFalse_isOmitted() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(false);

    // Act
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();

    // Assert
    assertThat(writer.get().toString()).isEqualTo("{}");
  }

  @Test
  public void writeNullProperty_whenSerializeNullsIsTrue_isIncluded() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(true);

    // Act
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();

    // Assert
    assertThat(writer.get().toString()).isEqualTo("{\"A\":null}");
  }

  @Test
  public void get_onNewWriter_returnsJsonNull() {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();

    // Act & Assert
    assertThat(writer.get()).isEqualTo(JsonNull.INSTANCE);
  }

  @Test
  public void writeNonFiniteNumbers_inLenientMode_succeeds() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);

    // Act
    writer.beginArray();
    writer.value(Float.NaN);
    writer.value(Float.NEGATIVE_INFINITY);
    writer.value(Float.POSITIVE_INFINITY);
    writer.value(Double.NaN);
    writer.value(Double.NEGATIVE_INFINITY);
    writer.value(Double.POSITIVE_INFINITY);
    writer.endArray();

    // Assert
    assertThat(writer.get().toString())
        .isEqualTo("[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]");
  }

  @Test
  public void write_nonFinitePrimitiveNumbersInStrictMode_throwsIllegalArgumentException()
      throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.POSITIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.POSITIVE_INFINITY));
  }

  @Test
  public void write_nonFiniteBoxedNumbersInStrictMode_throwsIllegalArgumentException()
      throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
    assertThrows(
        IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertThrows(
        IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
    assertThrows(
        IllegalArgumentException.class,
        () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertThrows(
        IllegalArgumentException.class,
        () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
  }

  @Test
  public void jsonValue_isUnsupported() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    // Act & Assert
    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("test"));
  }

  /**
   * {@link JsonTreeWriter} effectively replaces the complete writing logic of {@link JsonWriter} to
   * create a {@link JsonElement} tree instead of writing to a {@link Writer}. Therefore all
   * relevant methods of {@code JsonWriter} must be overridden. This test verifies that this is the
   * case.
   */
  @Test
  public void methods_fromSuperclass_areCorrectlyOverridden() {
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