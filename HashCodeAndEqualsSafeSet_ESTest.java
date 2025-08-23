package org.mockito.internal.util.collections;

import static java.util.Objects.isNull;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

import org.junit.Test;

public class HashCodeAndEqualsSafeSetTest {

  // ---------------------------------------------------------------------------
  // Factory methods
  // ---------------------------------------------------------------------------

  @Test
  public void shouldCreateEmptySetFromNullIterable() {
    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);

    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
  }

  @Test
  public void shouldCreateEmptySetFromEmptyArray() {
    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(new Object[0]);

    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowWhenVarargsArrayIsNull() {
    HashCodeAndEqualsSafeSet.of((Object[]) null);
  }

  @Test
  public void emptySetsCreatedInDifferentWaysAreEqual() {
    HashCodeAndEqualsSafeSet fromNullIterable = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
    HashCodeAndEqualsSafeSet fromEmptyCollection = HashCodeAndEqualsSafeSet.of(new Vector<>());

    assertTrue(fromNullIterable.equals(fromEmptyCollection));
    assertTrue(fromEmptyCollection.equals(fromNullIterable));
    assertEquals(fromNullIterable.hashCode(), fromEmptyCollection.hashCode());
  }

  // ---------------------------------------------------------------------------
  // Basic Set semantics
  // ---------------------------------------------------------------------------

  @Test
  public void shouldAddContainsAndRemoveSingleElement() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Object value = new Object();

    assertTrue(set.add(value));
    assertTrue(set.contains(value));
    assertTrue(set.remove(value));
    assertFalse(set.contains(value));
    assertTrue(set.isEmpty());
  }

  @Test
  public void shouldNotAddDuplicateElements() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Object value = new Object();

    assertTrue(set.add(value));
    assertFalse("Second add of the same element should return false", set.add(value));
    assertEquals(1, set.size());
  }

  @Test
  public void shouldBeEmptyAfterClear() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    set.add(new Object());

    set.clear();

    assertTrue(set.isEmpty());
    assertEquals(0, set.size());
  }

  @Test
  public void shouldNotBeEqualToNull() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    assertFalse(set.equals(null));
  }

  @Test
  public void shouldProvideNonNullIteratorForEmptySet() {
    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
    Iterator<Object> it = set.iterator();

    assertNotNull(it);
    assertFalse(it.hasNext());
  }

  // ---------------------------------------------------------------------------
  // Bulk operations
  // ---------------------------------------------------------------------------

  @Test
  public void addAllWithEmptyCollectionReturnsFalse() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

    assertFalse(set.addAll(new ArrayList<>()));
    assertTrue(set.isEmpty());
  }

  @Test
  public void removeAllOnSelfWhenEmptyReturnsFalse() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

    assertFalse(set.removeAll(set));
  }

  @Test
  public void retainAllWithDisjointCollectionOnEmptyReturnsFalse() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Collection<Object> other = List.of("a", "b");

    assertFalse(set.retainAll(other));
  }

  @Test(expected = IllegalArgumentException.class)
  public void addAllShouldRejectNullCollection() {
    new HashCodeAndEqualsSafeSet().addAll((Collection<?>) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void containsAllShouldRejectNullCollection() {
    new HashCodeAndEqualsSafeSet().containsAll((Collection<?>) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeAllShouldRejectNullCollection() {
    new HashCodeAndEqualsSafeSet().removeAll((Collection<?>) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void retainAllShouldRejectNullCollection() {
    new HashCodeAndEqualsSafeSet().retainAll((Collection<?>) null);
  }

  // ---------------------------------------------------------------------------
  // toArray variants
  // ---------------------------------------------------------------------------

  @Test
  public void toArrayWithZeroLengthTypedArrayReturnsSameArrayForEmptySet() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Object[] input = new Object[0];

    Object[] result = set.toArray(input);

    assertSame("Empty set should return the same zero-length array instance", input, result);
    assertEquals(0, result.length);
  }

  @Test
  public void toArrayWithLargerTypedArraySetsNullTerminator() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Object[] input = new Object[3];

    Object[] result = set.toArray(input);

    assertSame("Should reuse provided array", input, result);
    // Per Collection.toArray contract: if the array is larger, set element after last to null
    assertNull(result[0]); // size is 0, so first element must be null
  }

  @Test
  public void toArrayReturnsAllElements() {
    Object a = "a";
    Object b = "b";
    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(a, b);

    Object[] array = set.toArray();

    assertEquals(2, array.length);
    List<Object> asList = Arrays.asList(array);
    assertTrue(asList.contains(a));
    assertTrue(asList.contains(b));
  }

  @Test(expected = NullPointerException.class)
  public void toArrayShouldRejectNullTypedArray() {
    new HashCodeAndEqualsSafeSet().toArray((Object[]) null);
  }

  @Test(expected = ArrayStoreException.class)
  public void toArrayShouldThrowWhenTypedArrayComponentIsIncompatible() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    set.add(new Object()); // store a regular object

    // Ask to write objects into an array of HashCodeAndEqualsMockWrapper -> incompatible
    HashCodeAndEqualsMockWrapper[] incompatible = new HashCodeAndEqualsMockWrapper[1];
    set.toArray(incompatible);
  }

  // ---------------------------------------------------------------------------
  // Additional behaviors
  // ---------------------------------------------------------------------------

  @Test
  public void removeIfShouldRemoveMatchingElements() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    Object keep = new Object();
    set.add(keep);
    set.add(null);

    Predicate<Object> isNull = v -> isNull(v);
    boolean changed = set.removeIf(isNull);

    assertTrue(changed);
    assertEquals(1, set.size());
    assertTrue(set.contains(keep));
    assertFalse(set.contains(null));
  }

  @Test
  public void toStringOnEmptySetIsDeterministic() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    assertEquals("[]", set.toString());
  }

  @Test
  public void hashCodeOnEmptySetDoesNotThrow() {
    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
    // Not asserting exact value to avoid coupling to implementation
    set.hashCode();
  }

  @Test(expected = CloneNotSupportedException.class)
  public void cloneIsNotSupported() throws CloneNotSupportedException {
    HashCodeAndEqualsSafeSet.of("a").clone();
  }

  // ---------------------------------------------------------------------------
  // Sanity checks for containsAll/addAll with regular collections
  // ---------------------------------------------------------------------------

  @Test
  public void containsAllWithEmptyCollectionIsAlwaysTrue() {
    HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
    assertTrue(set.containsAll(new LinkedList<>()));
  }

  @Test
  public void addAllWithNonEmptyCollectionAddsElements() {
    Vector<Object> src = new Vector<>();
    src.add("x");
    src.add("y");

    HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);

    assertTrue(set.addAll(src));
    assertTrue(set.contains("x"));
    assertTrue(set.contains("y"));
    assertEquals(2, set.size());
  }
}