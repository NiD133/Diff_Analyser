package org.apache.commons.collections4.properties;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Test suite for OrderedProperties class.
 * Tests the ordered key functionality and various Map operations.
 */
public class OrderedPropertiesTest {

    private OrderedProperties properties;

    @Before
    public void setUp() {
        properties = new OrderedProperties();
    }

    // ========== Basic Operations Tests ==========

    @Test
    public void testPutAndGet_ShouldStoreAndRetrieveValues() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        Object previousValue = properties.put(key, value);
        Object retrievedValue = properties.get(key);

        // Then
        assertNull("Should return null for new key", previousValue);
        assertEquals("Should retrieve the stored value", value, retrievedValue);
    }

    @Test
    public void testPutExistingKey_ShouldReturnPreviousValue() {
        // Given
        String key = "existingKey";
        String oldValue = "oldValue";
        String newValue = "newValue";
        properties.put(key, oldValue);

        // When
        Object previousValue = properties.put(key, newValue);

        // Then
        assertEquals("Should return previous value", oldValue, previousValue);
        assertEquals("Should store new value", newValue, properties.get(key));
    }

    @Test
    public void testRemove_ShouldRemoveKeyValuePair() {
        // Given
        String key = "keyToRemove";
        String value = "valueToRemove";
        properties.put(key, value);

        // When
        Object removedValue = properties.remove(key);

        // Then
        assertEquals("Should return removed value", value, removedValue);
        assertNull("Key should no longer exist", properties.get(key));
    }

    @Test
    public void testRemoveNonExistentKey_ShouldReturnNull() {
        // When
        Object result = properties.remove("nonExistentKey");

        // Then
        assertNull("Should return null for non-existent key", result);
    }

    @Test
    public void testRemoveKeyValuePair_WhenMatches_ShouldReturnTrue() {
        // Given
        Integer key = 42;
        Integer value = 42;
        properties.put(key, value);

        // When
        boolean removed = properties.remove(key, value);

        // Then
        assertTrue("Should remove matching key-value pair", removed);
        assertFalse("Key should no longer exist", properties.containsKey(key));
    }

    @Test
    public void testRemoveKeyValuePair_WhenNotMatches_ShouldReturnFalse() {
        // Given
        String key = "testKey";
        String storedValue = "storedValue";
        String differentValue = "differentValue";
        properties.put(key, storedValue);

        // When
        boolean removed = properties.remove(key, differentValue);

        // Then
        assertFalse("Should not remove non-matching pair", removed);
        assertTrue("Key should still exist", properties.containsKey(key));
    }

    // ========== Conditional Operations Tests ==========

    @Test
    public void testPutIfAbsent_WithNewKey_ShouldAddValue() {
        // Given
        String key = "newKey";
        String value = "newValue";

        // When
        Object previousValue = properties.putIfAbsent(key, value);

        // Then
        assertNull("Should return null for new key", previousValue);
        assertEquals("Should store the value", value, properties.get(key));
    }

    @Test
    public void testPutIfAbsent_WithExistingKey_ShouldReturnExistingValue() {
        // Given
        String key = "existingKey";
        String existingValue = "existingValue";
        String newValue = "newValue";
        properties.setProperty(key, existingValue);

        // When
        Object result = properties.putIfAbsent(key, newValue);

        // Then
        assertEquals("Should return existing value", existingValue, result);
        assertEquals("Should not change existing value", existingValue, properties.get(key));
    }

    @Test
    public void testComputeIfAbsent_WithNewKey_ShouldComputeAndStore() {
        // Given
        String key = "computeKey";
        Function<Object, String> mappingFunction = k -> "computed_" + k;

        // When
        Object result = properties.computeIfAbsent(key, mappingFunction);

        // Then
        assertEquals("Should return computed value", "computed_" + key, result);
        assertEquals("Should store computed value", "computed_" + key, properties.get(key));
    }

    @Test
    public void testComputeIfAbsent_WithExistingKey_ShouldReturnExistingValue() {
        // Given
        String key = "existingKey";
        String existingValue = "existingValue";
        properties.put(key, existingValue);
        Function<Object, String> mappingFunction = k -> "computed_" + k;

        // When
        Object result = properties.computeIfAbsent(key, mappingFunction);

        // Then
        assertEquals("Should return existing value", existingValue, result);
    }

    @Test
    public void testMerge_WithNewKey_ShouldStoreValue() {
        // Given
        String key = "mergeKey";
        String value = "mergeValue";
        BiFunction<Object, Object, Object> remappingFunction = (oldVal, newVal) -> oldVal + "_" + newVal;

        // When
        Object result = properties.merge(key, value, remappingFunction);

        // Then
        assertEquals("Should store the value", value, result);
        assertEquals("Should store the value", value, properties.get(key));
    }

    @Test
    public void testCompute_ShouldApplyFunction() {
        // Given
        String key = "computeKey";
        BiFunction<Object, Object, String> remappingFunction = (k, v) -> "computed_" + k;

        // When
        Object result = properties.compute(key, remappingFunction);

        // Then
        assertEquals("Should return computed value", "computed_" + key, result);
        assertEquals("Should store computed value", "computed_" + key, properties.get(key));
    }

    // ========== Collection Operations Tests ==========

    @Test
    public void testKeySet_ShouldReturnAllKeys() {
        // Given
        properties.put("key1", "value1");
        properties.put("key2", "value2");

        // When
        Set<Object> keySet = properties.keySet();

        // Then
        assertEquals("Should contain all keys", 2, keySet.size());
        assertTrue("Should contain key1", keySet.contains("key1"));
        assertTrue("Should contain key2", keySet.contains("key2"));
    }

    @Test
    public void testEntrySet_ShouldReturnAllEntries() {
        // Given
        properties.put("key1", "value1");

        // When
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();

        // Then
        assertEquals("Should contain one entry", 1, entrySet.size());
        Map.Entry<Object, Object> entry = entrySet.iterator().next();
        assertEquals("Entry should have correct key", "key1", entry.getKey());
        assertEquals("Entry should have correct value", "value1", entry.getValue());
    }

    @Test
    public void testPutAll_ShouldAddAllEntries() {
        // Given
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("key1", "value1");
        sourceMap.put("key2", "value2");

        // When
        properties.putAll(sourceMap);

        // Then
        assertEquals("Should contain all entries", 2, properties.size());
        assertEquals("Should contain first entry", "value1", properties.get("key1"));
        assertEquals("Should contain second entry", "value2", properties.get("key2"));
    }

    @Test
    public void testClear_ShouldRemoveAllEntries() {
        // Given
        properties.put("key1", "value1");
        properties.put("key2", "value2");

        // When
        properties.clear();

        // Then
        assertTrue("Should be empty after clear", properties.isEmpty());
        assertEquals("Should have size 0", 0, properties.size());
    }

    @Test
    public void testForEach_ShouldIterateOverAllEntries() {
        // Given
        properties.put("key1", "value1");
        properties.put("key2", "value2");
        StringBuilder result = new StringBuilder();
        BiConsumer<Object, Object> action = (k, v) -> result.append(k).append("=").append(v).append(";");

        // When
        properties.forEach(action);

        // Then
        String resultString = result.toString();
        assertTrue("Should process key1", resultString.contains("key1=value1"));
        assertTrue("Should process key2", resultString.contains("key2=value2"));
    }

    // ========== Properties-specific Tests ==========

    @Test
    public void testLoadFromReader_ShouldParseProperties() throws Exception {
        // Given
        String propertiesContent = "key1=value1\nkey2=value2";
        StringReader reader = new StringReader(propertiesContent);

        // When
        properties.load(reader);

        // Then
        assertFalse("Should not be empty after loading", properties.isEmpty());
        assertTrue("Should contain loaded properties", properties.size() > 0);
    }

    // ========== Edge Cases and Error Handling ==========

    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey_ShouldThrowException() {
        properties.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKey_ShouldThrowException() {
        properties.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutIfAbsentWithNullKey_ShouldThrowException() {
        properties.putIfAbsent(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllWithNullMap_ShouldThrowException() {
        properties.putAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testForEachWithNullAction_ShouldThrowException() {
        properties.forEach(null);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeWithNullFunction_ShouldThrowException() {
        properties.compute("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeIfAbsentWithNullFunction_ShouldThrowException() {
        properties.computeIfAbsent("key", null);
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToString_WhenEmpty_ShouldReturnEmptyBraces() {
        // When
        String result = properties.toString();

        // Then
        assertEquals("Empty properties should return {}", "{}", result);
    }

    @Test
    public void testToString_WithEntries_ShouldShowContent() {
        // Given
        properties.put("key1", "value1");

        // When
        String result = properties.toString();

        // Then
        assertNotNull("ToString should not return null", result);
        assertTrue("Should contain key", result.contains("key1"));
        assertTrue("Should contain value", result.contains("value1"));
    }

    @Test
    public void testToString_WithSelfReference_ShouldHandleGracefully() {
        // Given
        properties.put(properties, properties);

        // When
        String result = properties.toString();

        // Then
        assertEquals("Should handle self-reference", "{(this Map)=(this Map)}", result);
    }
}