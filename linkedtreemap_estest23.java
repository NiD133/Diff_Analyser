package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link LinkedTreeMap} focusing on its handling of key comparability.
 */
public class LinkedTreeMapTest {

    /**
     * A simple helper class that does not implement {@link Comparable}, used to test
     * how LinkedTreeMap behaves with non-comparable keys.
     */
    private static class NonComparableKey {}

    @Test
    public void findWithNonComparableKeyThrowsClassCastException() {
        // Arrange: Create a LinkedTreeMap with its default constructor.
        // This configures the map to use natural ordering, which requires keys
        // to implement the Comparable interface.
        LinkedTreeMap<NonComparableKey, String> map = new LinkedTreeMap<>();
        NonComparableKey key = new NonComparableKey();

        // Act & Assert: Verify that calling find() with a non-comparable key
        // throws a ClassCastException, as the map will attempt to cast the key
        // to Comparable to perform a comparison.
        ClassCastException exception = assertThrows(
            ClassCastException.class,
            () -> map.find(key, true) // Attempt to find and create the key
        );

        // Optional: A more specific assertion on the exception message ensures
        // we're catching the expected error.
        assertTrue(
            "Exception message should indicate a casting problem to Comparable",
            exception.getMessage().contains("cannot be cast to java.lang.Comparable")
        );
    }
}