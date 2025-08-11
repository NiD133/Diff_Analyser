package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;

/**
 * Test suite for TransformedSortedMap functionality.
 * Tests the behavior of sorted maps that transform keys and values during operations.
 */
public class TransformedSortedMapTest {

    // Test Data Constants
    private static final Integer KEY_NEGATIVE = -3410;
    private static final Integer KEY_POSITIVE = 3005;
    private static final Integer VALUE_850 = 850;
    private static final Integer VALUE_784 = 784;

    // Helper Methods
    private TreeMap<Integer, Integer> createEmptyTreeMap() {
        return new TreeMap<>();
    }

    private TreeMap<Integer, Integer> createTreeMapWithEntry(Integer key, Integer value) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(key, value);
        return map;
    }

    // Factory Method Tests
    
    @Test
    public void testTransformingSortedMap_WithEmptyMap_ReturnsEmptySubMap() {
        // Given: An empty TreeMap with chained transformer (no transformations)
        TreeMap<Integer, Integer> sourceMap = createEmptyTreeMap();
        Transformer<Integer, Integer> noOpTransformer = ChainedTransformer.chainedTransformer(new LinkedList<>());
        
        // When: Creating a transformed map and getting a submap
        TransformedSortedMap<Integer, Integer> transformedMap = 
            new TransformedSortedMap<>(sourceMap, null, noOpTransformer);
        SortedMap<Integer, Integer> subMap = transformedMap.subMap(KEY_NEGATIVE, KEY_NEGATIVE);
        
        // Then: The submap should be empty
        assertTrue("SubMap of empty range should be empty", subMap.isEmpty());
    }

    @Test
    public void testTransformedSortedMap_WithFactoryTransformer_ReturnsEmptyHeadMap() {
        // Given: A TreeMap with factory transformer that creates constant values
        TreeMap<Integer, Integer> sourceMap = createEmptyTreeMap();
        Transformer<Object, Integer> constantFactory = new FactoryTransformer<>(
            ConstantFactory.constantFactory(KEY_POSITIVE)
        );
        Transformer<Object, Integer> keyTransformer = new InvokerTransformer<>(
            "toString", new Class[0], new Object[0]
        );
        
        // When: Creating transformed map and getting head map
        TransformedSortedMap<Integer, Integer> transformedMap = 
            TransformedSortedMap.transformedSortedMap(sourceMap, keyTransformer, constantFactory);
        SortedMap<Integer, Integer> headMap = transformedMap.headMap(KEY_POSITIVE);
        
        // Then: Head map should be empty (no elements less than the key)
        assertTrue("HeadMap should be empty when no elements exist", headMap.isEmpty());
    }

    @Test
    public void testTransformedSortedMap_WithConstantTransformer_ReturnsEmptyTailMap() {
        // Given: A TreeMap with constant transformers
        TreeMap<Integer, Object> sourceMap = new TreeMap<>();
        Transformer<Integer, Integer> constantTransformer = new ConstantTransformer<>(KEY_NEGATIVE);
        Transformer<Object, Integer> invokerTransformer = new InvokerTransformer<>(
            null, new Class[0], null
        );
        
        // When: Creating transformed map and getting tail map
        TransformedSortedMap<Integer, Object> transformedMap = 
            TransformedSortedMap.transformedSortedMap(sourceMap, constantTransformer, invokerTransformer);
        SortedMap<Integer, Object> tailMap = transformedMap.tailMap(KEY_NEGATIVE);
        
        // Then: Tail map should be empty
        assertTrue("TailMap should be empty when no elements exist", tailMap.isEmpty());
    }

    // Navigation Method Tests

    @Test
    public void testTailMap_WithExistingElements_ReturnsNonEmptyMap() {
        // Given: A TreeMap with one entry and a constant transformer
        TreeMap<Integer, Integer> sourceMap = createTreeMapWithEntry(VALUE_850, VALUE_850);
        Transformer<Object, Integer> constantTransformer = ConstantTransformer.constantTransformer(VALUE_850);
        
        // When: Creating transformed map and getting tail map from the existing key
        TransformedSortedMap<Integer, Integer> transformedMap = 
            TransformedSortedMap.transformingSortedMap(sourceMap, null, constantTransformer);
        SortedMap<Integer, Integer> tailMap = transformedMap.tailMap(VALUE_850);
        
        // Then: Tail map should contain the element
        assertFalse("TailMap should contain elements when they exist", tailMap.isEmpty());
    }

    @Test
    public void testSubMap_WithExistingElements_ReturnsNonEmptyMap() {
        // Given: A TreeMap with entries in the specified range
        TreeMap<Integer, Integer> sourceMap = createTreeMapWithEntry(-1, 3152);
        Transformer<Integer, Integer> constantTransformer = new ConstantTransformer<>(-1);
        
        // When: Creating transformed map and getting submap
        TransformedSortedMap<Integer, Integer> transformedMap = 
            new TransformedSortedMap<>(sourceMap, constantTransformer, constantTransformer);
        SortedMap<Integer, Integer> subMap = transformedMap.subMap(-1, 3152);
        
        // Then: SubMap should contain elements in range
        assertFalse("SubMap should contain elements in the specified range", subMap.isEmpty());
    }

    @Test
    public void testHeadMap_WithExistingElements_ReturnsCorrectSize() {
        // Given: A TreeMap with one entry and chained transformer
        TreeMap<Integer, Integer> sourceMap = createTreeMapWithEntry(-17, -17);
        Transformer<Integer, Integer> chainedTransformer = ChainedTransformer.chainedTransformer(new LinkedList<>());
        
        // When: Creating transformed map and getting head map
        TransformedSortedMap<Integer, Integer> transformedMap = 
            new TransformedSortedMap<>(sourceMap, chainedTransformer, chainedTransformer);
        SortedMap<Integer, Integer> headMap = transformedMap.headMap(0);
        
        // Then: Head map should contain one element (the negative key)
        assertEquals("HeadMap should contain one element less than 0", 1, headMap.size());
    }

    // Accessor Method Tests

    @Test
    public void testGetSortedMap_ReturnsUnderlyingMap() {
        // Given: A nested transformed sorted map structure
        TreeMap<String, Object> sourceMap = new TreeMap<>();
        Transformer<Object, String> invokerTransformer = new InvokerTransformer<>(
            "toString", new Class[0], new Object[0]
        );
        
        TransformedSortedMap<String, Object> innerMap = 
            new TransformedSortedMap<>(sourceMap, invokerTransformer, invokerTransformer);
        
        // When: Creating another transformed map wrapping the first
        TransformedSortedMap<String, Object> outerMap = 
            TransformedSortedMap.transformingSortedMap(innerMap, invokerTransformer, invokerTransformer);
        
        // Then: Should be able to access the underlying sorted map
        SortedMap<String, Object> underlyingMap = outerMap.getSortedMap();
        assertEquals("Underlying map should be empty", 0, underlyingMap.size());
    }

    @Test
    public void testGetSortedMap_WithPopulatedMap_ReturnsCorrectSize() {
        // Given: A populated TreeMap with transformers
        TreeMap<Integer, Integer> sourceMap = createTreeMapWithEntry(VALUE_784, VALUE_784);
        Transformer<Integer, Integer> constantTransformer = ConstantTransformer.constantTransformer(VALUE_784);
        
        TransformedSortedMap<Integer, Integer> transformedMap = 
            new TransformedSortedMap<>(sourceMap, constantTransformer, constantTransformer);
        
        // When: Adding an element and wrapping in another transformed map
        transformedMap.put(VALUE_784, VALUE_784);
        TransformedSortedMap<Integer, Integer> wrappedMap = 
            TransformedSortedMap.transformedSortedMap(transformedMap, constantTransformer, constantTransformer);
        
        // Then: Underlying map should reflect the correct size
        SortedMap<Integer, Integer> underlyingMap = wrappedMap.getSortedMap();
        assertEquals("Underlying map should contain one element", 1, underlyingMap.size());
    }

    @Test
    public void testComparator_ReturnsNullForNaturalOrdering() {
        // Given: A TreeMap with natural ordering and map transformer
        TreeMap<Object, Object> sourceMap = new TreeMap<>();
        Transformer<Object, Object> mapTransformer = MapTransformer.mapTransformer(sourceMap);
        
        // When: Creating a transformed sorted map
        TransformedSortedMap<Object, Object> transformedMap = 
            TransformedSortedMap.transformedSortedMap(sourceMap, mapTransformer, mapTransformer);
        
        // Then: Comparator should be null (indicating natural ordering)
        assertNull("Comparator should be null for natural ordering", transformedMap.comparator());
    }

    // Error Condition Tests

    @Test(expected = NullPointerException.class)
    public void testTransformingSortedMap_WithNullMap_ThrowsException() {
        // Given: Null map parameter
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        
        // When: Attempting to create transformed map with null source
        // Then: Should throw NullPointerException
        TransformedSortedMap.transformingSortedMap(null, transformer, transformer);
    }

    @Test(expected = RuntimeException.class)
    public void testTransformedSortedMap_WithExceptionTransformer_ThrowsException() {
        // Given: A map with existing data and exception transformer
        TreeMap<Object, Integer> sourceMap = new TreeMap<>();
        sourceMap.put(-1, -1);
        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        
        // When: Creating transformed map with exception transformer
        // Then: Should throw RuntimeException during transformation
        TransformedSortedMap.transformedSortedMap(sourceMap, exceptionTransformer, exceptionTransformer);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullMap_ThrowsException() {
        // When: Creating TransformedSortedMap with null map
        // Then: Should throw NullPointerException
        new TransformedSortedMap<>(null, null, null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testFirstKey_OnEmptyMap_ThrowsException() {
        // Given: An empty TreeMap
        TreeMap<Object, Integer> emptyMap = new TreeMap<>();
        TransformedSortedMap<Object, Integer> transformedMap = 
            TransformedSortedMap.transformedSortedMap(emptyMap, null, null);
        
        // When: Attempting to get first key from empty map
        // Then: Should throw NoSuchElementException
        transformedMap.firstKey();
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastKey_OnEmptyMap_ThrowsException() {
        // Given: An empty TreeMap
        TreeMap<Object, Integer> emptyMap = new TreeMap<>();
        TransformedSortedMap<Object, Integer> transformedMap = 
            TransformedSortedMap.transformedSortedMap(emptyMap, null, null);
        
        // When: Attempting to get last key from empty map
        // Then: Should throw NoSuchElementException
        transformedMap.lastKey();
    }

    @Test(expected = NullPointerException.class)
    public void testHeadMap_WithNullKey_ThrowsException() {
        // Given: A TreeMap that doesn't allow null keys
        TreeMap<Object, Object> sourceMap = new TreeMap<>();
        TransformedSortedMap<Object, Object> transformedMap = 
            TransformedSortedMap.transformingSortedMap(sourceMap, null, null);
        
        // When: Attempting to get head map with null key
        // Then: Should throw NullPointerException
        transformedMap.headMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void testTailMap_WithNullKey_ThrowsException() {
        // Given: A TreeMap that doesn't allow null keys
        TreeMap<Object, Object> sourceMap = new TreeMap<>();
        TransformedSortedMap<Object, Object> transformedMap = 
            TransformedSortedMap.transformedSortedMap(sourceMap, null, null);
        
        // When: Attempting to get tail map with null key
        // Then: Should throw NullPointerException
        transformedMap.tailMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubMap_WithNullFromKey_ThrowsException() {
        // Given: A TreeMap that doesn't allow null keys
        TreeMap<Object, Object> sourceMap = new TreeMap<>();
        TransformedSortedMap<Object, Object> transformedMap = 
            TransformedSortedMap.transformingSortedMap(sourceMap, null, null);
        
        // When: Attempting to get sub map with null from key
        // Then: Should throw NullPointerException
        transformedMap.subMap(null, new Object());
    }

    // Custom Comparator Tests

    @Test
    public void testComparator_WithCustomComparator_ReturnsComparator() {
        // Given: A TreeMap with custom comparator
        Comparator<Integer> customComparator = Integer::compareTo;
        TreeMap<Integer, Integer> sourceMap = new TreeMap<>(customComparator);
        Transformer<Object, Integer> transformer = InvokerTransformer.invokerTransformer("toString");
        
        // When: Creating transformed sorted map
        TransformedSortedMap<Integer, Integer> transformedMap = 
            TransformedSortedMap.transformedSortedMap(sourceMap, transformer, transformer);
        
        // Then: Should return the custom comparator
        assertNotNull("Custom comparator should not be null", transformedMap.comparator());
    }
}