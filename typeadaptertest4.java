package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for exception handling in {@link TypeAdapter} convenience methods.
 */
public class TypeAdapterWriteExceptionTest {

  // A specific exception instance to be thrown and then verified in assertions.
  private static final IOException CAUSE_EXCEPTION = new IOException("test exception");

  /**
   * A test-only TypeAdapter that unconditionally throws an {@link IOException} during serialization.
   * The read method is designed to fail loudly if it is ever called.
   */
  private static final TypeAdapter<Integer> THROWING_ADAPTER =
      new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, Integer value) throws IOException {
          throw CAUSE_EXCEPTION;
        }

        @Override
        public Integer read(JsonReader in) {
          throw new AssertionError("read should not be called by this test");
        }
      };

  /**
   * Verifies that {@link TypeAdapter#toJson(Object)} wraps an {@link IOException} from the underlying
   * {@link TypeAdapter#write(JsonWriter, Object)} method in a {@link JsonIOException}.
   */
  @Test
  public void toJson_whenWriteThrowsIOException_wrapsInJsonIOException() {
    // The value '1' is arbitrary and does not affect the test outcome
    JsonIOException actualException =
        assertThrows(JsonIOException.class, () -> THROWING_ADAPTER.toJson(1));

    assertThat(actualException).hasCauseThat().isSameInstanceAs(CAUSE_EXCEPTION);
  }

  /**
   * Verifies that {@link TypeAdapter#toJsonTree(Object)} wraps an {@link IOException} from the
   * underlying {@link TypeAdapter#write(JsonWriter, Object)} method in a {@link JsonIOException}.
   */
  @Test
  public void toJsonTree_whenWriteThrowsIOException_wrapsInJsonIOException() {
    // The value '1' is arbitrary and does not affect the test outcome
    JsonIOException actualException =
        assertThrows(JsonIOException.class, () -> THROWING_ADAPTER.toJsonTree(1));

    assertThat(actualException).hasCauseThat().isSameInstanceAs(CAUSE_EXCEPTION);
  }
}