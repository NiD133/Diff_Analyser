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

  /**
   * A TypeAdapter that always throws an AssertionError in both read and write methods. This is used
   * to test the behavior of nullSafe().
   */
  private static final TypeAdapter<String> ASSERTION_ERROR_ADAPTER =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
          throw new AssertionError("Unexpected call to write");
        }

        @Override
        public String read(JsonReader in) {
          throw new AssertionError("Unexpected call to read");
        }

        @Override
        public String toString() {
          return "assertionErrorAdapter";
        }
      };

  @Test
  public void testNullSafe() throws IOException {
    TypeAdapter<String> nullSafeAdapter = ASSERTION_ERROR_ADAPTER.nullSafe();

    // nullSafe adapter should write "null" for null input
    assertThat(nullSafeAdapter.toJson(null)).isEqualTo("null");

    // nullSafe adapter should return null when reading "null"
    assertThat(nullSafeAdapter.fromJson("null")).isNull();
  }

  @Test
  public void testNullSafe_ReturningSameInstanceOnceNullSafe() {
    TypeAdapter<?> nullSafeAdapter = ASSERTION_ERROR_ADAPTER.nullSafe();

    // Calling nullSafe() on an adapter that is already null-safe should return the same instance
    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  @Test
  public void testNullSafe_ToString() {
    TypeAdapter<?> adapter = ASSERTION_ERROR_ADAPTER;

    assertThat(adapter.toString()).isEqualTo("assertionErrorAdapter");
    assertThat(adapter.nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
    assertThat(adapter.nullSafe().nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
  }

  /**
   * Tests behavior when {@link TypeAdapter#write(JsonWriter, Object)} manually throws {@link
   * IOException} which is not caused by writer usage.  This verifies that Gson wraps the IOException
   * in a JsonIOException.
   */
  @Test
  public void testToJson_ThrowingIOException() {
    IOException thrownException = new IOException("test");
    TypeAdapter<Integer> adapter =
        new TypeAdapter<>() {
          @Override
          public void write(JsonWriter out, Integer value) throws IOException {
            throw thrownException;
          }

          @Override
          public Integer read(JsonReader in) {
            throw new AssertionError("not needed by this test");
          }
        };

    // When toJson(Object) throws an IOException, it should be wrapped in a JsonIOException
    JsonIOException e = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
    assertThat(e).hasCauseThat().isEqualTo(thrownException);

    // When toJsonTree(Object) throws an IOException, it should be wrapped in a JsonIOException
    e = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
    assertThat(e).hasCauseThat().isEqualTo(thrownException);
  }

  /** A TypeAdapter that simply writes and reads strings. */
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

  /**
   * Note: This test just verifies the current behavior; it is a bit questionable whether that
   * behavior is actually desired. It tests that fromJson() will parse a JSON string with trailing
   * characters, only consuming the valid JSON part.
   */
  @Test
  public void testFromJson_Reader_TrailingData() throws IOException {
    // The adapter should parse the "a" from "\"a\"1" and ignore the trailing "1"
    assertThat(STRING_ADAPTER.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
  }

  /**
   * Note: This test just verifies the current behavior; it is a bit questionable whether that
   * behavior is actually desired. It tests that fromJson() will parse a JSON string with trailing
   * characters, only consuming the valid JSON part.
   */
  @Test
  public void testFromJson_String_TrailingData() throws IOException {
    // The adapter should parse the "a" from "\"a\"1" and ignore the trailing "1"
    assertThat(STRING_ADAPTER.fromJson("\"a\"1")).isEqualTo("a");
  }
}