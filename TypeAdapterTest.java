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
  // Adapter that throws AssertionError on all operations
  private static final TypeAdapter<String> ASSERTION_ERROR_ADAPTER = new AssertionErrorAdapter();

  // Simple adapter that reads/writes strings normally
  private static final TypeAdapter<String> SIMPLE_STRING_ADAPTER = new SimpleStringAdapter();

  @Test
  public void nullSafeAdapter_whenWritingNull_outputsNullValue() throws IOException {
    TypeAdapter<String> adapter = ASSERTION_ERROR_ADAPTER.nullSafe();
    assertThat(adapter.toJson(null)).isEqualTo("null");
  }

  @Test
  public void nullSafeAdapter_whenReadingNull_returnsNull() throws IOException {
    TypeAdapter<String> adapter = ASSERTION_ERROR_ADAPTER.nullSafe();
    assertThat(adapter.fromJson("null")).isNull();
  }

  @Test
  public void nullSafeAdapter_whenAppliedMultipleTimes_returnsSameInstance() {
    TypeAdapter<?> nullSafeAdapter = ASSERTION_ERROR_ADAPTER.nullSafe();

    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  @Test
  public void nullSafeAdapter_toString_showsNullSafeWrapping() {
    TypeAdapter<?> adapter = ASSERTION_ERROR_ADAPTER;

    assertThat(adapter.toString()).isEqualTo("AssertionErrorAdapter");
    assertThat(adapter.nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[AssertionErrorAdapter]");
    assertThat(adapter.nullSafe().nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[AssertionErrorAdapter]");
  }

  @Test
  public void write_whenThrowingIOException_wrapsExceptionInJsonIOException() {
    IOException exception = new IOException("test");
    TypeAdapter<Integer> adapter = new ThrowingAdapter(exception);

    JsonIOException e = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
    assertThat(e).hasCauseThat().isEqualTo(exception);

    e = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
    assertThat(e).hasCauseThat().isEqualTo(exception);
  }

  @Test
  public void fromJson_readerWithTrailingData_ignoresExtraData() throws IOException {
    // Note: Current behavior ignores trailing data which might be questionable
    String jsonWithTrailingData = "\"a\"1";
    assertThat(SIMPLE_STRING_ADAPTER.fromJson(new StringReader(jsonWithTrailingData)))
        .isEqualTo("a");
  }

  @Test
  public void fromJson_stringWithTrailingData_ignoresExtraData() throws IOException {
    // Note: Current behavior ignores trailing data which might be questionable
    String jsonWithTrailingData = "\"a\"1";
    assertThat(SIMPLE_STRING_ADAPTER.fromJson(jsonWithTrailingData)).isEqualTo("a");
  }

  // Adapter that always throws AssertionError (used to test nullSafe behavior)
  private static final class AssertionErrorAdapter extends TypeAdapter<String> {
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
      return "AssertionErrorAdapter";
    }
  }

  // Simple adapter for basic string operations
  private static final class SimpleStringAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  }

  // Adapter that throws specified IOException during write
  private static final class ThrowingAdapter extends TypeAdapter<Integer> {
    private final IOException exception;

    ThrowingAdapter(IOException exception) {
      this.exception = exception;
    }

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
      throw exception;
    }

    @Override
    public Integer read(JsonReader in) {
      throw new AssertionError("Test doesn't require reading");
    }
  }
}