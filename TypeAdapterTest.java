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

/**
 * Tests for TypeAdapter convenience behavior:
 * - nullSafe() wrapper behavior and idempotence
 * - toString() delegation through nullSafe wrapper
 * - Wrapping of IOExceptions into JsonIOException for convenience APIs
 * - Current behavior for trailing data in fromJson methods
 */
public class TypeAdapterTest {

  //
  // nullSafe() behavior
  //

  @Test
  public void nullSafe_handlesNullsOnReadAndWrite_withoutDelegating() throws IOException {
    // Given a TypeAdapter that would throw if its read/write were ever called
    TypeAdapter<String> adapter = THROWING_ADAPTER.nullSafe();

    // When/Then: null is written and read without delegating to the throwing adapter
    assertThat(adapter.toJson(null)).isEqualTo("null");
    assertThat(adapter.fromJson("null")).isNull();
  }

  @Test
  public void nullSafe_isIdempotent_returnsSameInstance() {
    TypeAdapter<?> wrapped = THROWING_ADAPTER.nullSafe();

    // Calling nullSafe() again on a null-safe adapter should return the same instance
    assertThat(wrapped.nullSafe()).isSameInstanceAs(wrapped);
    assertThat(wrapped.nullSafe().nullSafe()).isSameInstanceAs(wrapped);
    assertThat(wrapped.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(wrapped);
  }

  @Test
  public void nullSafe_toString_includesDelegateToString_once() {
    TypeAdapter<?> delegate = THROWING_ADAPTER;

    // Delegate toString is used as-is
    assertThat(delegate.toString()).isEqualTo("ThrowingAdapter");

    // nullSafe wrapper toString mentions the delegate once
    assertThat(delegate.nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[ThrowingAdapter]");
    // Wrapping again does not nest the description
    assertThat(delegate.nullSafe().nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[ThrowingAdapter]");
  }

  //
  // IOException wrapping
  //

  /**
   * Verifies that IOExceptions thrown by write(JsonWriter, T) are wrapped into JsonIOException
   * by the convenience methods toJson(T) and toJsonTree(T).
   */
  @Test
  public void toJson_and_toJsonTree_wrapIOException_fromWrite() {
    IOException cause = new IOException("test");
    TypeAdapter<Integer> throwingWrite =
        new TypeAdapter<>() {
          @Override
          public void write(JsonWriter out, Integer value) throws IOException {
            // Intentionally thrown by adapter code; not by JsonWriter
            throw cause;
          }

          @Override
          public Integer read(JsonReader in) {
            throw new AssertionError("read not used in this test");
          }
        };

    JsonIOException e = assertThrows(JsonIOException.class, () -> throwingWrite.toJson(1));
    assertThat(e).hasCauseThat().isSameInstanceAs(cause);

    e = assertThrows(JsonIOException.class, () -> throwingWrite.toJsonTree(1));
    assertThat(e).hasCauseThat().isSameInstanceAs(cause);
  }

  //
  // Trailing data behavior
  //

  // Note: These tests document current behavior described on TypeAdapter#fromJson(...):
  // No exception is thrown for multiple top-level elements or trailing data.
  @Test
  public void fromJson_reader_allowsTrailingData_returnsFirstValue() throws IOException {
    assertThat(STRING_ADAPTER.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
  }

  @Test
  public void fromJson_string_allowsTrailingData_returnsFirstValue() throws IOException {
    assertThat(STRING_ADAPTER.fromJson("\"a\"1")).isEqualTo("a");
  }

  // -------------------------------------------------------------------------
  // Test fixtures
  // -------------------------------------------------------------------------

  /**
   * Adapter used to assert that nullSafe() does not delegate for nulls.
   * Any accidental delegation will fail the test with an AssertionError.
   */
  private static final TypeAdapter<String> THROWING_ADAPTER =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
          throw new AssertionError("write should not be called");
        }

        @Override
        public String read(JsonReader in) {
          throw new AssertionError("read should not be called");
        }

        @Override
        public String toString() {
          return "ThrowingAdapter";
        }
      };

  /**
   * Minimal string passthrough adapter used to exercise fromJson behavior.
   */
  private static final TypeAdapter<String> STRING_ADAPTER =
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
}