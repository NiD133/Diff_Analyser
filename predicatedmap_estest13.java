package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;

/**
 * Contains tests for edge cases in {@link PredicatedMap}.
 */
public class PredicatedMapTest {

    /**
     * Tests that a StackOverflowError is thrown when a map's predicate
     * triggers a recursive hashCode() calculation on a key.
     *
     * <p>This scenario is created by:
     * <ol>
     *   <li>Creating a map ('mapWithSelfAsKey') that contains itself as a key in its
     *       underlying decorated map.</li>
     *   <li>Using a predicate ('UniquePredicate') that invokes hashCode() on the elements
     *       it evaluates to check for uniqueness.</li>
     *   <li>Attempting to add 'mapWithSelfAsKey' as a key to another predicated map
     *       ('targetMap') that uses this same predicate.</li>
     * </ol>
     * The call to {@code targetMap.put()} triggers the predicate, which in turn calls
     * {@code hashCode()} on 'mapWithSelfAsKey'. The hash code of a map decorator is based
     * on its underlying map's contents. Since the underlying map contains the decorator
     * itself, this leads to an infinite recursion and a {@code StackOverflowError}.
     */
    @Test
    public void putWithSelfReferencingKeyInPredicateShouldCauseStackOverflow() {
        // Arrange: Create a predicate that will be shared between two maps.
        // The UniquePredicate relies on hashCode() to check for uniqueness.
        final Predicate<Object> uniquePredicate = new UniquePredicate<>();

        // Arrange: Create a map that will contain a reference to itself as a key.
        final Map<Object, String> underlyingMapForSelfReference = new HashMap<>();
        final PredicatedMap<Object, String> mapWithSelfAsKey =
                new PredicatedMap<>(underlyingMapForSelfReference, uniquePredicate, uniquePredicate);

        // This is the crucial step that creates the circular reference. The decorator
        // map is added as a key to the very map it decorates.
        underlyingMapForSelfReference.put(mapWithSelfAsKey, "a-value");

        // Arrange: Create the target map that will receive the self-referencing map.
        final Map<Object, Integer> targetUnderlyingMap = new HashMap<>();
        final PredicatedMap<Object, Integer> targetMap =
                PredicatedMap.predicatedMap(targetUnderlyingMap, uniquePredicate, uniquePredicate);

        // Act & Assert: Attempting to put the self-referencing map as a key
        // into another map using the same UniquePredicate. This triggers the
        // predicate, which calls hashCode() on the key, leading to a StackOverflowError.
        assertThrows(StackOverflowError.class, () -> {
            targetMap.put(mapWithSelfAsKey, 123);
        });
    }
}