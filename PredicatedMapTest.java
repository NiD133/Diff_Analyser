package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the {@link PredicatedMap} implementation.
 * This class tests the behavior of PredicatedMap, ensuring that
 * keys and values adhere to specified predicates.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    // Predicates for testing
    protected static final Predicate<Object> TRUE_PREDICATE = TruePredicate.<Object>truePredicate();
    protected static final Predicate<Object> STRING_INSTANCE_PREDICATE = String.class::isInstance;

    /**
     * Decorates a map with key and value predicates.
     *
     * @param map the map to decorate
     * @param keyPredicate the predicate for keys
     * @param valuePredicate the predicate for values
     * @return a predicated map
     */
    protected IterableMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate,
                                            final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), TRUE_PREDICATE, TRUE_PREDICATE);
    }

    /**
     * Creates a test map with string predicates for both keys and values.
     *
     * @return a predicated map
     */
    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), STRING_INSTANCE_PREDICATE, STRING_INSTANCE_PREDICATE);
    }

    /**
     * Tests that the entry set of a predicated map is not null and behaves as expected.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testEntrySet() {
        Map<K, V> map = makeTestMap();
        assertNotNull(map.entrySet(), "Entry set should not be null");

        map = decorateMap(new HashMap<>(), null, null);
        map.put((K) "oneKey", (V) "oneValue");
        assertEquals(1, map.entrySet().size(), "Entry set should contain one entry");
    }

    /**
     * Tests the put and putAll methods of a predicated map, ensuring that
     * invalid keys and values throw IllegalArgumentException.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testPut() {
        final Map<K, V> map = makeTestMap();

        // Test putting invalid value
        assertThrows(IllegalArgumentException.class, () -> map.put((K) "Hi", (V) Integer.valueOf(3)),
                "Putting an invalid value should raise IllegalArgumentException");

        // Test putting invalid key
        assertThrows(IllegalArgumentException.class, () -> map.put((K) Integer.valueOf(3), (V) "Hi"),
                "Putting an invalid key should raise IllegalArgumentException");

        // Ensure invalid key/value are not present
        assertFalse(map.containsKey(Integer.valueOf(3)));
        assertFalse(map.containsValue(Integer.valueOf(3)));

        // Prepare another map with mixed valid and invalid entries
        final Map<K, V> mixedMap = new HashMap<>();
        mixedMap.put((K) "A", (V) "a");
        mixedMap.put((K) "B", (V) "b");
        mixedMap.put((K) "C", (V) "c");
        mixedMap.put((K) "c", (V) Integer.valueOf(3));

        // Test putAll with invalid entries
        assertThrows(IllegalArgumentException.class, () -> map.putAll(mixedMap),
                "Putting invalid entries should raise IllegalArgumentException");

        // Test setting a valid value
        map.put((K) "E", (V) "e");

        // Test setting an invalid value via entry set
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();
        final Map.Entry<K, V> finalEntry = entry;
        assertThrows(IllegalArgumentException.class, () -> finalEntry.setValue((V) Integer.valueOf(3)),
                "Setting an invalid value should raise IllegalArgumentException");

        // Test setting a valid value via entry set
        map.put((K) "F", (V) "f");
        iterator = map.entrySet().iterator();
        entry = iterator.next();
        entry.setValue((V) "x");
    }
}