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
 * Tests for SignedBytes.
 * 
 * The tests are organized by API surface area and favor small, intention-revealing helpers and
 * messages. Where appropriate, we assert only on the sign of comparisons (to match the contract).
 */
@NullMarked
@GwtCompatible(emulated = true)
public class SignedBytesTest extends TestCase {

  private static final byte[] EMPTY = {};
  private static final byte[] ARRAY1 = {(byte) 1};

  private static final byte LEAST = Byte.MIN_VALUE;
  private static final byte GREATEST = Byte.MAX_VALUE;

  private static final byte[] VALUES = {LEAST, (byte) -1, (byte) 0, (byte) 1, GREATEST};

  // ----------------------------------------------------------------------------------------------
  // Casting
  // ----------------------------------------------------------------------------------------------

  public void testCheckedCast_withinRange_returnsSameValue() {
    for (byte value : VALUES) {
      assertThat(SignedBytes.checkedCast((long) value)).isEqualTo(value);
    }
  }

  public void testCheckedCast_outOfRange_throwsWithValueInMessage() {
    assertCastFailsWithValueInMessage(GREATEST + 1L);
    assertCastFailsWithValueInMessage(LEAST - 1L);
    assertCastFailsWithValueInMessage(Long.MAX_VALUE);
    assertCastFailsWithValueInMessage(Long.MIN_VALUE);
  }

  public void testSaturatedCast_withinRange_returnsSameValue() {
    for (byte value : VALUES) {
      assertThat(SignedBytes.saturatedCast((long) value)).isEqualTo(value);
    }
  }

  public void testSaturatedCast_outOfRange_clampsToBounds() {
    assertThat(SignedBytes.saturatedCast(GREATEST + 1L)).isEqualTo(GREATEST);
    assertThat(SignedBytes.saturatedCast(LEAST - 1L)).isEqualTo(LEAST);
    assertThat(SignedBytes.saturatedCast(Long.MAX_VALUE)).isEqualTo(GREATEST);
    assertThat(SignedBytes.saturatedCast(Long.MIN_VALUE)).isEqualTo(LEAST);
  }

  private static void assertCastFailsWithValueInMessage(long value) {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> SignedBytes.checkedCast(value));
    assertWithMessage("Exception message should contain the offending value")
        .that(ex.getMessage().contains(String.valueOf(value)))
        .isTrue();
  }

  // ----------------------------------------------------------------------------------------------
  // Comparison
  // ----------------------------------------------------------------------------------------------

  public void testCompare_matchesByteCompareSign() {
    for (byte x : VALUES) {
      for (byte y : VALUES) {
        int expected = Byte.compare(x, y);
        int actual = SignedBytes.compare(x, y);

        // We assert on sign only (contract); magnitude may differ.
        assertWithMessage("compare(%s, %s)", x, y)
            .that(Integer.signum(actual))
            .isEqualTo(Integer.signum(expected));
      }
    }
  }

  // ----------------------------------------------------------------------------------------------
  // Min/Max
  // ----------------------------------------------------------------------------------------------

  public void testMax_noArgs_throws() {
    assertThrows(IllegalArgumentException.class, () -> max());
  }

  public void testMax_varargs() {
    assertThat(max(LEAST)).isEqualTo(LEAST);
    assertThat(max(GREATEST)).isEqualTo(GREATEST);
    assertThat(max((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) 127);
  }

  public void testMin_noArgs_throws() {
    assertThrows(IllegalArgumentException.class, () -> min());
  }

  public void testMin_varargs() {
    assertThat(min(LEAST)).isEqualTo(LEAST);
    assertThat(min(GREATEST)).isEqualTo(GREATEST);
    assertThat(min((byte) 0, (byte) -128, (byte) -1, (byte) 127, (byte) 1)).isEqualTo((byte) -128);
  }

  // ----------------------------------------------------------------------------------------------
  // Join
  // ----------------------------------------------------------------------------------------------

  public void testJoin() {
    assertThat(SignedBytes.join(",", EMPTY)).isEmpty();
    assertThat(SignedBytes.join(",", ARRAY1)).isEqualTo("1");
    assertThat(SignedBytes.join(",", (byte) 1, (byte) 2)).isEqualTo("1,2");
    assertThat(SignedBytes.join("", (byte) 1, (byte) 2, (byte) 3)).isEqualTo("123");
    assertThat(SignedBytes.join(",", (byte) -128, (byte) -1)).isEqualTo("-128,-1");
  }

  // ----------------------------------------------------------------------------------------------
  // Lexicographical comparator
  // ----------------------------------------------------------------------------------------------

  @J2ktIncompatible // b/285319375
  public void testLexicographicalComparator_ordersCorrectly() {
    // Ordered ascending by lexicographic comparison of signed bytes.
    List<byte[]> ordered =
        Arrays.asList(
            new byte[] {},
            new byte[] {LEAST},
            new byte[] {LEAST, LEAST},
            new byte[] {LEAST, (byte) 1},
            new byte[] {(byte) 1},
            new byte[] {(byte) 1, LEAST},
            new byte[] {GREATEST, (byte) (GREATEST - 1)},
            new byte[] {GREATEST, GREATEST},
            new byte[] {GREATEST, GREATEST, GREATEST});

    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    Helpers.testComparator(comparator, ordered);
  }

  @J2ktIncompatible
  @GwtIncompatible // SerializableTester
  public void testLexicographicalComparator_isSerializableSingleton() {
    Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
    assertThat(SerializableTester.reserialize(comparator)).isSameInstanceAs(comparator);
  }

  // ----------------------------------------------------------------------------------------------
  // Sorting (descending)
  // ----------------------------------------------------------------------------------------------

  public void testSortDescending_wholeArray() {
    assertSortDescending(new byte[] {}, new byte[] {});
    assertSortDescending(new byte[] {1}, new byte[] {1});
    assertSortDescending(new byte[] {1, 2}, new byte[] {2, 1});
    assertSortDescending(new byte[] {1, 3, 1}, new byte[] {3, 1, 1});
    assertSortDescending(new byte[] {-1, 1, -2, 2}, new byte[] {2, 1, -1, -2});
  }

  public void testSortDescending_subrangeOnly() {
    // Empty slice
    assertSortDescending(new byte[] {}, 0, 0, new byte[] {});
    // Single element slice
    assertSortDescending(new byte[] {1}, 0, 1, new byte[] {1});
    // Full array
    assertSortDescending(new byte[] {1, 2}, 0, 2, new byte[] {2, 1});
    // Prefix only
    assertSortDescending(new byte[] {1, 3, 1}, 0, 2, new byte[] {3, 1, 1});
    // No-op (slice of length 1)
    assertSortDescending(new byte[] {1, 3, 1}, 0, 1, new byte[] {1, 3, 1});
    // Middle slice sorted; ends unchanged
    assertSortDescending(new byte[] {-1, -2, 1, 2}, 1, 3, new byte[] {-1, 1, -2, 2});
  }

  private static void assertSortDescending(byte[] input, byte[] expectedOutput) {
    byte[] actual = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(actual);
    assertThat(actual).isEqualTo(expectedOutput);
  }

  private static void assertSortDescending(
      byte[] input, int fromIndex, int toIndex, byte[] expectedOutput) {
    byte[] actual = Arrays.copyOf(input, input.length);
    SignedBytes.sortDescending(actual, fromIndex, toIndex);
    assertThat(actual).isEqualTo(expectedOutput);
  }

  // ----------------------------------------------------------------------------------------------
  // Nulls
  // ----------------------------------------------------------------------------------------------

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testAllPublicStaticMethods_rejectNullsWhereRequired() {
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}