/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PredicatedMap}.
 */
@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PredicatedMap_ESTest extends PredicatedMap_ESTest_scaffolding {

    // --- Helper Predicates for testing ---
    private static final Predicate<Object> ALWAYS_TRUE = TruePredicate.truePredicate();
    private static final Predicate<String> KEY_STARTS_WITH_A = key -> key != null && key.startsWith("A");
    private static final Predicate<Integer> VALUE_IS_POSITIVE = value -> value != null && value > 0;
    private static final Predicate<Object> ALWAYS_THROW_EXCEPTION = ExceptionPredicate.exceptionPredicate();

    // --- Constructor and Factory Method Tests ---

    /**
     * Tests that the factory method throws a NullPointerException if the map to decorate is null.
     */
    @Test(expected = NullPointerException.class)
    public void predicatedMap_shouldThrowNullPointerException_whenMapIsNull() {
        // Act
        PredicatedMap.predicatedMap(null, ALWAYS_TRUE, ALWAYS_TRUE);
    }

    /**
     * Tests that a map can be successfully decorated if its existing entries are all valid.
     */
    @Test
    public void predicatedMap_shouldSuccessfullyDecorateMapWithValidEntries() {
        // Arrange
        Map<String, Integer> initialMap = new HashMap<>();
        initialMap.put("A_Key", 1);
        initialMap.put("Another_Key", 10);

        Predicate<String> keyPredicate = key -> key.contains("_Key");
        Predicate<Integer> valuePredicate = value -> value > 0;

        // Act
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(initialMap, keyPredicate, valuePredicate);

        // Assert
        assertEquals("Map size should be preserved", 2, predicatedMap.size());
        assertTrue("Valid key should be present", predicatedMap.containsKey("A_Key"));
    }

    /**
     * Tests that decorating a map with an invalid key throws an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void predicatedMap_shouldThrowIllegalArgumentException_whenInitialKeyIsInvalid() {
        // Arrange
        Map<String, Integer> initialMap = new HashMap<>();
        initialMap.put("ValidKey", 1);
        initialMap.put("InvalidKey", 2); // This key will be rejected

        Predicate<String> keyPredicate = key -> key.equals("ValidKey");

        // Act: This should fail because "InvalidKey" does not match the predicate.
        PredicatedMap.predicatedMap(initialMap, keyPredicate, ALWAYS_TRUE);
    }

    /**
     * Tests that decorating a map with an invalid value throws an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void predicatedMap_shouldThrowIllegalArgumentException_whenInitialValueIsInvalid() {
        // Arrange
        Map<String, Integer> initialMap = new HashMap<>();
        initialMap.put("Key1", 10);
        initialMap.put("Key2", -5); // This value will be rejected

        // Act: This should fail because -5 is not a positive integer.
        PredicatedMap.predicatedMap(initialMap, ALWAYS_TRUE, VALUE_IS_POSITIVE);
    }

    /**
     * Tests that exceptions from a predicate during initial validation are propagated.
     */
    @Test(expected = RuntimeException.class)
    public void predicatedMap_shouldPropagateExceptionFromPredicate_duringInitialValidation() {
        // Arrange
        Map<Object, Object> initialMap = new HashMap<>();
        initialMap.put("key", "value");

        // Act: The ExceptionPredicate will throw a RuntimeException during validation.
        PredicatedMap.predicatedMap(initialMap, ALWAYS_THROW_EXCEPTION, ALWAYS_TRUE);
    }

    // --- put() Tests ---

    /**
     * Tests that put() succeeds when both the key and value are valid.
     */
    @Test
    public void put_shouldSucceed_whenKeyAndValueAreValid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), KEY_STARTS_WITH_A, VALUE_IS_POSITIVE);

        // Act
        predicatedMap.put("A_Key", 100);

        // Assert
        assertEquals(1, predicatedMap.size());
        assertEquals(Integer.valueOf(100), predicatedMap.get("A_Key"));
    }

    /**
     * Tests that put() throws an IllegalArgumentException if the key is rejected by the predicate.
     */
    @Test(expected = IllegalArgumentException.class)
    public void put_shouldThrowIllegalArgumentException_whenKeyIsInvalid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), KEY_STARTS_WITH_A, VALUE_IS_POSITIVE);

        // Act: "B_Key" does not start with "A" and should be rejected.
        predicatedMap.put("B_Key", 100);
    }

    /**
     * Tests that put() throws an IllegalArgumentException if the value is rejected by the predicate.
     */
    @Test(expected = IllegalArgumentException.class)
    public void put_shouldThrowIllegalArgumentException_whenValueIsInvalid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), KEY_STARTS_WITH_A, VALUE_IS_POSITIVE);

        // Act: -50 is not a positive integer and should be rejected.
        predicatedMap.put("A_Key", -50);
    }

    // --- putAll() Tests ---

    /**
     * Tests that putAll() succeeds when all entries in the source map are valid.
     */
    @Test
    public void putAll_shouldSucceed_whenAllEntriesAreValid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), KEY_STARTS_WITH_A, VALUE_IS_POSITIVE);
        Map<String, Integer> toAdd = new HashMap<>();
        toAdd.put("Apple", 1);
        toAdd.put("Ant", 2);

        // Act
        predicatedMap.putAll(toAdd);

        // Assert
        assertEquals(2, predicatedMap.size());
    }

    /**
     * Tests that putAll() throws an IllegalArgumentException if any key in the source map is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void putAll_shouldThrowIllegalArgumentException_whenAKeyIsInvalid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), KEY_STARTS_WITH_A, VALUE_IS_POSITIVE);
        Map<String, Integer> toAdd = new HashMap<>();
        toAdd.put("Apple", 1);
        toAdd.put("Banana", 2); // Invalid key

        // Act
        predicatedMap.putAll(toAdd);
    }

    // --- checkSetValue() Tests ---

    /**
     * Tests that checkSetValue() returns the value if it is valid.
     */
    @Test
    public void checkSetValue_shouldReturnSameValue_whenValueIsValid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), ALWAYS_TRUE, VALUE_IS_POSITIVE);

        // Act
        Integer checkedValue = predicatedMap.checkSetValue(10);

        // Assert
        assertEquals(Integer.valueOf(10), checkedValue);
    }

    /**
     * Tests that checkSetValue() throws an IllegalArgumentException if the value is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkSetValue_shouldThrowIllegalArgumentException_whenValueIsInvalid() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), ALWAYS_TRUE, VALUE_IS_POSITIVE);

        // Act: -10 is an invalid value.
        predicatedMap.checkSetValue(-10);
    }

    // --- isSetValueChecking() Tests ---

    /**
     * Tests that isSetValueChecking() returns true when a value predicate is configured.
     */
    @Test
    public void isSetValueChecking_shouldReturnTrue_whenValuePredicateIsSet() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), ALWAYS_TRUE, VALUE_IS_POSITIVE);

        // Act & Assert
        assertTrue(predicatedMap.isSetValueChecking());
    }

    /**
     * Tests that isSetValueChecking() returns false when the value predicate is null.
     */
    @Test
    public void isSetValueChecking_shouldReturnFalse_whenValuePredicateIsNull() {
        // Arrange
        PredicatedMap<String, Integer> predicatedMap = PredicatedMap.predicatedMap(new HashMap<>(), ALWAYS_TRUE, null);

        // Act & Assert
        assertFalse(predicatedMap.isSetValueChecking());
    }

    // --- Edge Case Tests ---

    /**
     * Tests for a StackOverflowError when a map is used as its own key and the predicate
     * triggers a recursive evaluation. This can happen with stateful predicates that
     * call hashCode() or equals() on the key, such as UniquePredicate.
     */
    @Test(expected = StackOverflowError.class)
    public void put_shouldThrowStackOverflowError_whenMapIsItsOwnKeyAndPredicateCausesRecursion() {
        // Arrange
        // Use raw types to allow the map to contain itself as a key.
        @SuppressWarnings({"rawtypes", "unchecked"})
        Map selfReferentialMap = new HashMap();
        Predicate<Object> recursivePredicate = new UniquePredicate<>();
        PredicatedMap<Object, Object> predicatedMap = PredicatedMap.predicatedMap(selfReferentialMap, recursivePredicate, ALWAYS_TRUE);

        // Act
        // This operation causes the predicate to evaluate the map itself.
        // UniquePredicate adds the key to an internal Set, which calls hashCode().
        // HashMap.hashCode() iterates its entries. Since the map is an entry's key,
        // this leads to infinite recursion.
        predicatedMap.put(predicatedMap, "some value");
    }
}