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
 * Unit tests for the {@link TypeAdapter} class.
 */
public class TypeAdapterTest {

  private static final TypeAdapter<String> ASSERTION_ERROR_ADAPTER = createAssertionErrorAdapter();
  private static final TypeAdapter<String> STRING_ADAPTER = createStringAdapter();

  @Test
  public void nullSafeAdapter_ShouldHandleNullValuesCorrectly() throws IOException {
    TypeAdapter<String> nullSafeAdapter = ASSERTION_ERROR_ADAPTER.nullSafe();

    assertThat(nullSafeAdapter.toJson(null)).isEqualTo("null");
    assertThat(nullSafeAdapter.fromJson("null")).isNull();
  }

  @Test
  public void nullSafeAdapter_ShouldReturnSameInstanceWhenCalledMultipleTimes() {
    TypeAdapter<?> nullSafeAdapter = ASSERTION_ERROR_ADAPTER.nullSafe();

    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  @Test
  public void nullSafeAdapter_ShouldHaveCorrectToStringRepresentation() {
    TypeAdapter<?> adapter = ASSERTION_ERROR_ADAPTER;

    assertThat(adapter.toString()).isEqualTo("assertionErrorAdapter");
    assertThat(adapter.nullSafe().toString()).isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
    assertThat(adapter.nullSafe().nullSafe().toString()).isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
  }

  @Test
  public void toJson_ShouldThrowJsonIOExceptionWhenWriteThrowsIOException() {
    IOException testException = new IOException("test");
    TypeAdapter<Integer> adapter = createThrowingIOExceptionAdapter(testException);

    JsonIOException jsonIOException = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
    assertThat(jsonIOException).hasCauseThat().isEqualTo(testException);

    jsonIOException = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
    assertThat(jsonIOException).hasCauseThat().isEqualTo(testException);
  }

  @Test
  public void fromJson_ShouldIgnoreTrailingDataInReader() throws IOException {
    assertThat(STRING_ADAPTER.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
  }

  @Test
  public void fromJson_ShouldIgnoreTrailingDataInString() throws IOException {
    assertThat(STRING_ADAPTER.fromJson("\"a\"1")).isEqualTo("a");
  }

  private static TypeAdapter<String> createAssertionErrorAdapter() {
    return new TypeAdapter<>() {
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
  }

  private static TypeAdapter<String> createStringAdapter() {
    return new TypeAdapter<>() {
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

  private static TypeAdapter<Integer> createThrowingIOExceptionAdapter(IOException exception) {
    return new TypeAdapter<>() {
      @Override
      public void write(JsonWriter out, Integer value) throws IOException {
        throw exception;
      }

      @Override
      public Integer read(JsonReader in) {
        throw new AssertionError("not needed by this test");
      }
    };
  }
}