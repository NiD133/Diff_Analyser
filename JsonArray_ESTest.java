package com.google.gson;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Readable unit tests for JsonArray focusing on documented behavior:
 * - construction, size, isEmpty
 * - add, set, remove, contains
 * - iteration order
 * - equals, hashCode, deepCopy
 * - asList view (live and null-rejecting)
 * - single-element getters: success cases, IllegalState preconditions, and exception propagation
 * - bounds and null argument validation
 */
public class JsonArrayTest {

  // Construction, size, isEmpty

  @Test
  public void emptyArray_hasSizeZero_andIsEmpty() {
    JsonArray arr = new JsonArray();
    assertEquals(0, arr.size());
    assertTrue(arr.isEmpty());
  }

  @Test
  public void capacityConstructor_acceptsNonNegative() {
    JsonArray arr = new JsonArray(10);
    assertEquals(0, arr.size());
    assertTrue(arr.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void capacityConstructor_rejectsNegative() {
    new JsonArray(-1);
  }

  // Add, set, remove, contains

  @Test
  public void addNumber_increasesSize_andStoresPrimitive() {
    JsonArray arr = new JsonArray();
    arr.add(1.5);
    assertEquals(1, arr.size());
    assertTrue(arr.get(0).isJsonPrimitive());
  }

  @Test
  public void addNullNumber_isStoredAsJsonNull() {
    JsonArray arr = new JsonArray();
    arr.add((Number) null); // per docs: null becomes JsonNull
    assertEquals(1, arr.size());
    assertTrue(arr.get(0).isJsonNull());
  }

  @Test
  public void set_replacesElement_andReturnsPrevious() {
    JsonArray arr = new JsonArray();
    arr.add(1);
    JsonElement previous = arr.set(0, (JsonElement) JsonNull.INSTANCE);
    assertTrue(previous.isJsonPrimitive());
    assertTrue(arr.get(0).isJsonNull());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void set_outOfBounds_throws() {
    JsonArray arr = new JsonArray();
    arr.set(1, new JsonPrimitive(42));
  }

  @Test
  public void removeByElement_removesAndReturnsTrue() {
    JsonArray arr = new JsonArray();
    JsonElement e = new JsonPrimitive(123);
    arr.add(e);
    assertTrue(arr.remove(e));
    assertEquals(0, arr.size());
  }

  @Test
  public void removeByElement_notPresent_returnsFalse() {
    JsonArray arr = new JsonArray();
    assertFalse(arr.remove(new JsonPrimitive(999)));
  }

  @Test
  public void removeByIndex_returnsElement_andShiftsRemaining() {
    JsonArray arr = new JsonArray();
    arr.add("a");
    arr.add("b");
    JsonElement removed = arr.remove(0);
    assertEquals("a", removed.getAsString());
    assertEquals(1, arr.size());
    assertEquals("b", arr.get(0).getAsString());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void removeByIndex_outOfBounds_throws() {
    JsonArray arr = new JsonArray();
    arr.remove(0);
  }

  @Test
  public void contains_respectsEquality() {
    JsonArray arr = new JsonArray();
    arr.add(new JsonPrimitive(7));
    assertTrue(arr.contains(new JsonPrimitive(7)));
    assertFalse(arr.contains(new JsonPrimitive(8)));
  }

  // Iterator and order

  @Test
  public void iterator_iteratesInInsertionOrder() {
    JsonArray arr = new JsonArray();
    arr.add("x");
    arr.add("y");
    arr.add("z");

    List<String> seen = new ArrayList<>();
    for (JsonElement e : arr) {
      seen.add(e.getAsString());
    }
    assertEquals(List.of("x", "y", "z"), seen);
  }

  @Test
  public void iterator_notNull() {
    JsonArray arr = new JsonArray();
    Iterator<JsonElement> it = arr.iterator();
    assertNotNull(it);
  }

  // get and bounds

  @Test
  public void get_returnsElementAtIndex() {
    JsonArray arr = new JsonArray();
    arr.add("hello");
    assertEquals("hello", arr.get(0).getAsString());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_negativeIndex_throws() {
    JsonArray arr = new JsonArray();
    arr.get(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void get_indexTooLarge_throws() {
    JsonArray arr = new JsonArray();
    arr.get(0);
  }

  // equals, hashCode, deepCopy

  @Test
  public void equals_and_hashCode_basicSemantics() {
    JsonArray a = new JsonArray();
    JsonArray b = new JsonArray();
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());

    a.add(1);
    assertNotEquals(a, b);
    assertNotEquals(a.hashCode(), b.hashCode());

    assertFalse(a.equals(null));
    assertTrue(a.equals(a)); // reflexive
  }

  @Test
  public void deepCopy_producesEqualButIndependentInstance() {
    JsonArray original = new JsonArray();
    original.add("value");

    JsonArray copy = original.deepCopy();
    assertEquals(original, copy);
    assertNotSame(original, copy);

    // Mutating copy does not affect original
    copy.add("new");
    assertEquals(1, original.size());
    assertEquals(2, copy.size());
    assertNotEquals(original, copy);
  }

  // asList view

  @Test
  public void asList_isLiveAndBiDirectional() {
    JsonArray arr = new JsonArray();
    arr.add(1);

    List<JsonElement> view = arr.asList();
    assertEquals(1, view.size());

    // Mutate via view -> reflected in JsonArray
    view.add(new JsonPrimitive(2));
    assertEquals(2, arr.size());
    assertEquals(2, arr.get(1).getAsInt());

    // Mutate via JsonArray -> reflected in view
    arr.remove(0);
    assertEquals(1, view.size());
    assertEquals(2, view.get(0).getAsInt());
  }

  @Test(expected = NullPointerException.class)
  public void asList_rejectsNullElements() {
    JsonArray arr = new JsonArray();
    arr.asList().add(null);
  }

  // addAll

  @Test
  public void addAll_copiesElementsAndPreservesOrder() {
    JsonArray src = new JsonArray();
    src.add("a");
    src.add("b");
    JsonArray dest = new JsonArray();

    dest.addAll(src);
    assertEquals(2, dest.size());
    assertEquals("a", dest.get(0).getAsString());
    assertEquals("b", dest.get(1).getAsString());
  }

  @Test(expected = NullPointerException.class)
  public void addAll_nullArray_throws() {
    JsonArray dest = new JsonArray();
    dest.addAll(null);
  }

  // Single-element getters: success cases

  @Test
  public void singleElementGetters_delegateToElement() {
    JsonArray arr = new JsonArray();
    arr.add(Character.valueOf('6'));

    // These should parse the single element appropriately
    assertEquals(6, arr.getAsInt());
    assertEquals(6L, arr.getAsLong());
    assertEquals(6.0f, arr.getAsFloat(), 0.0001f);
    assertEquals(6.0, arr.getAsDouble(), 0.0001);
    assertEquals((byte) 6, arr.getAsByte());
    assertEquals((short) 6, arr.getAsShort());
    assertEquals("6", arr.getAsString());
    assertEquals('6', arr.getAsCharacter());
  }

  // Single-element getters: preconditions (size must be exactly 1)

  @Test(expected = IllegalStateException.class)
  public void singleElementGetter_whenEmpty_throwsIllegalState() {
    new JsonArray().getAsString();
  }

  @Test(expected = IllegalStateException.class)
  public void singleElementGetter_whenMoreThanOne_throwsIllegalState() {
    JsonArray arr = new JsonArray();
    arr.add(1);
    arr.add(2);
    arr.getAsInt();
  }

  // Single-element getters: propagate element exceptions

  @Test(expected = NumberFormatException.class)
  public void singleElementGetter_invalidConversion_propagatesException() {
    JsonArray arr = new JsonArray();
    arr.add(Character.valueOf('@')); // not a number
    arr.getAsShort(); // should propagate NumberFormatException
  }

  @Test(expected = UnsupportedOperationException.class)
  public void singleElementGetter_onJsonNull_throwsUnsupportedOperation() {
    JsonArray arr = new JsonArray();
    arr.add((Number) null); // becomes JsonNull
    arr.getAsInt();
  }
}