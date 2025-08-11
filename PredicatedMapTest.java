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
 * Tests for {@link PredicatedMap}.
 *
 * The tests below configure a PredicatedMap that only accepts String keys and values.
 * To verify runtime predicate enforcement (beyond generics), we intentionally insert
 * non-String values via unchecked casts; those are isolated in small helper methods
 * to keep individual test bodies clear.
 *
 * @param <K> key type (left generic to allow inserting invalid types in tests)
 * @param <V> value type (left generic to allow inserting invalid types in tests)
 */
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    // Predicates used to decorate maps in tests.
    private static final Predicate<Object> ANY = TruePredicate.<Object>truePredicate();
    private static final Predicate<Object> STRING_ONLY = String.class::isInstance;

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        // A map that accepts any key/value
        return decorateMap(new HashMap<>(), ANY, ANY);
    }

    private IterableMap<K, V> makeStringOnlyMap() {
        // A map that only accepts String keys and values
        return decorateMap(new HashMap<>(), STRING_ONLY, STRING_ONLY);
    }

    protected IterableMap<K, V> decorateMap(final Map<K, V> map,
                                            final Predicate<? super K> keyPredicate,
                                            final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    // ---------- Tests for entrySet ----------

    @Test
    void entrySet_isNeverNull() {
        // Given a predicated map
        final Map<K, V> map = makeStringOnlyMap();

        // Then its entrySet must not be null
        assertNotNull(map.entrySet(), "Entry set should never be null.");
    }

    @Test
    void entrySet_reportsCorrectSize_withNullPredicates() {
        // Given a map decorated with no predicates
        Map<K, V> map = decorateMap(new HashMap<>(), null, null);

        // When we add one entry
        putRaw(map, "oneKey", "oneValue");

        // Then size should reflect that single entry
        assertEquals(1, map.entrySet().size(), "Entry set should contain exactly one entry.");

        // And re-decorating with null predicates again does not alter contents
        map = decorateMap(map, null, null);
        assertEquals(1, map.size(), "Re-decorating with null predicates must not change contents.");
    }

    // ---------- Tests for put / putAll predicate enforcement ----------

    @Test
    void put_rejectsInvalidValueType() {
        // Given a map that only accepts String keys and values
        final Map<K, V> map = makeStringOnlyMap();

        // When putting a non-String value
        // Then an IllegalArgumentException is thrown and nothing is added
        assertThrows(IllegalArgumentException.class,
                () -> putRaw(map, "Hi", Integer.valueOf(3)),
                "Non-String value should be rejected.");

        assertFalse(map.containsValue(Integer.valueOf(3)),
                "Rejected value must not be present in the map.");
    }

    @Test
    void put_rejectsInvalidKeyType() {
        // Given a map that only accepts String keys and values
        final Map<K, V> map = makeStringOnlyMap();

        // When putting a non-String key
        // Then an IllegalArgumentException is thrown and nothing is added
        assertThrows(IllegalArgumentException.class,
                () -> putRaw(map, Integer.valueOf(3), "Hi"),
                "Non-String key should be rejected.");

        assertFalse(map.containsKey(Integer.valueOf(3)),
                "Rejected key must not be present in the map.");
    }

    @Test
    void putAll_rejectsWhenAnyEntryIsInvalid() {
        // Given a map that only accepts String keys and values
        final Map<K, V> map = makeStringOnlyMap();

        // And a source map containing one invalid value
        final Map<K, V> source = newMapWithValidStringEntriesAndOneInvalid();

        // When copying all entries
        // Then an IllegalArgumentException is thrown and the operation is rejected
        assertThrows(IllegalArgumentException.class,
                () -> map.putAll(source),
                "putAll must fail when any entry violates predicates.");
    }

    // ---------- Tests for entry.setValue checking ----------

    @Test
    void entrySet_setValue_rejectsInvalidValueType() {
        // Given a map with one valid entry
        final Map<K, V> map = makeStringOnlyMap();
        putRaw(map, "E", "e");

        // And we obtain an entry
        final Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        final Map.Entry<K, V> entry = it.next();

        // When setting a non-String value through the entry
        // Then an IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class,
                () -> setEntryValueRaw(entry, Integer.valueOf(3)),
                "Non-String value should be rejected via setValue.");
    }

    @Test
    void entrySet_setValue_allowsValidValue() {
        // Given a map with two valid entries
        final Map<K, V> map = makeStringOnlyMap();
        putRaw(map, "E", "e");
        putRaw(map, "F", "f");

        // When updating the value for key "F" via its entry
        final Map.Entry<K, V> entryF = findEntryByStringKey(map, "F");
        setEntryValueRaw(entryF, "x");

        // Then the map must reflect the updated value
        assertEquals("x", map.get("F"), "Value for key 'F' should be updated to 'x'.");
    }

    // ---------- Helper methods to isolate unchecked casts ----------

    @SuppressWarnings("unchecked")
    private void putRaw(final Map<K, V> map, final Object rawKey, final Object rawValue) {
        map.put((K) rawKey, (V) rawValue);
    }

    @SuppressWarnings("unchecked")
    private void setEntryValueRaw(final Map.Entry<K, V> entry, final Object rawValue) {
        entry.setValue((V) rawValue);
    }

    private Map.Entry<K, V> findEntryByStringKey(final Map<K, V> map, final String key) {
        for (final Map.Entry<K, V> e : map.entrySet()) {
            if (key.equals(e.getKey())) {
                return e;
            }
        }
        throw new AssertionError("Expected to find entry with key: " + key);
    }

    private Map<K, V> newMapWithValidStringEntriesAndOneInvalid() {
        final Map<K, V> map = new HashMap<>();
        putRaw(map, "A", "a");
        putRaw(map, "B", "b");
        putRaw(map, "C", "c");
        // Deliberately invalid value (non-String) to trigger predicate failure
        putRaw(map, "c", Integer.valueOf(3));
        return map;
    }

//    // Uncomment to regenerate serialized forms if needed
//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/PredicatedMap.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/PredicatedMap.fullCollection.version4.obj");
//    }
}