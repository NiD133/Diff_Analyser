package org.apache.commons.collections4.map;

import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link PredicatedMap} class.
 */
public class PredicatedMapTest {

    /**
     * Tests that calling putAll() with an empty map on an already empty
     * PredicatedMap results in the map remaining empty.
     */
    @Test
    public void putAllWithEmptyMapOnEmptyMapShouldRemainEmpty() {
        // Arrange
        final Map<String, String> initialMap = new HashMap<>();
        
        // Use simple, always-passing predicates, as the predicate logic is not the focus of this test.
        final PredicatedMap<String, String> predicatedMap =
                new PredicatedMap<>(initialMap, TruePredicate.truePredicate(), TruePredicate.truePredicate());

        // Sanity check to ensure the map starts empty.
        assertTrue("PredicatedMap should be empty before the operation.", predicatedMap.isEmpty());

        // Act
        // Call putAll with an empty map (in this case, the map itself).
        predicatedMap.putAll(predicatedMap);

        // Assert
        assertTrue("PredicatedMap should remain empty after calling putAll with an empty map.", predicatedMap.isEmpty());
        assertEquals("Map size should still be 0.", 0, predicatedMap.size());
    }
}