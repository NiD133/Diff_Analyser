/*
 * Copyright (C) 2022 Google Inc.
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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TypeAdapterTest {

  // A test-only TypeAdapter that throws AssertionError when its read/write methods are called.
  // Useful for verifying that the adapter is NOT called under certain conditions.
  private static final TypeAdapter<String> assertionErrorAdapter =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
          throw new AssertionError("unexpected call");
        }

        @Override
        public String read(JsonReader in) {
          throw new AssertionError("unexpected call");
        }

        @Override
        public String toString() {
          return "assertionErrorAdapter";
        }
      };

  private static final TypeAdapter<String> stringAdapter =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
          out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
          return in.nextString();
        }
      };

  @Test
  public void nullSafe_handlesNullValues() throws IOException {
    TypeAdapter<String> adapter = assertionErrorAdapter.nullSafe();

    // The nullSafe() wrapper should handle nulls directly, without calling the delegate adapter.
    assertThat(adapter.toJson(null)).isEqualTo("null");
    assertThat(adapter.fromJson("null")).isNull();
  }

  @Test
  public void nullSafe_onAlreadyNullSafeAdapter_returnsSameInstance() {
    TypeAdapter<?> nullSafeAdapter = assertionErrorAdapter.nullSafe();

    // Calling nullSafe() on an adapter that is already null-safe should be idempotent.
    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  @Test
  public void nullSafe_toString_describesWrappedAdapter() {
    TypeAdapter<?> adapter = assertionErrorAdapter;

    assertThat(adapter.toString()).isEqualTo("assertionErrorAdapter");

    String nullSafeToString = "NullSafeTypeAdapter[assertionErrorAdapter]";
    assertThat(adapter.nullSafe().toString()).isEqualTo(nullSafeToString);
    // The toString() representation should not change with multiple nullSafe() calls.
    assertThat(adapter.nullSafe().nullSafe().toString()).isEqualTo(nullSafeToString);
  }

  /**
   * Tests that when a {@link TypeAdapter#write(JsonWriter, Object)} implementation throws an {@link
   * IOException}, the convenience methods {@link TypeAdapter#toJson(Object)} and {@link
   * TypeAdapter#toJsonTree(Object)} wrap it in a {@link JsonIOException}.
   */
  @Test
  public void toJson_whenAdapterThrowsIoException_wrapsInJsonIoException() {
    IOException cause = new IOException("test");
    TypeAdapter<Integer> adapter =
        new TypeAdapter<>() {
          @Override
          public void write(JsonWriter out, Integer value) throws IOException {
            throw cause;
          }

          @Override
          public Integer read(JsonReader in) {
            throw new AssertionError("not needed by this test");
          }
        };

    JsonIOException e1 = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
    assertThat(e1).hasCauseThat().isSameInstanceAs(cause);

    JsonIOException e2 = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
    assertThat(e2).hasCauseThat().isSameInstanceAs(cause);
  }

  /**
   * Verifies the current behavior of {@link TypeAdapter#fromJson(java.io.Reader)} which ignores
   * trailing data after a valid JSON value.
   *
   * <p>Note: This behavior is questionable and might change in a future version. This test exists
   * to document and lock in the current implementation's behavior.
   */
  @Test
  public void fromJsonReader_withTrailingData_ignoresIt() throws IOException {
    assertThat(stringAdapter.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
  }

  /**
   * Verifies the current behavior of {@link TypeAdapter#fromJson(String)} which ignores trailing
   * data after a valid JSON value.
   *
   * <p>Note: This behavior is questionable and might change in a future version. This test exists
   * to document and lock in the current implementation's behavior.
   */
  @Test
  public void fromJsonString_withTrailingData_ignoresIt() throws IOException {
    assertThat(stringAdapter.fromJson("\"a\"1")).isEqualTo("a");
  }
}