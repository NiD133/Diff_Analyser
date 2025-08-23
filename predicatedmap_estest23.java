package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains tests for the {@link PredicatedMap} constructor.
 */
public class PredicatedMap_ESTestTest23 extends PredicatedMap_ESTest_scaffolding {

    /**
     * Tests that the PredicatedMap constructor throws a StackOverflowError when
     * attempting to decorate a map that contains itself as a key.
     * <p>
     * The constructor validates existing elements by iterating over the decorated map's
     * entry set. Standard map iterators may invoke hashCode() on keys, and for a map
     * containing itself, this leads to infinite recursion.
     */
    @Test(expected = StackOverflowError.class)
    public void constructorShouldThrowStackOverflowErrorWhenDecoratingARecursiveMap() {
        // Arrange: Create a map that contains itself as a key, forming a recursive structure.

        // 1. The map that will have a recursive key reference.
        final Map<Object, HashMap<Integer, Integer>> recursiveMap = new HashMap<>();

        // 2. A predicate for the value. Its specific logic is not essential for triggering
        //    the StackOverflowError but is required for the setup.
        final Predicate<Object> anyPredicate = new UniquePredicate<>();

        // 3. Decorate the map. The key predicate is null (no validation on keys).
        final PredicatedMap<Object, HashMap<Integer, Integer>> predicatedMap =
                new PredicatedMap<>(recursiveMap, null, anyPredicate);

        // 4. Create the recursive structure by putting the map into itself as a key.
        //    This operation is valid according to the map's predicates.
        predicatedMap.putIfAbsent(recursiveMap, new HashMap<>());

        // Act: Attempt to decorate the already-decorated map, which now has a recursive structure.
        // The PredicatedMap constructor iterates over the provided map's entries to validate them.
        // This iteration triggers a hashCode() call on the recursive key, leading to infinite
        // recursion and a StackOverflowError.
        new PredicatedMap<>(predicatedMap, anyPredicate, null);

        // Assert: The test expects a StackOverflowError, as declared in the @Test annotation.
    }
}