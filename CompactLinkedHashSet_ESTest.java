package com.google.common.collect;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for CompactLinkedHashSet.
 *
 * Goals:
 * - Validate core Set behaviors: creation, insertion order, deduplication, null handling.
 * - Validate array conversions, spliterator behavior, and clearing.
 * - Validate error cases for invalid inputs.
 *
 * These tests avoid brittle internal details and emphasize easy-to-understand assertions.
 */
public class CompactLinkedHashSetTest {

  // --- Creation -------------------------------------------------------------------------------

  @Test
  public void create_emptySet_hasSizeZeroAndIsEmpty() {
    // Arrange + Act
    CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();

    // Assert
    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
    assertArrayEquals(new Object[0], set.toArray());
  }

  @Test
  public void create_fromVarargs_deduplicatesAndPreservesInsertionOrder_includingNulls() {
    // Arrange + Act
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(1, 2, 1, null, null);

    // Assert
    assertEquals(3, set.size()); // 1, 2, null
    assertIteratesInOrder(set, Arrays.asList(1, 2, null));
  }

  @Test
  public void create_fromCollection_preservesIterationOrderOfSource() {
    // Arrange
    List<String> source = Arrays.asList("a", "b", "a", "c");

    // Act
    CompactLinkedHashSet<String> set = CompactLinkedHashSet.create(source);

    // Assert
    assertEquals(3, set.size());
    assertIteratesInOrder(set, Arrays.asList("a", "b", "c"));
  }

  @Test
  public void createWithExpectedSize_nonNegative_okAndEmpty() {
    // Arrange + Act
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(32);

    // Assert
    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
  }

  // --- Error cases ----------------------------------------------------------------------------

  @Test
  public void createWithExpectedSize_negative_throwsIllegalArgumentException() {
    // Assert
    assertThrows(IllegalArgumentException.class, () -> CompactLinkedHashSet.createWithExpectedSize(-1));
  }

  @Test
  public void constructor_negativeExpectedSize_throwsIllegalArgumentException() {
    // Assert
    assertThrows(IllegalArgumentException.class, () -> new CompactLinkedHashSet<Object>(-1));
  }

  @Test
  public void create_fromNullCollection_throwsNullPointerException() {
    // Assert
    assertThrows(NullPointerException.class, () -> CompactLinkedHashSet.create((java.util.Collection<Object>) null));
  }

  @Test
  public void create_fromNullVarargs_throwsNullPointerException() {
    // Arrange
    String[] nullArray = null;

    // Assert
    assertThrows(NullPointerException.class, () -> CompactLinkedHashSet.create(nullArray));
  }

  // --- Core operations ------------------------------------------------------------------------

  @Test
  public void add_preservesInsertionOrder_likeLinkedHashSet() {
    // Arrange
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create();

    // Act
    set.add(10);
    set.add(20);
    set.add(10); // duplicate
    set.add(30);

    // Assert
    assertEquals(3, set.size());
    assertIteratesInOrder(set, Arrays.asList(10, 20, 30));
  }

  @Test
  public void clear_removesAllElements() {
    // Arrange
    CompactLinkedHashSet<String> set = CompactLinkedHashSet.create("x", "y", "z");

    // Act
    set.clear();

    // Assert
    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
  }

  @Test
  public void retainAll_disjointCollection_removesAllAndReturnsTrue() {
    // Arrange
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(1, 2, 3);

    // Act
    boolean changed = set.retainAll(Collections.singleton(99));

    // Assert
    assertTrue(changed);
    assertTrue(set.isEmpty());
  }

  // --- Array conversions ----------------------------------------------------------------------

  @Test
  public void toArray_returnsElementsInInsertionOrder() {
    // Arrange
    CompactLinkedHashSet<String> set = CompactLinkedHashSet.create("a", "b", "c");

    // Act
    Object[] arr = set.toArray();

    // Assert
    assertArrayEquals(new Object[] {"a", "b", "c"}, arr);
  }

  @Test
  public void toArray_givenArrayOfExactSize_returnsSameInstanceFilled() {
    // Arrange
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(1, 2);
    Integer[] target = new Integer[2];

    // Act
    Integer[] result = set.toArray(target);

    // Assert
    assertSame(target, result);
    assertArrayEquals(new Integer[] {1, 2}, result);
  }

  @Test
  public void toArray_givenLargerArray_setsNullTerminatorAndReturnsSameArray() {
    // Arrange
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(7, 8);
    Integer[] target = new Integer[5];
    Arrays.fill(target, -1);

    // Act
    Integer[] result = set.toArray(target);

    // Assert
    assertSame(target, result);
    assertEquals(Integer.valueOf(7), result[0]);
    assertEquals(Integer.valueOf(8), result[1]);
    assertNull("Element after last should be null", result[2]);
  }

  // --- Spliterator ----------------------------------------------------------------------------

  @Test
  public void spliterator_nonNull_andIteratesInInsertionOrder() {
    // Arrange
    CompactLinkedHashSet<String> set = CompactLinkedHashSet.create("first", "second", "third");

    // Act
    Spliterator<String> spliterator = set.spliterator();
    List<String> seen = new ArrayList<>();
    spliterator.forEachRemaining(seen::add);

    // Assert
    assertNotNull(spliterator);
    assertEquals(Arrays.asList("first", "second", "third"), seen);
  }

  // --- Misc -----------------------------------------------------------------------------------

  @Test
  public void convertToHashFloodingResistantImplementation_onEmpty_returnsEmptySet() {
    // Arrange
    CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();

    // Act
    Set<Object> converted = set.convertToHashFloodingResistantImplementation();

    // Assert
    assertTrue(converted.isEmpty());
    assertEquals(0, converted.size());
  }

  // --- Helpers --------------------------------------------------------------------------------

  private static <T> void assertIteratesInOrder(Iterable<T> actual, List<T> expected) {
    List<T> seen = new ArrayList<>();
    Iterator<T> it = actual.iterator();
    while (it.hasNext()) {
      seen.add(it.next());
    }
    assertEquals("Iteration order mismatch", expected, seen);
  }
}