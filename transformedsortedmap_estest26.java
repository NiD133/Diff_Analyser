package org.apache.commons.collections4.map;

import static org.junit.Assert.assertThrows;

import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;

/**
 * This class contains tests for the {@link TransformedSortedMap}.
 * This specific test focuses on the behavior of the firstKey() method.
 */
public class TransformedSortedMap_ESTestTest26 {

    /**
     * Tests that calling firstKey() on an empty TransformedSortedMap
     * throws a NoSuchElementException, as per the SortedMap contract.
     */
    @Test
    public void firstKeyOnEmptyMapShouldThrowNoSuchElementException() {
        // Arrange: Create a TransformedSortedMap that decorates an empty TreeMap.
        // The key and value transformers are not relevant for this test, so they are null.
        final SortedMap<Object, Integer> emptyMap = new TreeMap<>();
        final SortedMap<Object, Integer> transformedMap =
                TransformedSortedMap.transformedSortedMap(emptyMap, null, null);

        // Act & Assert: Verify that calling firstKey() throws the expected exception.
        assertThrows(NoSuchElementException.class, () -> {
            transformedMap.firstKey();
        });
    }
}