package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for PredicatedMap.
 * These tests exercise the most important behaviors:
 * - construction and factory preconditions
 * - key/value validation on put/putAll
 * - validation of pre-existing entries on construction
 * - behavior of protected hooks: checkSetValue and isSetValueChecking
 * - behavior when predicates are null (no validation)
 */
public class PredicatedMapTest {

    // Helpers
    private static <T> Predicate<T> notNull() {
        return NotNullPredicate.notNullPredicate();
    }

    private static <T> Predicate<T> allowAll() {
        return TruePredicate.truePredicate();
    }

    private static <T> Predicate<T> allowOnlyNull() {
        return NullPredicate.nullPredicate();
    }

    // Construction / factory --------------------------------------------------

    @Test
    public void factoryRejectsNullMap() {
        try {
            PredicatedMap.predicatedMap(null, allowAll(), allowAll());
            fail("Expected NullPointerException for null map");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void constructorRejectsNullMap() {
        try {
            new PredicatedMap<Object, Object>(null, allowAll(), allowAll());
            fail("Expected NullPointerException for null map");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void constructorValidatesExistingEntries() {
        // Given a map that already contains an invalid entry (null value)
        Map<String, String> backing = new HashMap<>();
        backing.put("ok", null);

        // When decorating with not-null value predicate
        try {
            new PredicatedMap<>(backing, allowAll(), notNull());
            fail("Expected IllegalArgumentException for existing invalid value");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // isSetValueChecking / checkSetValue -------------------------------------

    @Test
    public void isSetValueCheckingIsTrueWhenValuePredicatePresent() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), notNull());
        assertTrue(map.isSetValueChecking());
    }

    @Test
    public void isSetValueCheckingIsFalseWhenNoValuePredicate() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), null);
        assertFalse(map.isSetValueChecking());
    }

    @Test
    public void checkSetValueReturnsValueWhenAccepted() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), notNull());

        String value = "v";
        assertSame(value, map.checkSetValue(value));
    }

    @Test
    public void checkSetValueAllowsNullWhenNoValuePredicate() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), null);

        assertNull(map.checkSetValue(null));
    }

    @Test
    public void checkSetValueThrowsWhenRejected() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), notNull());

        try {
            map.checkSetValue(null);
            fail("Expected IllegalArgumentException for rejected value");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // validate(key, value) ----------------------------------------------------

    @Test
    public void validateRejectsNullKeyWhenKeyPredicateIsNotNull() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), notNull(), allowAll());

        try {
            map.validate(null, "v");
            fail("Expected IllegalArgumentException for rejected key");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void validateRejectsNullValueWhenValuePredicateIsNotNull() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), notNull());

        try {
            map.validate("k", null);
            fail("Expected IllegalArgumentException for rejected value");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void validateAcceptsWhenBothPredicatesPass() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), notNull(), notNull());

        // Should not throw
        map.validate("k", "v");
    }

    // put / putAll ------------------------------------------------------------

    @Test
    public void putRejectsNullKeyWhenKeyPredicateIsNotNull() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), notNull(), allowAll());

        try {
            map.put(null, "v");
            fail("Expected IllegalArgumentException for rejected key");
        } catch (IllegalArgumentException expected) {
            // ok
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void putRejectsNullValueWhenValuePredicateIsNotNull() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowAll(), notNull());

        try {
            map.put("k", null);
            fail("Expected IllegalArgumentException for rejected value");
        } catch (IllegalArgumentException expected) {
            // ok
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void putAcceptsValidPair() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), notNull(), notNull());

        String previous = map.put("k", "v");
        assertNull(previous);
        assertEquals(1, map.size());
        assertEquals("v", map.get("k"));
    }

    @Test
    public void putAllValidatesAllEntriesAndDoesNotPartiallyApply() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), notNull(), notNull());

        Map<String, String> toCopy = new HashMap<>();
        toCopy.put("ok", "v");
        toCopy.put("badKey", null); // invalid value

        try {
            map.putAll(toCopy);
            fail("Expected IllegalArgumentException for rejected entry in putAll");
        } catch (IllegalArgumentException expected) {
            // ok
        }
        assertTrue("No entries should have been added on failure", map.isEmpty());
    }

    @Test
    public void nullPredicatesMeanNoValidation() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), null, null);

        // Both null key and null value are accepted when predicates are null
        map.put(null, null);
        assertTrue(map.containsKey(null));
        assertNull(map.get(null));
    }

    @Test
    public void allowOnlyNullPredicateAcceptsOnlyNulls() {
        PredicatedMap<String, String> map =
                new PredicatedMap<>(new HashMap<>(), allowOnlyNull(), allowOnlyNull());

        // Accepts null key/value
        map.put(null, null);
        assertTrue(map.containsKey(null));

        // Rejects non-null key
        try {
            map.put("k", null);
            fail("Expected IllegalArgumentException for non-null key");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        // Rejects non-null value
        try {
            map.put(null, "v");
            fail("Expected IllegalArgumentException for non-null value");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }
}