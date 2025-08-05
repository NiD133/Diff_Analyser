package org.apache.commons.collections4.properties;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A test suite for {@link OrderedProperties} to ensure it correctly functions
 * as a properties map that maintains insertion order.
 */
public class OrderedPropertiesTest {

    private OrderedProperties properties;

    @Before
    public void setUp() {
        properties = new OrderedProperties();
    }

    // --- Core Feature: Insertion Order Preservation ---

    @Test
    public void keys_and_propertyNames_shouldPreserveInsertionOrder() {
        // Arrange
        properties.put("first", "1");
        properties.put("second", "2");
        properties.setProperty("third", "3"); // setProperty is equivalent to put

        // Act
        List<Object> keys = Collections.list(properties.keys());
        List<Object> propertyNames = Collections.list(properties.propertyNames());

        // Assert
        List<String> expectedOrder = Arrays.asList("first", "second", "third");
        assertEquals("keys() should return keys in insertion order.", expectedOrder, keys);
        assertEquals("propertyNames() should return keys in insertion order.", expectedOrder, propertyNames);
    }

    @Test
    public void entrySet_shouldPreserveInsertionOrder() {
        // Arrange
        properties.put("b", "2");
        properties.put("a", "1");
        properties.put("c", "3");

        // Act
        List<String> keyOrderFromEntrySet = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            keyOrderFromEntrySet.add((String) entry.getKey());
        }

        // Assert
        List<String> expectedOrder = Arrays.asList("b", "a", "c");
        assertEquals("entrySet() iterator should preserve insertion order.", expectedOrder, keyOrderFromEntrySet);
    }

    @Test
    public void loadFromReader_shouldAddPropertiesInOrder() throws IOException {
        // Arrange
        String propsString = "b=2\na=1\nc=3";

        // Act
        properties.load(new StringReader(propsString));

        // Assert
        List<Object> keys = Collections.list(properties.keys());
        assertEquals("Loaded keys should be in the order they appeared in the reader.", Arrays.asList("b", "a", "c"), keys);
    }

    // --- put / putAll / putIfAbsent ---

    @Test
    public void put_shouldReturnPreviousValue_whenKeyExists() {
        properties.put("key", "initialValue");
        Object previousValue = properties.put("key", "newValue");
        assertEquals("initialValue", previousValue);
    }
    
    @Test
    public void putAll_shouldAddAllEntriesFromMap() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        
        properties.putAll(map);
        
        assertEquals(2, properties.size());
        assertEquals("1", properties.get("a"));
    }

    @Test
    public void putIfAbsent_shouldAddValue_whenKeyIsNew() {
        Object previousValue = properties.putIfAbsent("key", "value");
        assertNull("putIfAbsent should return null for a new key.", previousValue);
        assertEquals("value", properties.get("key"));
    }

    @Test
    public void putIfAbsent_shouldNotChangeValue_whenKeyExists() {
        properties.put("key", "existingValue");
        Object previousValue = properties.putIfAbsent("key", "newValue");
        assertEquals("putIfAbsent should return the existing value.", "existingValue", previousValue);
        assertEquals("The value should not be overwritten.", "existingValue", properties.get("key"));
    }

    // --- remove ---

    @Test
    public void remove_shouldReturnNull_whenKeyDoesNotExist() {
        assertNull(properties.remove("nonexistentKey"));
    }

    @Test
    public void removeWithValue_shouldReturnFalse_whenKeyDoesNotExist() {
        assertFalse(properties.remove("nonexistentKey", "value"));
    }

    @Test
    public void removeWithValue_shouldReturnTrue_whenKeyAndValueExist() {
        // Arrange
        Integer key = 100;
        String value = "value";
        properties.put(key, value);

        // Act & Assert
        assertTrue("remove(k,v) should succeed for existing entry.", properties.remove(key, value));
        assertTrue("Properties should be empty after removal.", properties.isEmpty());
    }
    
    @Test
    public void removeWithValue_shouldSucceed_whenUsingEqualButNotSameKeyObject() {
        // Arrange
        Integer key1 = new Integer(100);
        Integer key2 = new Integer(100); // key1.equals(key2) is true
        properties.put(key1, "value");

        // Act
        boolean result = properties.remove(key2, "value");

        // Assert
        assertTrue("Removal should succeed with an equal key.", result);
        assertTrue(properties.isEmpty());
    }

    // --- compute / merge ---

    @Test
    public void compute_shouldUpdateValue_whenKeyExists() {
        properties.put("key", 1);
        Object result = properties.compute("key", (k, v) -> (Integer) v + 1);
        assertEquals(2, result);
        assertEquals(2, properties.get("key"));
    }

    @Test
    public void compute_shouldRemoveEntry_whenFunctionReturnsNull() {
        properties.put("key", "value");
        Object result = properties.compute("key", (k, v) -> null);
        assertNull(result);
        assertFalse(properties.containsKey("key"));
    }

    @Test
    public void merge_shouldUpdateValue_whenKeyExists() {
        properties.put("key", "old");
        Object result = properties.merge("key", "new", (oldVal, newVal) -> oldVal + "" + newVal);
        assertEquals("oldnew", result);
        assertEquals("oldnew", properties.get("key"));
    }

    // --- forEach ---

    @Test
    public void forEach_shouldIterateOverEntriesInOrder() {
        properties.put("b", 2);
        properties.put("a", 1);

        List<Object> iteratedKeys = new ArrayList<>();
        properties.forEach((key, value) -> iteratedKeys.add(key));

        assertEquals(Arrays.asList("b", "a"), iteratedKeys);
    }

    @Test
    public void forEach_shouldDoNothing_onEmptyProperties() {
        AtomicInteger callCount = new AtomicInteger(0);
        properties.forEach((k, v) -> callCount.incrementAndGet());
        assertEquals(0, callCount.get());
    }

    // --- toString ---

    @Test
    public void toString_shouldReturnEmptyBraces_forEmptyProperties() {
        assertEquals("{}", properties.toString());
    }

    @Test
    public void toString_shouldReflectInsertionOrder() {
        properties.put("b", 2);
        properties.put("a", 1);
        properties.put("c", 3);

        String expected = "{b=2, a=1, c=3}";
        assertEquals(expected, properties.toString());
    }

    @Test
    public void toString_shouldHandleSelfReferenceWithoutError() {
        properties.put(properties, properties);
        assertEquals("{(this Map)=(this Map)}", properties.toString());
    }

    // --- Null Handling and Exceptions (as per Hashtable contract) ---

    @Test(expected = NullPointerException.class)
    public void put_shouldThrowNPE_whenKeyIsNull() {
        properties.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void put_shouldThrowNPE_whenValueIsNull() {
        properties.put("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void putAll_shouldThrowNPE_whenMapIsNull() {
        properties.putAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void remove_shouldThrowNPE_whenKeyIsNull() {
        properties.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void compute_shouldThrowNPE_whenFunctionIsNull() {
        properties.compute("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void merge_shouldThrowNPE_whenFunctionIsNull() {
        properties.merge("key", "value", null);
    }
}