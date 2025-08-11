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
 * Tests for the core functionality of TypeAdapter, including null safety,
 * error handling, and JSON parsing behavior.
 */
public class TypeAdapterTest {

  // Test adapters used across multiple test methods
  
  /**
   * A test adapter that throws AssertionError for any read/write operation.
   * Used to verify that nullSafe() wrapper handles null values without
   * delegating to the underlying adapter.
   */
  private static final TypeAdapter<String> THROWS_ON_ANY_OPERATION_ADAPTER =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, String value) {
          throw new AssertionError("unexpected call - nullSafe should handle nulls");
        }

        @Override
        public String read(JsonReader in) {
          throw new AssertionError("unexpected call - nullSafe should handle nulls");
        }

        @Override
        public String toString() {
          return "assertionErrorAdapter";
        }
      };

  /**
   * A simple string adapter that reads/writes string values normally.
   * Used for testing basic JSON parsing behavior.
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

  // Tests for nullSafe() functionality

  @Test
  public void nullSafe_handlesNullValuesCorrectly() throws IOException {
    TypeAdapter<String> nullSafeAdapter = THROWS_ON_ANY_OPERATION_ADAPTER.nullSafe();

    // Verify that null values are handled by the wrapper without calling the underlying adapter
    assertThat(nullSafeAdapter.toJson(null)).isEqualTo("null");
    assertThat(nullSafeAdapter.fromJson("null")).isNull();
  }

  @Test
  public void nullSafe_returnsTheSameInstanceWhenCalledMultipleTimes() {
    TypeAdapter<?> nullSafeAdapter = THROWS_ON_ANY_OPERATION_ADAPTER.nullSafe();

    // Verify that calling nullSafe() on an already null-safe adapter returns the same instance
    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  @Test
  public void nullSafe_toStringShowsWrappedAdapterName() {
    TypeAdapter<?> originalAdapter = THROWS_ON_ANY_OPERATION_ADAPTER;
    TypeAdapter<?> nullSafeAdapter = originalAdapter.nullSafe();

    assertThat(originalAdapter.toString()).isEqualTo("assertionErrorAdapter");
    assertThat(nullSafeAdapter.toString())
        .isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
    
    // Verify that multiple nullSafe() calls don't create nested wrappers in toString()
    assertThat(nullSafeAdapter.nullSafe().toString())
        .isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
  }

  // Tests for error handling

  /**
   * Verifies that IOExceptions thrown by TypeAdapter.write() are properly wrapped
   * in JsonIOException for both toJson() and toJsonTree() methods.
   */
  @Test
  public void toJson_wrapsIOExceptionInJsonIOException() {
    IOException originalException = new IOException("simulated write error");
    TypeAdapter<Integer> adapterThatThrowsIOException = createAdapterThatThrowsIOException(originalException);

    // Test toJson() method
    JsonIOException thrownByToJson = assertThrows(
        JsonIOException.class, 
        () -> adapterThatThrowsIOException.toJson(42)
    );
    assertThat(thrownByToJson).hasCauseThat().isEqualTo(originalException);

    // Test toJsonTree() method
    JsonIOException thrownByToJsonTree = assertThrows(
        JsonIOException.class, 
        () -> adapterThatThrowsIOException.toJsonTree(42)
    );
    assertThat(thrownByToJsonTree).hasCauseThat().isEqualTo(originalException);
  }

  private TypeAdapter<Integer> createAdapterThatThrowsIOException(IOException exceptionToThrow) {
    return new TypeAdapter<>() {
      @Override
      public void write(JsonWriter out, Integer value) throws IOException {
        throw exceptionToThrow;
      }

      @Override
      public Integer read(JsonReader in) {
        throw new AssertionError("read() not needed for this test");
      }
    };
  }

  // Tests for JSON parsing behavior with trailing data

  /**
   * Verifies current behavior where fromJson() ignores trailing data after valid JSON.
   * Note: This test documents existing behavior that may be questionable and could
   * change in future versions.
   */
  @Test
  public void fromJson_withReader_ignoresTrailingData() throws IOException {
    StringReader readerWithTrailingData = new StringReader("\"validString\"extraTrailingData");
    
    String result = STRING_ADAPTER.fromJson(readerWithTrailingData);
    
    assertThat(result).isEqualTo("validString");
  }

  /**
   * Verifies current behavior where fromJson() ignores trailing data after valid JSON.
   * Note: This test documents existing behavior that may be questionable and could
   * change in future versions.
   */
  @Test
  public void fromJson_withString_ignoresTrailingData() throws IOException {
    String jsonWithTrailingData = "\"validString\"extraTrailingData";
    
    String result = STRING_ADAPTER.fromJson(jsonWithTrailingData);
    
    assertThat(result).isEqualTo("validString");
  }
}