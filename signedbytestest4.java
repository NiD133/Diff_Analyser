package com.google.common.primitives;

import static com.google.common.primitives.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.primitives.SignedBytes.max;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SignedBytes#max(byte...)}.
 */
@DisplayName("SignedBytes.max()")
class SignedBytesMaxTest {

  @Test
  @DisplayName("should throw IllegalArgumentException for an empty array")
  void max_givenEmptyArray_throwsIllegalArgumentException() {
    // The Javadoc for SignedBytes.max() specifies that it throws an
    // IllegalArgumentException when the input array is empty.
    assertThrows(IllegalArgumentException.class, () -> max());
  }
}