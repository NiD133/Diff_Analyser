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
 * Tests for JsonTreeWriter, which creates JsonElement trees instead of writing JSON to a stream.
 */
@SuppressWarnings("resource")
public final class JsonTreeWriterTest {

  // ========== Array Writing Tests ==========

  @Test
  public void shouldCreateSimpleArrayWithNumericValues() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    
    // Then
    String actualJson = writer.get().toString();
    assertThat(actualJson).isEqualTo("[1,2,3]");
  }

  @Test
  public void shouldCreateNestedArrayStructure() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When - Create structure: [[],[[]]]
    writer.beginArray();
      writer.beginArray(); // First empty array
      writer.endArray();
      writer.beginArray(); // Second array containing empty array
        writer.beginArray(); // Nested empty array
        writer.endArray();
      writer.endArray();
    writer.endArray();
    
    // Then
    String actualJson = writer.get().toString();
    assertThat(actualJson).isEqualTo("[[],[[]]]");
  }

  // ========== Object Writing Tests ==========

  @Test
  public void shouldCreateSimpleObjectWithProperties() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When
    writer.beginObject();
    writer.name("A").value(1);
    writer.name("B").value(2);
    writer.endObject();
    
    // Then
    String actualJson = writer.get().toString();
    assertThat(actualJson).isEqualTo("{\"A\":1,\"B\":2}");
  }

  @Test
  public void shouldCreateNestedObjectStructure() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When - Create structure: {"A":{"B":{}},"C":{}}
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
    
    // Then
    String actualJson = writer.get().toString();
    assertThat(actualJson).isEqualTo("{\"A\":{\"B\":{}},\"C\":{}}");
  }

  // ========== Writer State Management Tests ==========

  @Test
  public void shouldThrowExceptionWhenWritingAfterClose() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    
    // When - Complete a valid JSON structure and close
    writer.beginArray();
    writer.value("A");
    writer.endArray();
    writer.close();
    
    // Then - Further operations should fail
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.beginArray()
    );
    // Note: Exception message verification could be added here if needed
  }

  @Test
  public void shouldThrowExceptionWhenClosingIncompleteDocument() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    writer.beginArray(); // Start array but don't close it
    
    // When/Then - Closing incomplete document should fail
    IOException exception = assertThrows(IOException.class, () -> writer.close());
    assertThat(exception).hasMessageThat().isEqualTo("Incomplete document");
  }

  // ========== Error Handling Tests ==========

  @Test
  public void shouldRejectNameAtTopLevel() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When/Then - Name without object context should fail
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.name("hello")
    );
    assertThat(exception).hasMessageThat().isEqualTo("Did not expect a name");

    // When - Complete a valid document
    writer.value(12);
    writer.close();

    // Then - Name after completion should also fail
    exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.name("hello")
    );
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
  }

  @Test
  public void shouldRejectNameInsideArray() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    // When/Then - Name inside array should fail
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.name("hello")
    );
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    // When - Add value and try name again
    writer.value(12);
    exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.name("hello")
    );
    assertThat(exception).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

    // When - Complete the array
    writer.endArray();

    // Then - Should produce valid JSON
    assertThat(writer.get().toString()).isEqualTo("[12]");
  }

  @Test
  public void shouldRejectConsecutiveNamesInObject() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("a");
    
    // When/Then - Second name without value should fail
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, 
        () -> writer.name("a")
    );
    assertThat(exception).hasMessageThat().isEqualTo("Did not expect a name");
  }

  // ========== Null Serialization Tests ==========

  @Test
  public void shouldOmitNullValuesWhenSerializeNullsIsFalse() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(false);
    
    // When
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    
    // Then - Null property should be omitted
    assertThat(writer.get().toString()).isEqualTo("{}");
  }

  @Test
  public void shouldIncludeNullValuesWhenSerializeNullsIsTrue() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setSerializeNulls(true);
    
    // When
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    
    // Then - Null property should be included
    assertThat(writer.get().toString()).isEqualTo("{\"A\":null}");
  }

  // ========== Empty Writer Tests ==========

  @Test
  public void shouldReturnJsonNullForEmptyWriter() {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When/Then - Empty writer should return JsonNull
    assertThat(writer.get()).isEqualTo(JsonNull.INSTANCE);
  }

  // ========== Fluent Interface Tests ==========

  @Test
  public void beginArrayShouldReturnWriterForChaining() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When/Then - Should return same writer instance for method chaining
    assertThat(writer.beginArray()).isEqualTo(writer);
  }

  @Test
  public void beginObjectShouldReturnWriterForChaining() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    
    // When/Then - Should return same writer instance for method chaining
    assertThat(writer.beginObject()).isEqualTo(writer);
  }

  @Test
  public void stringValueShouldReturnWriterForChaining() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    String testValue = "test_string";
    
    // When/Then - Should return same writer instance for method chaining
    assertThat(writer.value(testValue)).isEqualTo(writer);
  }

  @Test
  public void primitiveBooleanValueShouldReturnWriterForChaining() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    boolean testValue = true;
    
    // When/Then - Should return same writer instance for method chaining
    assertThat(writer.value(testValue)).isEqualTo(writer);
  }

  @Test
  public void boxedBooleanValueShouldReturnWriterForChaining() throws Exception {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    Boolean testValue = Boolean.TRUE;
    
    // When/Then - Should return same writer instance for method chaining
    assertThat(writer.value(testValue)).isEqualTo(writer);
  }

  // ========== Special Number Handling Tests ==========

  @Test
  public void shouldAllowNaNAndInfinityInLenientMode() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LENIENT);
    
    // When
    writer.beginArray();
    writer.value(Float.NaN);
    writer.value(Float.NEGATIVE_INFINITY);
    writer.value(Float.POSITIVE_INFINITY);
    writer.value(Double.NaN);
    writer.value(Double.NEGATIVE_INFINITY);
    writer.value(Double.POSITIVE_INFINITY);
    writer.endArray();
    
    // Then
    String expectedJson = "[NaN,-Infinity,Infinity,NaN,-Infinity,Infinity]";
    assertThat(writer.get().toString()).isEqualTo(expectedJson);
  }

  @Test
  public void shouldRejectNaNAndInfinityInStrictMode() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();
    
    // When/Then - All special float/double values should be rejected
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.POSITIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.POSITIVE_INFINITY));
  }

  @Test
  public void shouldRejectBoxedNaNAndInfinityInStrictMode() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    writer.beginArray();
    
    // When/Then - All boxed special float/double values should be rejected
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NaN)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertThrows(IllegalArgumentException.class, () -> writer.value(Double.valueOf(Double.POSITIVE_INFINITY)));
  }

  // ========== Unsupported Operations Tests ==========

  @Test
  public void jsonValueShouldThrowUnsupportedOperationException() throws IOException {
    // Given
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    
    // When/Then - jsonValue is not supported for tree writing
    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("test"));
  }

  // ========== Implementation Completeness Tests ==========

  /**
   * Verifies that JsonTreeWriter properly overrides all relevant methods from JsonWriter.
   * Since JsonTreeWriter creates JsonElement trees instead of writing to a stream,
   * it must override the complete writing logic of JsonWriter.
   */
  @Test
  public void shouldOverrideAllRelevantJsonWriterMethods() {
    // Given - Methods that don't need to be overridden (configuration/state methods)
    List<String> methodsNotRequiringOverride = Arrays.asList(
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
    
    // When/Then - All other methods should be overridden
    MoreAsserts.assertOverridesMethods(JsonWriter.class, JsonTreeWriter.class, methodsNotRequiringOverride);
  }
}