package com.google.common.primitives;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Readable, behavior-focused tests for SignedBytes.
 *
 * Each test name states the scenario and the expected outcome.
 * Tests avoid EvoSuite-specific scaffolding and verify typical success and failure cases.
 */
class SignedBytesTest {

  // -------- checkedCast --------

  @Test
  void checkedCast_withinRange_returnsSameValue() {
    assertEquals((byte) 0, SignedBytes.checkedCast(0L));
    assertEquals((byte) -1, SignedBytes.checkedCast(-1L));
    assertEquals((byte) 39, SignedBytes.checkedCast(39L));
    assertEquals(Byte.MIN_VALUE, SignedBytes.checkedCast((long) Byte.MIN_VALUE));
    assertEquals(Byte.MAX_VALUE, SignedBytes.checkedCast((long) Byte.MAX_VALUE));
  }

  @Test
  void checkedCast_outOfRange_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(1940L));
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(-1826L));
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast((long) Byte.MAX_VALUE + 1));
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast((long) Byte.MIN_VALUE - 1));
  }

  // -------- saturatedCast --------

  @Test
  void saturatedCast_withinRange_returnsSameValue() {
    assertEquals((byte) -128, SignedBytes.saturatedCast((byte) -128)); // boundary
    assertEquals((byte) 127, SignedBytes.saturatedCast(127L));         // boundary
    assertEquals((byte) 0, SignedBytes.saturatedCast(0L));
  }

  @Test
  void saturatedCast_outOfRange_clampsToBounds() {
    assertEquals(Byte.MIN_VALUE, SignedBytes.saturatedCast(-2776L));
    assertEquals(Byte.MAX_VALUE, SignedBytes.saturatedCast(209L));
  }

  // -------- compare --------

  @Test
  void compare_equal_returnsZero() {
    assertEquals(0, SignedBytes.compare((byte) 0, (byte) 0));
  }

  @Test
  void compare_firstGreater_returnsPositive() {
    assertTrue(SignedBytes.compare((byte) 111, (byte) 0) > 0);
  }

  @Test
  void compare_firstLess_returnsNegative() {
    assertTrue(SignedBytes.compare((byte) 0, (byte) 84) < 0);
  }

  // -------- min --------

  @Test
  void min_nonEmpty_returnsSmallestValue() {
    assertEquals((byte) 39, SignedBytes.min(new byte[] {39, 110, 54}));
    assertEquals((byte) -69, SignedBytes.min(new byte[] {0, -69}));
    assertEquals((byte) 0, SignedBytes.min(new byte[] {16, 0, 0, 0, 0, 0, 0, 0}));
  }

  @Test
  void min_emptyArray_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.min(new byte[0]));
  }

  @Test
  void min_nullArray_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> SignedBytes.min((byte[]) null));
  }

  // -------- max --------

  @Test
  void max_nonEmpty_returnsLargestValue() {
    assertEquals((byte) -81, SignedBytes.max(new byte[] {-81}));
    assertEquals((byte) 110, SignedBytes.max(new byte[] {0, 110, 0}));
    assertEquals((byte) 0, SignedBytes.max(new byte[] {0, 0, 0}));
  }

  @Test
  void max_emptyArray_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SignedBytes.max(new byte[0]));
  }

  @Test
  void max_nullArray_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> SignedBytes.max((byte[]) null));
  }

  // -------- join --------

  @Test
  void join_emptyArray_returnsEmptyString() {
    assertEquals("", SignedBytes.join("1", new byte[0]));
  }

  @Test
  void join_separatorPlacedBetweenValues_only() {
    assertEquals("0&#GMks!-I`k0&#GMks!-I`k0",
        SignedBytes.join("&#GMks!-I`k", new byte[] {0, 0, 0}));
  }

  @Test
  void join_nullArray_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> SignedBytes.join("sep", (byte[]) null));
  }

  @Test
  void join_nullSeparator_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> SignedBytes.join(null, new byte[0]));
  }

  // -------- lexicographicalComparator --------

  @Test
  @DisplayName("lexicographicalComparator: [] < [1] < [1, -128] < [1, 127] < [2]")
  void lexicographicalComparator_respectsDocumentedOrder() {
    Comparator<byte[]> cmp = SignedBytes.lexicographicalComparator();

    assertTrue(cmp.compare(new byte[] {}, new byte[] {1}) < 0);
    assertTrue(cmp.compare(new byte[] {1}, new byte[] {1, -128}) < 0);
    assertTrue(cmp.compare(new byte[] {1, -128}, new byte[] {1, 127}) < 0);
    assertTrue(cmp.compare(new byte[] {1, 127}, new byte[] {2}) < 0);
  }

  @Test
  void lexicographicalComparator_isNonNull() {
    assertNotNull(SignedBytes.lexicographicalComparator());
  }

  // -------- sortDescending (whole array) --------

  @Test
  void sortDescending_sortsEntireArrayInPlace() {
    byte[] data = {3, -1, 2, 127, -128};
    SignedBytes.sortDescending(data);
    assertArrayEquals(new byte[] {127, 3, 2, -1, -128}, data);
  }

  @Test
  void sortDescending_nullArray_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> SignedBytes.sortDescending((byte[]) null));
  }

  // -------- sortDescending (sub-range) --------

  @Test
  void sortDescending_subRange_onlyAffectsThatRange() {
    byte[] data = {5, 2, 9, 1, 0};
    // Sort indices [1, 4): elements {2, 9, 1} -> {9, 2, 1}
    SignedBytes.sortDescending(data, 1, 4);
    assertArrayEquals(new byte[] {5, 9, 2, 1, 0}, data);
  }

  @Test
  void sortDescending_subRange_allowsSingleElementNoOp() {
    byte[] data = {10, 20, 30};
    SignedBytes.sortDescending(data, 0, 1); // only the first element
    assertArrayEquals(new byte[] {10, 20, 30}, data);
  }

  @Test
  void sortDescending_subRange_invalidIndexes_throwIndexOutOfBounds() {
    byte[] data = new byte[13];
    assertThrows(IndexOutOfBoundsException.class, () -> SignedBytes.sortDescending(data, 127, 127));
    assertThrows(IndexOutOfBoundsException.class, () -> SignedBytes.sortDescending(data, -1, 5));
    assertThrows(IndexOutOfBoundsException.class, () -> SignedBytes.sortDescending(data, 5, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> SignedBytes.sortDescending(data, 0, 14));
  }
}