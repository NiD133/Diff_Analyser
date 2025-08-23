package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes#join(String, byte...)}.
 *
 * <p>This test class has been refactored from its original version to improve understandability.
 * The original contained a single test method for multiple scenarios and included several unused
 * helper methods and constants.
 */
@GwtCompatible
@RunWith(JUnit4.class)
public class SignedBytesTestTest8 {

  private static final byte[] EMPTY_ARRAY = {};
  private static final byte[] ARRAY_WITH_SINGLE_ELEMENT = {(byte) 1};

  @Test
  public void join_withEmptyArray_returnsEmptyString() {
    assertThat(SignedBytes.join(",", EMPTY_ARRAY)).isEmpty();
  }

  @Test
  public void join_withSingleElementArray_returnsElementString() {
    assertThat(SignedBytes.join(",", ARRAY_WITH_SINGLE_ELEMENT)).isEqualTo("1");
  }

  @Test
  public void join_withMultipleElements_returnsElementsJoinedBySeparator() {
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
  }

  @Test
  public void join_withEmptySeparator_returnsConcatenatedElements() {
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
  }

  @Test
  public void join_withNegativeAndBoundaryValues_returnsJoinedString() {
    // Verifies that negative values, including the minimum byte value, are handled correctly.
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }
}