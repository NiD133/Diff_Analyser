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
  private static final byte[] ARRAY1 = {(byte) 1};

  private static final byte LEAST = Byte.MIN_VALUE;
  private static final byte GREATEST = Byte.MAX_VALUE;

  private static final byte[] VALUES = {LEAST, -1, 0, 1, GREATEST};

  public void testCheckedCast_inRangeValue_isUnchanged() {
    for (byte value : VALUES) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
  }

  public void testCheckedCast_valueTooLarge_throwsIllegalArgumentException() {
    long tooLarge = (long) GREATEST + 1;
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(tooLarge));
    assertThat(thrown).hasMessageThat().contains(String.valueOf(tooLarge));

    thrown =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(Long.MAX_VALUE));
    assertThat(thrown).hasMessageThat().contains(String.valueOf(Long.MAX_VALUE));
  }

  public void testCheckedCast_valueTooSmall_throwsIllegalArgumentException() {
    long tooSmall = (long) LEAST - 1;
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(tooSmall));
    assertThat(thrown).hasMessageThat().contains(String.valueOf(tooSmall));

    thrown =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(Long.MIN_VALUE));
    assertThat(thrown).hasMessageThat().contains(String.valueOf(Long.MIN_VALUE));
  }

  public void testSaturatedCast_inRangeValue_isUnchanged() {
    for (byte value : VALUES) {
      assertThat(SignedBytes.saturatedCast((long) value)).isEqualTo(value);
    }
  }

  public void testSaturatedCast_valueTooLarge_saturatesToMaxByte() {
    assertThat(SignedBytes.saturatedCast((long) GREATEST + 1)).isEqualTo(GREATEST);
    assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE)).isEqualTo(GREATEST);
  }

  public void testSaturatedCast_valueTooSmall_saturatesToMinByte() {
    assertThat(SignedBytes.saturatedCast((long) LEAST - 1)).isEqualTo(LEAST);
    assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE)).isEqualTo(LEAST);
  }

  public void testCompare_hasSameSignAsByteCompare() {
    for (byte x : VALUES) {
      for (byte y : VALUES) {
        int expectedSign = Integer.signum(Byte.compare(x, y));
        int actualSign = Integer.signum(SignedBytes.compare(x, y));
        assertWithMessage("compare(" + x + ", " + y + ")")
            .that(actualSign)
            .isEqualTo(expectedSign);
      }
    }
  }

  public void testMax_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> max());
  }

  public void testMax_returnsLargestValue() {
    assertThat(max(LEAST)).isEqualTo(LEAST);
    assertThat(max(GREATEST)).isEqualTo(GREATEST);
    assertThat(max((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) 127);
  }

  public void testMin_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> min());
  }

  public void testMin_returnsSmallestValue() {
    assertThat(min(LEAST)).isEqualTo(LEAST);
    assertThat(min(GREATEST)).isEqualTo(GREATEST);
    assertThat(min((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) -128);
  }

  public void testJoin() {
    assertThat(SignedBytes.join(",", EMPTY)).isEmpty();
    assertThat(SignedBytes.join(",", ARRAY1)).isEqualTo("1");
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }

  @J2ktIncompatible // b/285319375
  public void testLexicographicalComparator() {
    List<byte[]> ordered =
        Arrays.asList(
            new byte[] {},
            new byte[] {LEAST},
            new byte[] {LEAST, LEAST},
            new byte[] {LEAST, (byte) 1},
            new byte[] {(byte) 1},
            new byte[] {(byte) 1, LEAST},
            new byte[] {GREATEST, GREATEST - (byte) 1},
            new byte[] {GREATEST, GREATEST},
            new byte[] {GREATEST, GREATEST, GREATEST});

    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    Helpers.testComparator(comparator, ordered);
  }

  @J2ktIncompatible
  @GwtIncompatible // SerializableTester
  public void testLexicographicalComparatorSerializable() {
    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    assertThat(SerializableTester.reserialize(comparator)).isSameInstanceAs(comparator);
  }

  public void testSortDescending() {
    // empty array
    testSortDescending(new byte[] {}, new byte[] {});
    // single element
    testSortDescending(new byte[] {1}, new byte[] {1});
    // two elements
    testSortDescending(new byte[] {1, 2}, new byte[] {2, 1});
    // with duplicates
    testSortDescending(new byte[] {1, 3, 1}, new byte[] {3, 1, 1});
    // with negative numbers
    testSortDescending(new byte[] {-1, 1, -2, 2}, new byte[] {2, 1, -1, -2});
    // with boundary values
    testSortDescending(VALUES.clone(), new byte[] {GREATEST, 1, 0, -1, LEAST});
  }

  private static void testSortDescending(byte[] input, byte[] expectedOutput) {
    input = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(input);
    assertThat(input).isEqualTo(expectedOutput);
  }

  private static void testSortDescending(
      byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
    input = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(input, fromIndex, toIndex);
    assertThat(input).isEqualTo(expectedOutput);
  }

  public void testSortDescendingIndexed() {
    // empty array, empty range
    testSortDescending(new byte[] {}, 0, 0, new byte[] {});
    // single element, full range
    testSortDescending(new byte[] {1}, 0, 1, new byte[] {1});
    // full range
    testSortDescending(new byte[] {1, 2}, 0, 2, new byte[] {2, 1});
    // partial range at start
    testSortDescending(new byte[] {1, 3, 1}, 0, 2, new byte[] {3, 1, 1});
    // partial range of one element (no-op)
    testSortDescending(new byte[] {1, 3, 1}, 0, 1, new byte[] {1, 3, 1});
    // partial range in middle
    testSortDescending(new byte[] {-1, -2, 1, 2}, 1, 3, new byte[] {-1, 1, -2, 2});
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}