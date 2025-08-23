package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link PredicatedMap} focusing on recursive scenarios.
 */
public class PredicatedMapTest {

    /**
     * This test verifies the behavior of {@code putAll} when the source map
     * contains the {@code PredicatedMap} instance itself as a key.
     * <p>
     * This setup creates a circular dependency for hashCode calculation. When
     * {@code putAll} attempts to add the entry where the key is the map itself,
     * the underlying HashMap implementation calls {@code hashCode()} on the key.
     * The {@code PredicatedMap}'s hashCode implementation delegates to the
     * underlying map's hashCode, which in turn needs to compute the hashCode of
     * its keys, leading to an infinite recursion and a {@link StackOverflowError}.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void putAllWithSelfReferencingKeyInSourceShouldCauseStackOverflow() {
        // Arrange: Create a map that contains itself as a key.
        final Map<Object, Object> underlyingMap = new HashMap<>();
        final Predicate<Object> nonNullPredicate = NotNullPredicate.notNullPredicate();
        final PredicatedMap<Object, Object> predicatedMap =
            new PredicatedMap<>(underlyingMap, nonNullPredicate, nonNullPredicate);

        // Add a standard entry to the map.
        predicatedMap.put("anyKey", "anyValue");

        // Create the self-referencing condition by putting the decorated map
        // into its own underlying map. This is the crucial step that sets up
        // the recursive hashCode() call.
        underlyingMap.put(predicatedMap, "anotherValue");

        // Act: Attempt to add all entries from the underlying map back into the
        // predicated map. This will trigger the recursive hashCode calculation
        // when processing the self-referencing entry.
        predicatedMap.putAll(underlyingMap);
    }
}