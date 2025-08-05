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
 * Tests various utility methods for handling signed byte values.
 */
@NullMarked
@GwtCompatible(emulated = true)
public class SignedBytesTest extends TestCase {
  
  // Constants for test data
  private static final byte[] EMPTY_ARRAY = {};
  private static final byte[] SINGLE_ELEMENT_ARRAY = {(byte) 1};
  private static final byte LEAST_BYTE = Byte.MIN_VALUE;
  private static final byte GREATEST_BYTE = Byte.MAX_VALUE;
  private static final byte[] TEST_VALUES = {LEAST_BYTE, -1, 0, 1, GREATEST_BYTE};

  /**
   * Tests the checkedCast method to ensure it correctly casts long values to byte
   * and throws exceptions for out-of-range values.
   */
  public void testCheckedCast() {
    for (byte value : TEST_VALUES) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
    assertCastFails(GREATEST_BYTE + 1L);
    assertCastFails(LEAST_BYTE - 1L);
    assertCastFails(Long.MAX_VALUE);
    assertCastFails(Long.MIN_VALUE);
  }

  /**
   * Tests the saturatedCast method to ensure it correctly casts long values to byte
   * and saturates to the nearest byte value for out-of-range values.
   */
  public void testSaturatedCast() {
    for (byte value : TEST_VALUES) {
      assertThat(SignedBytes.saturatedCast((long) value)).isEqualTo(value);
    }
    assertThat(SignedBytes.saturatedCast(GREATEST_BYTE + 1L)).isEqualTo(GREATEST_BYTE);
    assertThat(SignedBytes.saturatedCast(LEAST_BYTE - 1L)).isEqualTo(LEAST_BYTE);
    assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE)).isEqualTo(GREATEST_BYTE);
    assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE)).isEqualTo(LEAST_BYTE);
  }

  /**
   * Helper method to assert that a checked cast fails for a given value.
   */
  private static void assertCastFails(long value) {
    try {
      SignedBytes.checkedCast(value);
      fail("Expected IllegalArgumentException for value: " + value);
    } catch (IllegalArgumentException ex) {
      assertWithMessage("Exception message should contain the value: " + value)
          .that(ex.getMessage().contains(String.valueOf(value)))
          .isTrue();
    }
  }

  /**
   * Tests the compare method to ensure it correctly compares two byte values.
   */
  public void testCompare() {
    for (byte x : TEST_VALUES) {
      for (byte y : TEST_VALUES) {
        int expected = Byte.compare(x, y);
        int actual = SignedBytes.compare(x, y);
        assertWithMessage("Comparison failed for values: " + x + ", " + y)
            .that(Integer.signum(actual))
            .isEqualTo(Integer.signum(expected));
      }
    }
  }

  /**
   * Tests the max method with no arguments to ensure it throws an exception.
   */
  public void testMax_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> max());
  }

  /**
   * Tests the max method to ensure it returns the maximum value from a set of byte values.
   */
  public void testMax() {
    assertThat(max(LEAST_BYTE)).isEqualTo(LEAST_BYTE);
    assertThat(max(GREATEST_BYTE)).isEqualTo(GREATEST_BYTE);
    assertThat(max((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) 127);
  }

  /**
   * Tests the min method with no arguments to ensure it throws an exception.
   */
  public void testMin_noArgs() {
    assertThrows(IllegalArgumentException.class, () -> min());
  }

  /**
   * Tests the min method to ensure it returns the minimum value from a set of byte values.
   */
  public void testMin() {
    assertThat(min(LEAST_BYTE)).isEqualTo(LEAST_BYTE);
    assertThat(min(GREATEST_BYTE)).isEqualTo(GREATEST_BYTE);
    assertThat(min((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) -128);
  }

  /**
   * Tests the join method to ensure it correctly joins byte values into a string.
   */
  public void testJoin() {
    assertThat(SignedBytes.join(",", EMPTY_ARRAY)).isEmpty();
    assertThat(SignedBytes.join(",", SINGLE_ELEMENT_ARRAY)).isEqualTo("1");
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }

  /**
   * Tests the lexicographicalComparator method to ensure it correctly compares byte arrays.
   */
  @J2ktIncompatible // b/285319375
  public void testLexicographicalComparator() {
    List<byte[]> ordered = Arrays.asList(
        new byte[] {},
        new byte[] {LEAST_BYTE},
        new byte[] {LEAST_BYTE, LEAST_BYTE},
        new byte[] {LEAST_BYTE, (byte) 1},
        new byte[] {(byte) 1},
        new byte[] {(byte) 1, LEAST_BYTE},
        new byte[] {GREATEST_BYTE, GREATEST_BYTE - (byte) 1},
        new byte[] {GREATEST_BYTE, GREATEST_BYTE},
        new byte[] {GREATEST_BYTE, GREATEST_BYTE, GREATEST_BYTE}
    );

    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    Helpers.testComparator(comparator, ordered);
  }

  /**
   * Tests the lexicographicalComparator method for serializability.
   */
  @J2ktIncompatible
  @GwtIncompatible // SerializableTester
  public void testLexicographicalComparatorSerializable() {
    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    assertThat(SerializableTester.reserialize(comparator)).isSameInstanceAs(comparator);
  }

  /**
   * Tests the sortDescending method to ensure it correctly sorts byte arrays in descending order.
   */
  public void testSortDescending() {
    testSortDescending(new byte[] {}, new byte[] {});
    testSortDescending(new byte[] {1}, new byte[] {1});
    testSortDescending(new byte[] {1, 2}, new byte[] {2, 1});
    testSortDescending(new byte[] {1, 3, 1}, new byte[] {3, 1, 1});
    testSortDescending(new byte[] {-1, 1, -2, 2}, new byte[] {2, 1, -1, -2});
  }

  /**
   * Helper method to test sorting of byte arrays in descending order.
   */
  private static void testSortDescending(byte[] input, byte[] expectedOutput) {
    byte[] copy = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(copy);
    assertThat(copy).isEqualTo(expectedOutput);
  }

  /**
   * Helper method to test sorting of byte arrays in descending order within a specified range.
   */
  private static void testSortDescending(byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
    byte[] copy = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(copy, fromIndex, toIndex);
    assertThat(copy).isEqualTo(expectedOutput);
  }

  /**
   * Tests the sortDescending method with specified index range.
   */
  public void testSortDescendingIndexed() {
    testSortDescending(new byte[] {}, 0, 0, new byte[] {});
    testSortDescending(new byte[] {1}, 0, 1, new byte[] {1});
    testSortDescending(new byte[] {1, 2}, 0, 2, new byte[] {2, 1});
    testSortDescending(new byte[] {1, 3, 1}, 0, 2, new byte[] {3, 1, 1});
    testSortDescending(new byte[] {1, 3, 1}, 0, 1, new byte[] {1, 3, 1});
    testSortDescending(new byte[] {-1, -2, 1, 2}, 1, 3, new byte[] {-1, 1, -2, 2});
  }

  /**
   * Tests for null pointer exceptions in public static methods of SignedBytes class.
   */
  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}