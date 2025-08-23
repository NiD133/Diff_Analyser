package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Tests for {@link TransformedSortedMap}.
 * This class contains a test case focusing on the behavior of the lastKey() method.
 */
public class TransformedSortedMap_ESTestTest28 {

    /**
     * Tests that calling lastKey() on an empty TransformedSortedMap throws a NoSuchElementException.
     * This behavior is inherited from the underlying empty map.
     */
    @Test(expected = NoSuchElementException.class)
    public void lastKeyOnEmptyMapShouldThrowNoSuchElementException() {
        // Arrange: Create an empty sorted map and decorate it.
        // The transformers are irrelevant for the lastKey() method, so we can pass null.
        final SortedMap<String, String> emptyUnderlyingMap = new TreeMap<>();
        final SortedMap<String, String> transformedMap =
                TransformedSortedMap.transformedSortedMap(emptyUnderlyingMap, null, null);

        // Act: Attempt to get the last key from the empty map.
        // This call is expected to throw the exception specified in the @Test annotation.
        transformedMap.lastKey();

        // Assert: The test passes if a NoSuchElementException is thrown.
        // This is handled declaratively by the 'expected' attribute of the @Test annotation.
    }
}