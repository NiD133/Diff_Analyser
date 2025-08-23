package com.google.gson.internal;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}.
 */
public class LinkedTreeMapTest {

  /**
   * Verifies that {@code put} throws a {@link ClassCastException} when a non-comparable key is
   * inserted into a map that uses natural ordering. The default {@link LinkedTreeMap} constructor
   * creates such a map.
   */
  @Test
  public void put_nonComparableKey_throwsClassCastException() {
    // Arrange: Create a map that uses natural ordering for its keys.
    LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();
    Object nonComparableKey = new Object();

    // Act & Assert: Attempting to put a key that does not implement Comparable should fail.
    assertThrows(
        "Putting a non-Comparable key should throw ClassCastException",
        ClassCastException.class,
        () -> map.put(nonComparableKey, "any value"));
  }
}