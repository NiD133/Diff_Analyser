package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.EqualPredicate;

/**
 * Test suite for PredicatedMap functionality.
 * PredicatedMap validates that keys and values added to the map match specified predicates.
 */
public class PredicatedMapTest {

    // Test predicates for reuse
    private static final Predicate<String> NOT_NULL_STRING = NotNullPredicate.notNullPredicate();
    private static final Predicate<Integer> NOT_NULL_INTEGER = NotNullPredicate.notNullPredicate();
    private static final Predicate<Object> ALWAYS_TRUE = TruePredicate.truePredicate();

    @Test
    public void testFactoryMethodWithValidMap() {
        // Given: A regular HashMap
        Map<String, Integer> baseMap = new HashMap<>();
        
        // When: Creating a PredicatedMap with non-null predicates
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        // Then: Map should be created successfully
        assertNotNull(predicatedMap);
        assertTrue(predicatedMap.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testFactoryMethodWithNullMap() {
        // When: Trying to create PredicatedMap with null base map
        // Then: Should throw NullPointerException
        PredicatedMap.predicatedMap(null, NOT_NULL_STRING, NOT_NULL_INTEGER);
    }

    @Test
    public void testPutWithValidKeyAndValue() {
        // Given: A PredicatedMap that accepts non-null strings and integers
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        // When: Adding valid key-value pair
        Integer result = predicatedMap.put("validKey", 42);
        
        // Then: Should succeed
        assertNull(result); // No previous value
        assertEquals(1, predicatedMap.size());
        assertEquals(Integer.valueOf(42), predicatedMap.get("validKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithInvalidKey() {
        // Given: A PredicatedMap that rejects null keys
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        // When: Trying to add null key
        // Then: Should throw IllegalArgumentException
        predicatedMap.put(null, 42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithInvalidValue() {
        // Given: A PredicatedMap that rejects null values
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        // When: Trying to add null value
        // Then: Should throw IllegalArgumentException
        predicatedMap.put("validKey", null);
    }

    @Test
    public void testPutAllWithValidEntries() {
        // Given: A PredicatedMap and a map with valid entries
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        Map<String, Integer> validEntries = new HashMap<>();
        validEntries.put("key1", 1);
        validEntries.put("key2", 2);
        
        // When: Adding all valid entries
        predicatedMap.putAll(validEntries);
        
        // Then: All entries should be added
        assertEquals(2, predicatedMap.size());
        assertEquals(Integer.valueOf(1), predicatedMap.get("key1"));
        assertEquals(Integer.valueOf(2), predicatedMap.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllWithInvalidEntries() {
        // Given: A PredicatedMap and a map containing invalid entries
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        Map<String, Integer> invalidEntries = new HashMap<>();
        invalidEntries.put("validKey", 1);
        invalidEntries.put(null, 2); // Invalid null key
        
        // When: Trying to add entries with invalid key
        // Then: Should throw IllegalArgumentException
        predicatedMap.putAll(invalidEntries);
    }

    @Test
    public void testConstructorValidatesExistingEntries() {
        // Given: A map with existing entries
        Map<String, Integer> baseMap = new HashMap<>();
        baseMap.put("existingKey", 100);
        
        // When: Creating PredicatedMap with predicates that accept the existing entries
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, NOT_NULL_INTEGER);
        
        // Then: Should succeed and contain the existing entry
        assertEquals(1, predicatedMap.size());
        assertEquals(Integer.valueOf(100), predicatedMap.get("existingKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsInvalidExistingEntries() {
        // Given: A map with an entry that will be rejected by predicate
        Map<String, Integer> baseMap = new HashMap<>();
        baseMap.put("key", 42);
        
        // Create a predicate that only accepts the value 100
        Predicate<Integer> restrictivePredicate = EqualPredicate.equalPredicate(100);
        
        // When: Creating PredicatedMap with restrictive predicate
        // Then: Should throw IllegalArgumentException because existing value (42) is rejected
        PredicatedMap.predicatedMap(baseMap, ALWAYS_TRUE, restrictivePredicate);
    }

    @Test
    public void testIsSetValueCheckingWithValuePredicate() {
        // Given: A PredicatedMap with a value predicate
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, ALWAYS_TRUE, NOT_NULL_INTEGER);
        
        // When: Checking if set value checking is enabled
        boolean isChecking = predicatedMap.isSetValueChecking();
        
        // Then: Should return true because value predicate is present
        assertTrue(isChecking);
    }

    @Test
    public void testIsSetValueCheckingWithoutValuePredicate() {
        // Given: A PredicatedMap without a value predicate
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, NOT_NULL_STRING, null);
        
        // When: Checking if set value checking is enabled
        boolean isChecking = predicatedMap.isSetValueChecking();
        
        // Then: Should return false because no value predicate is present
        assertFalse(isChecking);
    }

    @Test
    public void testCheckSetValueWithValidValue() {
        // Given: A PredicatedMap with value predicate
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, ALWAYS_TRUE, NOT_NULL_INTEGER);
        
        // When: Checking a valid value
        Integer validValue = 42;
        Integer result = predicatedMap.checkSetValue(validValue);
        
        // Then: Should return the same value
        assertEquals(validValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSetValueWithInvalidValue() {
        // Given: A PredicatedMap with value predicate that rejects null
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, ALWAYS_TRUE, NOT_NULL_INTEGER);
        
        // When: Checking an invalid (null) value
        // Then: Should throw IllegalArgumentException
        predicatedMap.checkSetValue(null);
    }

    @Test
    public void testWithNullPredicatesAllowsAnyValue() {
        // Given: A PredicatedMap with null predicates (no validation)
        Map<String, Integer> baseMap = new HashMap<>();
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(
            baseMap, null, null);
        
        // When: Adding entries with null key and value
        predicatedMap.put(null, null);
        
        // Then: Should succeed because no validation is performed
        assertEquals(1, predicatedMap.size());
        assertNull(predicatedMap.get(null));
    }
}