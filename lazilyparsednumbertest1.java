package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

  @Test
  public void hashCode_forEqualObjects_isSame() {
    // Arrange: Create two LazilyParsedNumber instances with the same underlying string value.
    // These objects are expected to be equal.
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("123");

    // Act & Assert:
    // First, confirm that the objects are indeed equal. This makes the hashCode test meaningful.
    assertThat(number2).isEqualTo(number1);

    // Then, assert that their hash codes are identical, fulfilling the Object.hashCode() contract.
    assertThat(number2.hashCode()).isEqualTo(number1.hashCode());
  }
}