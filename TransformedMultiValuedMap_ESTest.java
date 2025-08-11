package org.apache.commons.collections4.multimap;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

/**
 * Test suite for TransformedMultiValuedMap functionality.
 * Tests key and value transformation behavior during map operations.
 */
public class TransformedMultiValuedMapTest {

    private MultiValuedMap<String, Integer> baseMap;
    private Transformer<String, String> upperCaseTransformer;
    private Transformer<Integer, Integer> doubleValueTransformer;
    private Transformer<Object, Object> nullTransformer;

    @Before
    public void setUp() {
        baseMap = new ArrayListValuedHashMap<>();
        upperCaseTransformer = input -> input != null ? input.toUpperCase() : null;
        doubleValueTransformer = input -> input != null ? input * 2 : null;
        nullTransformer = ConstantTransformer.nullTransformer();
    }

    @Test
    public void testTransformingMapCreation_WithValidParameters_ShouldCreateMap() {
        // Given: A base map and transformers
        
        // When: Creating a transforming map
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // Then: Map should be created successfully
        assertNotNull("Transformed map should not be null", transformedMap);
        assertTrue("Transformed map should be empty initially", transformedMap.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testTransformingMapCreation_WithNullBaseMap_ShouldThrowException() {
        // Given: Null base map
        
        // When: Creating transforming map with null base
        // Then: Should throw NullPointerException
        TransformedMultiValuedMap.transformingMap(null, upperCaseTransformer, doubleValueTransformer);
    }

    @Test
    public void testTransformedMapCreation_WithExistingData_ShouldTransformExistingEntries() {
        // Given: Base map with existing data
        baseMap.put("hello", 5);
        baseMap.put("world", 10);
        
        // When: Creating transformed map that transforms existing data
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformedMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // Then: Existing data should be transformed
        assertFalse("Map should not be empty", transformedMap.isEmpty());
        assertTrue("Should contain transformed key", transformedMap.containsKey("HELLO"));
        assertTrue("Should contain transformed value", transformedMap.get("HELLO").contains(10));
    }

    @Test
    public void testPutSingleValue_WithTransformers_ShouldTransformKeyAndValue() {
        // Given: A transforming map
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Adding a single key-value pair
        boolean result = transformedMap.put("test", 5);
        
        // Then: Key and value should be transformed
        assertTrue("Put operation should return true", result);
        assertTrue("Should contain transformed key", transformedMap.containsKey("TEST"));
        assertTrue("Should contain transformed value", transformedMap.get("TEST").contains(10));
        assertFalse("Should not contain original key", transformedMap.containsKey("test"));
    }

    @Test
    public void testPutAllFromIterable_WithTransformers_ShouldTransformAllValues() {
        // Given: A transforming map and collection of values
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        List<Integer> values = Arrays.asList(1, 2, 3);
        
        // When: Adding multiple values for a key
        boolean result = transformedMap.putAll("key", values);
        
        // Then: All values should be transformed
        assertTrue("PutAll operation should return true", result);
        Collection<Integer> storedValues = transformedMap.get("KEY");
        assertTrue("Should contain transformed values", storedValues.containsAll(Arrays.asList(2, 4, 6)));
        assertEquals("Should have correct number of values", 3, storedValues.size());
    }

    @Test
    public void testPutAllFromMap_WithTransformers_ShouldTransformAllEntries() {
        // Given: A transforming map and source map
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        Map<String, Integer> sourceMap = new HashMap<>();
        sourceMap.put("first", 10);
        sourceMap.put("second", 20);
        
        // When: Adding all entries from source map
        boolean result = transformedMap.putAll(sourceMap);
        
        // Then: All entries should be transformed
        assertTrue("PutAll operation should return true", result);
        assertTrue("Should contain transformed keys", transformedMap.containsKey("FIRST"));
        assertTrue("Should contain transformed keys", transformedMap.containsKey("SECOND"));
        assertTrue("Should contain transformed values", transformedMap.get("FIRST").contains(20));
        assertTrue("Should contain transformed values", transformedMap.get("SECOND").contains(40));
    }

    @Test
    public void testTransformKey_WithNullTransformer_ShouldReturnOriginalKey() {
        // Given: Map with null key transformer
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, null, doubleValueTransformer);
        
        // When: Transforming a key
        String result = transformedMap.transformKey("test");
        
        // Then: Original key should be returned
        assertEquals("Should return original key when transformer is null", "test", result);
    }

    @Test
    public void testTransformValue_WithNullTransformer_ShouldReturnOriginalValue() {
        // Given: Map with null value transformer
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, null);
        
        // When: Transforming a value
        Integer result = transformedMap.transformValue(42);
        
        // Then: Original value should be returned
        assertEquals("Should return original value when transformer is null", Integer.valueOf(42), result);
    }

    @Test
    public void testTransformValue_WithNullInput_ShouldHandleGracefully() {
        // Given: Map with transformers
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Transforming null value
        Integer result = transformedMap.transformValue(null);
        
        // Then: Should handle null gracefully
        assertNull("Should return null for null input", result);
    }

    @Test(expected = RuntimeException.class)
    public void testPut_WithExceptionThrowingTransformer_ShouldPropagateException() {
        // Given: Map with exception-throwing transformer
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, ExceptionTransformer.exceptionTransformer(), doubleValueTransformer);
        
        // When: Attempting to put value
        // Then: Should throw RuntimeException
        transformedMap.put("test", 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPut_WithUnmodifiableMap_ShouldThrowException() {
        // Given: Unmodifiable base map
        MultiValuedMap<String, Integer> unmodifiableMap = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(baseMap);
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            new TransformedMultiValuedMap<>(unmodifiableMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Attempting to put value
        // Then: Should throw UnsupportedOperationException
        transformedMap.put("test", 5);
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllFromIterable_WithNullIterable_ShouldThrowException() {
        // Given: Transforming map
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Attempting to put null iterable
        // Then: Should throw NullPointerException
        transformedMap.putAll("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllFromMap_WithNullMap_ShouldThrowException() {
        // Given: Transforming map
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Attempting to put null map
        // Then: Should throw NullPointerException
        transformedMap.putAll((Map<String, Integer>) null);
    }

    @Test
    public void testPutAll_WithEmptyCollections_ShouldReturnFalse() {
        // Given: Transforming map and empty collections
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Adding empty collections
        boolean resultFromIterable = transformedMap.putAll("key", Collections.emptyList());
        boolean resultFromMap = transformedMap.putAll(Collections.emptyMap());
        
        // Then: Should return false for empty collections
        assertFalse("Should return false for empty iterable", resultFromIterable);
        assertFalse("Should return false for empty map", resultFromMap);
    }

    @Test
    public void testPut_DuplicateValues_ShouldHandleCorrectly() {
        // Given: Set-based map (no duplicates) and List-based map (allows duplicates)
        MultiValuedMap<String, Integer> setBasedMap = new HashSetValuedHashMap<>();
        MultiValuedMap<String, Integer> listBasedMap = new ArrayListValuedHashMap<>();
        
        TransformedMultiValuedMap<String, Integer> setTransformedMap = 
            TransformedMultiValuedMap.transformingMap(setBasedMap, upperCaseTransformer, doubleValueTransformer);
        TransformedMultiValuedMap<String, Integer> listTransformedMap = 
            TransformedMultiValuedMap.transformingMap(listBasedMap, upperCaseTransformer, doubleValueTransformer);
        
        // When: Adding same value twice
        boolean firstAddToSet = setTransformedMap.put("key", 5);
        boolean secondAddToSet = setTransformedMap.put("key", 5);
        
        boolean firstAddToList = listTransformedMap.put("key", 5);
        boolean secondAddToList = listTransformedMap.put("key", 5);
        
        // Then: Set should reject duplicate, List should accept
        assertTrue("First add to set should succeed", firstAddToSet);
        assertFalse("Second add to set should fail (duplicate)", secondAddToSet);
        
        assertTrue("First add to list should succeed", firstAddToList);
        assertTrue("Second add to list should succeed (allows duplicates)", secondAddToList);
    }

    @Test
    public void testChainedTransformations_ShouldApplyInCorrectOrder() {
        // Given: Map with identity transformers (no-op)
        Transformer<String, String> identityKeyTransformer = NOPTransformer.nopTransformer();
        Transformer<Integer, Integer> identityValueTransformer = NOPTransformer.nopTransformer();
        
        TransformedMultiValuedMap<String, Integer> transformedMap = 
            TransformedMultiValuedMap.transformingMap(baseMap, identityKeyTransformer, identityValueTransformer);
        
        // When: Adding values
        transformedMap.put("test", 42);
        
        // Then: Values should remain unchanged
        assertTrue("Should contain original key", transformedMap.containsKey("test"));
        assertTrue("Should contain original value", transformedMap.get("test").contains(42));
    }
}