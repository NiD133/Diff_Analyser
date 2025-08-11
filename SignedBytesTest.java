/*
 * Copyright (C) 2008 The Guava Authors
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

package com.google.common.primitives;

import static com.google.common.primitives.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.primitives.SignedBytes.max;
import static com.google.common.primitives.SignedBytes.min;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.testing.Helpers;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

/**
 * Unit test for {@link SignedBytes}.
 *
 * @author Kevin Bourrillion
 */
@NullMarked
@GwtCompatible(emulated = true)
public class SignedBytesTest extends TestCase {
  private static final byte[] EMPTY = {};
  private static final byte[] SINGLE_ELEMENT_ARRAY = {(byte) 1};

  private static final byte MIN_VALUE = Byte.MIN_VALUE;
  private static final byte MAX_VALUE = Byte.MAX_VALUE;
  private static final byte[] ALL_VALUES = {MIN_VALUE, -1, 0, 1, MAX_VALUE};

  // Test cases for checkedCast
  public void testCheckedCast_validValues() {
    for (byte value : ALL_VALUES) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
  }

  public void testCheckedCast_outOfRangeValues() {
    assertCastFails(MAX_VALUE + 1L);
    assertCastFails(MIN_VALUE - 1L);
    assertCastFails(Long.MAX_VALUE);
    assertCastFails(Long.MIN_VALUE);
  }

  // Test cases for saturatedCast
  public void testSaturatedCast_validValues() {
    for (byte value : ALL_VALUES) {
      assertThat(SignedBytes.saturatedCast((long) value)).isEqualTo(value);
    }
  }

  public void testSaturatedCast_outOfRangeValues() {
    assertThat(SignedBytes.saturatedCast(MAX_VALUE + 1L)).isEqualTo(MAX_VALUE);
    assertThat(SignedBytes.saturatedCast(MIN_VALUE - 1L)).isEqualTo(MIN_VALUE);
    assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE)).isEqualTo(MAX_VALUE);
    assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE)).isEqualTo(MIN_VALUE);
  }

  // Helper for checkedCast failure cases
  private static void assertCastFails(long value) {
    try {
      SignedBytes.checkedCast(value);
      fail("Cast to byte should have failed: " + value);
    } catch (IllegalArgumentException ex) {
      assertWithMessage(value + " not found in exception text: " + ex.getMessage())
          .that(ex.getMessage().contains(String.valueOf(value)))
          .isTrue();
    }
  }

  // Test cases for compare
  public void testCompare() {
    for (byte x : ALL_VALUES) {
      for (byte y : ALL_VALUES) {
        int expected = Byte.compare(x, y);
        int actual = SignedBytes.compare(x, y);
        if (expected == 0) {
          assertWithMessage(x + ", " + y).that(actual).isEqualTo(expected);
        } else if (expected < 0) {
          assertWithMessage(x + ", " + y).that(actual).isLessThan(0);
        } else {
          assertWithMessage(x + ", " + y).that(actual).isGreaterThan(0);
        }
      }
    }
  }

  // Test cases for max
  public void testMax_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> max());
  }

  public void testMax_singleElement() {
    assertThat(max(MIN_VALUE)).isEqualTo(MIN_VALUE);
    assertThat(max(MAX_VALUE)).isEqualTo(MAX_VALUE);
  }

  public void testMax_multipleElements() {
    assertThat(max((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) 127);
  }

  // Test cases for min
  public void testMin_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> min());
  }

  public void testMin_singleElement() {
    assertThat(min(MIN_VALUE)).isEqualTo(MIN_VALUE);
    assertThat(min(MAX_VALUE)).isEqualTo(MAX_VALUE);
  }

  public void testMin_multipleElements() {
    assertThat(min((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) -128);
  }

  // Test cases for join
  public void testJoin_emptyArray() {
    assertThat(SignedBytes.join(",", EMPTY)).isEmpty();
  }

  public void testJoin_singleElement() {
    assertThat(SignedBytes.join(",", SINGLE_ELEMENT_ARRAY)).isEqualTo("1");
  }

  public void testJoin_multipleElements() {
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }

  // Test cases for lexicographicalComparator
  @J2ktIncompatible // b/285319375
  public void testLexicographicalComparator() {
    List<byte[]> orderedArrays = Arrays.asList(
        new byte[] {},
        new byte[] {MIN_VALUE},
        new byte[] {MIN_VALUE, MIN_VALUE},
        new byte[] {MIN_VALUE, (byte) 1},
        new byte[] {(byte) 1},
        new byte[] {(byte) 1, MIN_VALUE},
        new byte[] {MAX_VALUE, (byte) (MAX_VALUE - 1)},
        new byte[] {MAX_VALUE, MAX_VALUE},
        new byte[] {MAX_VALUE, MAX_VALUE, MAX_VALUE}
    );

    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    Helpers.testComparator(comparator, orderedArrays);
  }

  @J2ktIncompatible
  @GwtIncompatible // SerializableTester
  public void testLexicographicalComparatorSerializable() {
    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    assertThat(SerializableTester.reserialize(comparator)).isSameInstanceAs(comparator);
  }

  // Test cases for sortDescending
  public void testSortDescending_emptyArray() {
    testSortDescendingHelper(new byte[] {}, new byte[] {});
  }

  public void testSortDescending_singleElement() {
    testSortDescendingHelper(new byte[] {1}, new byte[] {1});
  }

  public void testSortDescending_multipleElements() {
    testSortDescendingHelper(new byte[] {1, 2}, new byte[] {2, 1});
    testSortDescendingHelper(new byte[] {1, 3, 1}, new byte[] {3, 1, 1});
    testSortDescendingHelper(new byte[] {-1, 1, -2, 2}, new byte[] {2, 1, -1, -2});
  }

  // Test cases for sortDescending with range parameters
  public void testSortDescendingIndexed_emptyRange() {
    testSortDescendingRangeHelper(new byte[] {}, 0, 0, new byte[] {});
  }

  public void testSortDescendingIndexed_fullArray() {
    testSortDescendingRangeHelper(new byte[] {1}, 0, 1, new byte[] {1});
    testSortDescendingRangeHelper(new byte[] {1, 2}, 0, 2, new byte[] {2, 1});
    testSortDescendingRangeHelper(new byte[] {1, 3, 1}, 0, 3, new byte[] {3, 1, 1});
  }

  public void testSortDescendingIndexed_partialArray() {
    testSortDescendingRangeHelper(new byte[] {1, 3, 1}, 0, 1, new byte[] {1, 3, 1});
    testSortDescendingRangeHelper(new byte[] {-1, -2, 1, 2}, 1, 3, new byte[] {-1, 1, -2, 2});
  }

  // Helper method for sortDescending tests
  private static void testSortDescendingHelper(byte[] input, byte[] expectedOutput) {
    byte[] sortedInput = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(sortedInput);
    assertThat(sortedInput).isEqualTo(expectedOutput);
  }

  // Helper method for sortDescending with range parameters
  private static void testSortDescendingRangeHelper(
      byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
    byte[] sortedInput = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(sortedInput, fromIndex, toIndex);
    assertThat(sortedInput).isEqualTo(expectedOutput);
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}