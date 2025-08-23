package com.google.gson.internal;

import org.junit.Test;

/**
 * This test class contains tests for the {@link LinkedTreeMap} class.
 * This particular test focuses on the behavior of the find() method.
 */
public class LinkedTreeMapFindTest {

    /**
     * Verifies that find() throws a NullPointerException when called with a null key
     * on a map that uses the natural order comparator. The natural order comparator
     * does not support null keys, as it would lead to calling compareTo() on a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void findWithNullKeyOnNaturalOrderMapThrowsNullPointerException() {
        // Arrange: Create a map that uses natural ordering and add an element.
        // An element must be present to ensure the comparison logic inside find() is executed.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        map.put(10, "some value");

        // Act & Assert: Attempting to find a null key should trigger a NullPointerException
        // because the natural order comparator will try to call compareTo(..) on null.
        // The 'create' flag is set to true, consistent with the original test's scenario.
        map.find(null, true);
    }
}