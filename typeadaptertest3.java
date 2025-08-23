package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

/**
 * Tests the behavior of the {@link TypeAdapter#nullSafe()} wrapper,
 * specifically its {@code toString()} implementation.
 */
public class TypeAdapterNullSafeTest {

  // A simple TypeAdapter with a custom toString() to act as the delegate.
  // The read/write methods are designed to fail if ever called, as this
  // test only focuses on the toString() behavior.
  private static final TypeAdapter<String> delegateAdapter = new TypeAdapter<>() {
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
      return "DelegateAdapter";
    }
  };

  @Test
  public void testToString_reflectsWrapperAndDelegate() {
    // 1. The base adapter should have its own toString() representation.
    assertThat(delegateAdapter.toString()).isEqualTo("DelegateAdapter");

    // 2. When wrapped, the toString() should indicate the NullSafe wrapper
    //    and include the delegate's toString().
    TypeAdapter<String> nullSafeAdapter = delegateAdapter.nullSafe();
    assertThat(nullSafeAdapter.toString())
        .isEqualTo("NullSafeTypeAdapter[DelegateAdapter]");

    // 3. Calling nullSafe() on an already null-safe adapter should be idempotent
    //    and not change the toString() representation.
    TypeAdapter<String> nullSafeAgainAdapter = nullSafeAdapter.nullSafe();
    assertThat(nullSafeAgainAdapter.toString())
        .isEqualTo("NullSafeTypeAdapter[DelegateAdapter]");
  }
}