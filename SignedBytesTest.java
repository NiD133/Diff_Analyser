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
  
  // Test data constants
  private static final byte[] EMPTY_ARRAY = {};
  private static final byte[] SINGLE_ELEMENT_ARRAY = {(byte) 1};
  
  // Boundary values for signed bytes
  private static final byte MIN_BYTE_VALUE = Byte.MIN_VALUE; // -128
  private static final byte MAX_BYTE_VALUE = Byte.MAX_VALUE; // 127
  
  // Representative test values covering edge cases and typical values
  private static final byte[] TEST_VALUES = {
      MIN_BYTE_VALUE, // -128
      -1,
      0,
      1,
      MAX_BYTE_VALUE  // 127
  };

  public void testCheckedCast_withValidByteValues_returnsOriginalValue() {
    for (byte value : TEST_VALUES) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
  }
  
  public void testCheckedCast_withOutOfRangeValues_throwsException() {
    long valueTooLarge = MAX_BYTE_VALUE + 1L; // 128
    long valueTooSmall = MIN_BYTE_VALUE - 1L; // -129
    
    assertCastThrowsIllegalArgumentException(valueTooLarge);
    assertCastThrowsIllegalArgumentException(valueTooSmall);
    assertCastThrowsIllegalArgumentException(Long.MAX_VALUE);
    assertCastThrowsIllegalArgumentException(Long.MIN_VALUE);
  }

  public void testSaturatedCast_withValidByteValues_returnsOriginalValue() {
    for (byte value : TEST_VALUES) {
      assertThat(SignedBytes.saturatedCast((long) value)).isEqualTo(value);
    }
  }
  
  public void testSaturatedCast_withOutOfRangeValues_returnsClampedValue() {
    // Values too large should be clamped to MAX_BYTE_VALUE
    assertThat(SignedBytes.saturatedCast(MAX_BYTE_VALUE + 1L)).isEqualTo(MAX_BYTE_VALUE);
    assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE)).isEqualTo(MAX_BYTE_VALUE);
    
    // Values too small should be clamped to MIN_BYTE_VALUE
    assertThat(SignedBytes.saturatedCast(MIN_BYTE_VALUE - 1L)).isEqualTo(MIN_BYTE_VALUE);
    assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE)).isEqualTo(MIN_BYTE_VALUE);
  }

  private static void assertCastThrowsIllegalArgumentException(long valueOutOfRange) {
    try {
      SignedBytes.checkedCast(valueOutOfRange);
      fail("Expected IllegalArgumentException for value: " + valueOutOfRange);
    } catch (IllegalArgumentException ex) {
      String errorMessage = ex.getMessage();
      assertWithMessage("Exception message should contain the invalid value: " + valueOutOfRange)
          .that(errorMessage.contains(String.valueOf(valueOutOfRange)))
          .isTrue();
    }
  }

  public void testCompare_withAllValueCombinations_behavesLikeByteCompare() {
    for (byte firstValue : TEST_VALUES) {
      for (byte secondValue : TEST_VALUES) {
        int expectedResult = Byte.compare(firstValue, secondValue);
        int actualResult = SignedBytes.compare(firstValue, secondValue);
        
        assertCompareResultsMatch(firstValue, secondValue, expectedResult, actualResult);
      }
    }
  }
  
  private static void assertCompareResultsMatch(byte x, byte y, int expected, int actual) {
    String comparisonContext = String.format("Comparing %d and %d", x, y);
    
    if (expected == 0) {
      assertWithMessage(comparisonContext).that(actual).isEqualTo(0);
    } else if (expected < 0) {
      assertWithMessage(comparisonContext + " should return negative").that(actual < 0).isTrue();
    } else {
      assertWithMessage(comparisonContext + " should return positive").that(actual > 0).isTrue();
    }
  }

  public void testMax_withNoArguments_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> max());
  }

  public void testMax_withSingleValue_returnsThatValue() {
    assertThat(max(MIN_BYTE_VALUE)).isEqualTo(MIN_BYTE_VALUE);
    assertThat(max(MAX_BYTE_VALUE)).isEqualTo(MAX_BYTE_VALUE);
  }
  
  public void testMax_withMultipleValues_returnsLargestValue() {
    byte[] mixedValues = {0, -128, -1, 127, 1};
    assertThat(max(mixedValues)).isEqualTo((byte) 127);
  }

  public void testMin_withNoArguments_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> min());
  }

  public void testMin_withSingleValue_returnsThatValue() {
    assertThat(min(MIN_BYTE_VALUE)).isEqualTo(MIN_BYTE_VALUE);
    assertThat(min(MAX_BYTE_VALUE)).isEqualTo(MAX_BYTE_VALUE);
  }
  
  public void testMin_withMultipleValues_returnsSmallestValue() {
    byte[] mixedValues = {0, -128, -1, 127, 1};
    assertThat(min(mixedValues)).isEqualTo((byte) -128);
  }

  public void testJoin_withVariousInputs_createsCorrectString() {
    // Empty array
    assertThat(SignedBytes.join(",", EMPTY_ARRAY)).isEmpty();
    
    // Single element
    assertThat(SignedBytes.join(",", SINGLE_ELEMENT_ARRAY)).isEqualTo("1");
    
    // Multiple elements with comma separator
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
    
    // Multiple elements with no separator
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
    
    // Negative values
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }

  @J2ktIncompatible // b/285319375
  public void testLexicographicalComparator_withOrderedArrays_maintainsCorrectOrder() {
    List<byte[]> arraysInExpectedOrder = Arrays.asList(
        new byte[] {},                                    // Empty array comes first
        new byte[] {MIN_BYTE_VALUE},                     // Single minimum value
        new byte[] {MIN_BYTE_VALUE, MIN_BYTE_VALUE},     // Two minimum values
        new byte[] {MIN_BYTE_VALUE, (byte) 1},           // Min followed by positive
        new byte[] {(byte) 1},                           // Single positive value
        new byte[] {(byte) 1, MIN_BYTE_VALUE},           // Positive followed by min
        new byte[] {MAX_BYTE_VALUE, MAX_BYTE_VALUE - 1}, // Near-max values
        new byte[] {MAX_BYTE_VALUE, MAX_BYTE_VALUE},     // Two max values
        new byte[] {MAX_BYTE_VALUE, MAX_BYTE_VALUE, MAX_BYTE_VALUE} // Three max values
    );

    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    Helpers.testComparator(comparator, arraysInExpectedOrder);
  }

  @J2ktIncompatible
  @GwtIncompatible // SerializableTester
  public void testLexicographicalComparator_afterSerialization_returnsSameInstance() {
    Comparator<byte[]> originalComparator = SignedBytes.lexicographicalComparator();
    Comparator<byte[]> deserializedComparator = SerializableTester.reserialize(originalComparator);
    
    assertThat(deserializedComparator).isSameInstanceAs(originalComparator);
  }

  public void testSortDescending_withVariousArrays_sortsCorrectly() {
    verifySortDescending(new byte[] {}, new byte[] {});
    verifySortDescending(new byte[] {1}, new byte[] {1});
    verifySortDescending(new byte[] {1, 2}, new byte[] {2, 1});
    verifySortDescending(new byte[] {1, 3, 1}, new byte[] {3, 1, 1});
    verifySortDescending(new byte[] {-1, 1, -2, 2}, new byte[] {2, 1, -1, -2});
  }

  public void testSortDescending_withIndexRange_sortsOnlySpecifiedRange() {
    verifySortDescendingWithRange(new byte[] {}, 0, 0, new byte[] {});
    verifySortDescendingWithRange(new byte[] {1}, 0, 1, new byte[] {1});
    verifySortDescendingWithRange(new byte[] {1, 2}, 0, 2, new byte[] {2, 1});
    verifySortDescendingWithRange(new byte[] {1, 3, 1}, 0, 2, new byte[] {3, 1, 1});
    verifySortDescendingWithRange(new byte[] {1, 3, 1}, 0, 1, new byte[] {1, 3, 1});
    verifySortDescendingWithRange(new byte[] {-1, -2, 1, 2}, 1, 3, new byte[] {-1, 1, -2, 2});
  }

  private static void verifySortDescending(byte[] inputArray, byte[] expectedOutput) {
    byte[] arrayToSort = Arrays.copyOf(inputArray, inputArray.length);
    SignedBytes.sortDescending(arrayToSort);
    assertThat(arrayToSort).isEqualTo(expectedOutput);
  }

  private static void verifySortDescendingWithRange(
      byte[] inputArray, int fromIndex, int toIndex, byte[] expectedOutput) {
    byte[] arrayToSort = Arrays.copyOf(inputArray, inputArray.length);
    SignedBytes.sortDescending(arrayToSort, fromIndex, toIndex);
    assertThat(arrayToSort).isEqualTo(expectedOutput);
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullPointerHandling_forAllPublicMethods_throwsNullPointerException() {
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}