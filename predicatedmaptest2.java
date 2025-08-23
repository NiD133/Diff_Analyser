package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the validation aspects of PredicatedMap.
 * <p>
 * This class focuses on verifying that the map correctly throws exceptions
 * when predicate conditions are not met for keys or values.
 *
 * @param <K> the type of the keys in this map
 * @param <V> the type of the values in this map
 */
public class PredicatedMapValidationTest<K, V> extends AbstractIterableMapTest<K, V> {

    /** A predicate that accepts any object. */
    protected static final Predicate<Object> ACCEPT_ALL_PREDICATE = TruePredicate.truePredicate();

    /** A predicate that only accepts String instances. */
    protected static final Predicate<Object> STRING_ONLY_PREDICATE = String.class::isInstance;

    private Map<K, V> predicatedMap;

    @BeforeEach
    public void setUp() {
        predicatedMap = makeTestMap();
    }

    protected PredicatedMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate,
                                              final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates a map that allows any key or value. Used by the superclass tests.
     */
    @Override
    public PredicatedMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), ACCEPT_ALL_PREDICATE, ACCEPT_ALL_PREDICATE);
    }

    /**
     * Creates a map that only accepts Strings for keys and values.
     */
    public PredicatedMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), STRING_ONLY_PREDICATE, STRING_ONLY_PREDICATE);
    }

    // The following tests require casting and unchecked warnings because they
    // are designed to verify runtime validation, which intentionally bypasses
    // compile-time type safety.

    @Test
    @SuppressWarnings("unchecked")
    void put_shouldThrowException_whenKeyIsInvalid() {
        // Arrange
        final K invalidKey = (K) Integer.valueOf(123);
        final V validValue = (V) "validValue";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.put(invalidKey, validValue),
                "An invalid key should cause an IllegalArgumentException.");
        assertFalse(predicatedMap.containsKey(invalidKey), "Map should not contain the invalid key after the failed put.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void put_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        final K validKey = (K) "validKey";
        final V invalidValue = (V) Integer.valueOf(456);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.put(validKey, invalidValue),
                "An invalid value should cause an IllegalArgumentException.");
        assertFalse(predicatedMap.containsValue(invalidValue), "Map should not contain the invalid value after the failed put.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void putAll_shouldThrowException_whenMapContainsInvalidValue() {
        // Arrange
        final Map<K, V> mapWithInvalidValue = new HashMap<>();
        mapWithInvalidValue.put((K) "A", (V) "a");
        mapWithInvalidValue.put((K) "B", (V) Integer.valueOf(123)); // Invalid value

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> predicatedMap.putAll(mapWithInvalidValue),
                "putAll with an invalid value should cause an IllegalArgumentException.");
        assertTrue(predicatedMap.isEmpty(), "Map should remain empty after a failed putAll operation.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void entrySetValue_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        final K key = (K) "key";
        final V originalValue = (V) "originalValue";
        predicatedMap.put(key, originalValue);

        final Map.Entry<K, V> entry = predicatedMap.entrySet().iterator().next();
        final V invalidValue = (V) Integer.valueOf(789);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> entry.setValue(invalidValue),
                "Setting an invalid value on an entry should cause an IllegalArgumentException.");
        assertEquals(originalValue, predicatedMap.get(key), "Value should not be changed after a failed setValue call.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void entrySetValue_shouldSucceed_whenValueIsValid() {
        // Arrange
        final K key = (K) "key";
        predicatedMap.put(key, (V) "originalValue");

        final Map.Entry<K, V> entry = predicatedMap.entrySet().iterator().next();
        final V newValue = (V) "newValue";

        // Act
        entry.setValue(newValue);

        // Assert
        assertEquals(newValue, predicatedMap.get(key), "Map should be updated with the new value.");
    }
}