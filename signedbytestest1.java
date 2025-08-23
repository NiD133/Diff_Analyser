package com.google.common.primitives;

import static com.google.common.primitives.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.J2ktIncompatible;
import org.jspecify.annotations.NullMarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes}.
 *
 * <p>This refactored test focuses on the {@code checkedCast} method, splitting the original
 * monolithic test into focused, descriptively named test cases.
 */
@GwtCompatible
@J2ktIncompatible // Original test was not J2kt compatible
@NullMarked
@RunWith(JUnit4.class)
public class SignedBytesTest {

  private static final byte MIN_BYTE = Byte.MIN_VALUE;
  private static final byte MAX_BYTE = Byte.MAX_VALUE;

  /** Tests that valid long values are cast correctly to bytes. */
  @Test
  public void checkedCast_withValidByteValues_returnsSameValue() {
    byte[] validValues = {MIN_BYTE, -1, 0, 1, MAX_BYTE};
    for (byte value : validValues) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
  }

  /** Tests that a value just larger than the maximum byte value throws an exception. */
  @Test
  public void checkedCast_withValueJustOverMax_throwsIllegalArgumentException() {
    long outOfRangeValue = (long) MAX_BYTE + 1;

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(outOfRangeValue));

    assertThat(exception).hasMessageThat().contains(String.valueOf(outOfRangeValue));
  }

  /** Tests that a value just smaller than the minimum byte value throws an exception. */
  @Test
  public void checkedCast_withValueJustUnderMin_throwsIllegalArgumentException() {
    long outOfRangeValue = (long) MIN_BYTE - 1;

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(outOfRangeValue));

    assertThat(exception).hasMessageThat().contains(String.valueOf(outOfRangeValue));
  }

  /** Tests that extreme long values (MIN_VALUE and MAX_VALUE) throw an exception. */
  @Test
  public void checkedCast_withExtremeLongValues_throwsIllegalArgumentException() {
    // Test with Long.MAX_VALUE
    IllegalArgumentException maxException =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(Long.MAX_VALUE));
    assertThat(maxException).hasMessageThat().contains(String.valueOf(Long.MAX_VALUE));

    // Test with Long.MIN_VALUE
    IllegalArgumentException minException =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(Long.MIN_VALUE));
    assertThat(minException).hasMessageThat().contains(String.valueOf(Long.MIN_VALUE));
  }
}