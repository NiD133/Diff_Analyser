package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for PredicatedMap focusing on the entrySet behavior.
 *
 * @param <K> the type of the keys in this map
 * @param <V> the type of the values in this map
 */
// The class was renamed from PredicatedMapTestTest1 for clarity.
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    protected static final Predicate<Object> truePredicate = TruePredicate.truePredicate();
    protected static final Predicate<Object> testPredicate = String.class::isInstance;

    protected IterableMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate, final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), truePredicate, truePredicate);
    }

    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), testPredicate, testPredicate);
    }

    @Nested
    @DisplayName("entrySet()")
    class EntrySetTests {

        @Test
        @DisplayName("on a new map should return a non-null, empty set")
        void onNewMapShouldReturnEmptyAndNonNullSet() {
            // Arrange
            final Map<K, V> map = makeTestMap();

            // Act
            final Set<Map.Entry<K, V>> entrySet = map.entrySet();

            // Assert
            assertNotNull(entrySet, "The entry set should not be null for a new map.");
            assertTrue(entrySet.isEmpty(), "The entry set should be empty for a new map.");
        }

        @Test
        @DisplayName("should reflect entries added to the map")
        @SuppressWarnings("unchecked")
        void shouldReflectAddedEntries() {
            // Arrange
            // Use null predicates to allow any key/value, simplifying the test's focus.
            final Map<K, V> map = decorateMap(new HashMap<>(), null, null);
            final K key = (K) "key1";
            final V value = (V) "value1";

            // Act
            map.put(key, value);

            // Assert
            assertEquals(1, map.entrySet().size(), "The entry set size should be 1 after adding one entry.");
            final Map.Entry<K, V> entry = map.entrySet().iterator().next();
            assertEquals(key, entry.getKey(), "The key in the entry set should match the added key.");
            assertEquals(value, entry.getValue(), "The value in the entry set should match the added value.");
        }
    }
}