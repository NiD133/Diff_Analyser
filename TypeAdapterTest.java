package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TypeAdapterTest {

  // Test to ensure nullSafe() method handles null values correctly
  @Test
  public void testNullSafeHandlesNullValues() throws IOException {
    TypeAdapter<String> nullSafeAdapter = assertionErrorAdapter.nullSafe();

    assertThat(nullSafeAdapter.toJson(null)).isEqualTo("null");
    assertThat(nullSafeAdapter.fromJson("null")).isNull();
  }

  // Test to ensure nullSafe() returns the same instance when called multiple times
  @Test
  public void testNullSafeReturnsSameInstance() {
    TypeAdapter<?> nullSafeAdapter = assertionErrorAdapter.nullSafe();

    assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
  }

  // Test to verify the toString() method of nullSafe() returns the expected string
  @Test
  public void testNullSafeToString() {
    TypeAdapter<?> adapter = assertionErrorAdapter;

    assertThat(adapter.toString()).isEqualTo("assertionErrorAdapter");
    assertThat(adapter.nullSafe().toString()).isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
    assertThat(adapter.nullSafe().nullSafe().toString()).isEqualTo("NullSafeTypeAdapter[assertionErrorAdapter]");
  }

  // Custom TypeAdapter that throws AssertionError for unexpected calls
  private static final TypeAdapter<String> assertionErrorAdapter = new TypeAdapter<>() {
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

  // Test to verify behavior when write() method throws IOException
  @Test
  public void testToJsonThrowsIOException() {
    IOException testException = new IOException("test");
    TypeAdapter<Integer> adapter = new TypeAdapter<>() {
      @Override
      public void write(JsonWriter out, Integer value) throws IOException {
        throw testException;
      }

      @Override
      public Integer read(JsonReader in) {
        throw new AssertionError("not needed by this test");
      }
    };

    JsonIOException jsonIOException = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
    assertThat(jsonIOException).hasCauseThat().isEqualTo(testException);

    jsonIOException = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
    assertThat(jsonIOException).hasCauseThat().isEqualTo(testException);
  }

  // A simple TypeAdapter for String that writes and reads JSON values
  private static final TypeAdapter<String> simpleStringAdapter = new TypeAdapter<>() {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
      return in.nextString();
    }
  };

  // Test to verify behavior when JSON has trailing data (using Reader)
  @Test
  public void testFromJsonReaderWithTrailingData() throws IOException {
    assertThat(simpleStringAdapter.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
  }

  // Test to verify behavior when JSON has trailing data (using String)
  @Test
  public void testFromJsonStringWithTrailingData() throws IOException {
    assertThat(simpleStringAdapter.fromJson("\"a\"1")).isEqualTo("a");
  }
}