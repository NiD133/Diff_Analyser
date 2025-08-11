package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import org.jfree.chart.date.SerialDate;
import org.jfree.chart.date.SpreadsheetDate;
import org.jfree.data.general.DefaultKeyedValueDataset;

/**
 * Test suite for DefaultKeyedValueDataset class.
 * Tests the core functionality of storing and retrieving keyed values.
 */
public class DefaultKeyedValueDatasetTest {

    // Constructor Tests
    
    @Test
    public void testDefaultConstructor_CreatesEmptyDataset() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        
        assertNull("Key should be null for empty dataset", dataset.getKey());
        assertNull("Value should be null for empty dataset", dataset.getValue());
    }

    @Test
    public void testConstructorWithKeyAndValue_StoresDataCorrectly() {
        String key = "TestKey";
        Integer value = 42;
        
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);
        
        assertEquals("Key should match constructor parameter", key, dataset.getKey());
        assertEquals("Value should match constructor parameter", value, dataset.getValue());
    }

    @Test
    public void testConstructorWithNullKey_ThrowsException() {
        try {
            new DefaultKeyedValueDataset(null, 100);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should mention null key", 
                      e.getMessage().contains("Null 'key' argument"));
        }
    }

    @Test
    public void testConstructorWithNullValue_AllowsNullValue() {
        String key = "TestKey";
        
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, null);
        
        assertEquals("Key should be stored correctly", key, dataset.getKey());
        assertNull("Null value should be allowed", dataset.getValue());
    }

    // Value Retrieval Tests

    @Test
    public void testGetValue_ReturnsCorrectNumberTypes() {
        // Test with BigInteger
        BigInteger bigIntValue = new BigInteger("12345");
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", bigIntValue);
        
        Number retrievedValue = dataset.getValue();
        assertSame("Should return the exact same BigInteger instance", bigIntValue, retrievedValue);
        assertEquals("BigInteger value should be correct", bigIntValue, retrievedValue);
    }

    @Test
    public void testGetValue_WithSerialDateKey() {
        SerialDate dateKey = SerialDate.createInstance(1670);
        Integer value = 2958465;
        
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(dateKey, value);
        
        assertSame("Key should be the same SerialDate instance", dateKey, dataset.getKey());
        assertEquals("Value should match", value, dataset.getValue());
    }

    // Update Value Tests

    @Test
    public void testUpdateValue_ChangesExistingValue() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", 100);
        
        dataset.updateValue(200);
        
        assertEquals("Value should be updated", Integer.valueOf(200), dataset.getValue());
        assertEquals("Key should remain unchanged", "key", dataset.getKey());
        assertTrue("Notification should be enabled by default", dataset.getNotify());
    }

    @Test
    public void testUpdateValue_WithNullValue_ThrowsException() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", 100);
        
        try {
            dataset.updateValue(null);
            fail("Should throw RuntimeException when updating with null value");
        } catch (RuntimeException e) {
            assertTrue("Exception message should mention null update", 
                      e.getMessage().contains("updateValue: can't update null"));
        }
    }

    @Test
    public void testUpdateValue_OnEmptyDataset_ThrowsException() {
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        
        try {
            emptyDataset.updateValue(100);
            fail("Should throw exception when updating empty dataset");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention null key", 
                      e.getMessage().contains("Null 'key' argument"));
        }
    }

    // Set Value Tests

    @Test
    public void testSetValue_UpdatesKeyAndValue() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("oldKey", 100);
        
        dataset.setValue("newKey", 200);
        
        assertEquals("Key should be updated", "newKey", dataset.getKey());
        assertEquals("Value should be updated", Integer.valueOf(200), dataset.getValue());
    }

    @Test
    public void testSetValue_WithNullKey_ThrowsException() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        
        try {
            dataset.setValue(null, 100);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention null key", 
                      e.getMessage().contains("Null 'key' argument"));
        }
    }

    // Equality Tests

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", 100);
        
        assertTrue("Dataset should equal itself", dataset.equals(dataset));
    }

    @Test
    public void testEquals_IdenticalDatasets_ReturnsTrue() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("key", 100);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("key", 100);
        
        assertTrue("Datasets with same key and value should be equal", 
                  dataset1.equals(dataset2));
    }

    @Test
    public void testEquals_DifferentValues_ReturnsFalse() {
        SpreadsheetDate key = new SpreadsheetDate(379);
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset(key, 2);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(key, 3);
        
        assertFalse("Datasets with different values should not be equal", 
                   dataset1.equals(dataset2));
    }

    @Test
    public void testEquals_DifferentKeys_ReturnsFalse() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("key1", 100);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("key2", 100);
        
        assertFalse("Datasets with different keys should not be equal", 
                   dataset1.equals(dataset2));
    }

    @Test
    public void testEquals_EmptyDatasets_ReturnsTrue() {
        DefaultKeyedValueDataset emptyDataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset emptyDataset2 = new DefaultKeyedValueDataset();
        
        assertTrue("Empty datasets should be equal", 
                  emptyDataset1.equals(emptyDataset2));
    }

    @Test
    public void testEquals_WithNonDatasetObject_ReturnsFalse() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", 100);
        BigInteger otherObject = BigInteger.TEN;
        
        assertFalse("Dataset should not equal non-dataset object", 
                   dataset.equals(otherObject));
    }

    @Test
    public void testEquals_EmptyVsPopulated_ReturnsFalse() {
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset populatedDataset = new DefaultKeyedValueDataset("key", 100);
        
        assertFalse("Empty dataset should not equal populated dataset", 
                   emptyDataset.equals(populatedDataset));
        assertFalse("Populated dataset should not equal empty dataset", 
                   populatedDataset.equals(emptyDataset));
    }

    // Hash Code Tests

    @Test
    public void testHashCode_EmptyDataset() {
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        
        // Should not throw exception
        int hashCode = emptyDataset.hashCode();
        
        // Hash code should be consistent
        assertEquals("Hash code should be consistent", hashCode, emptyDataset.hashCode());
    }

    @Test
    public void testHashCode_PopulatedDataset() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("key", 100);
        
        // Should not throw exception
        int hashCode = dataset.hashCode();
        
        // Hash code should be consistent
        assertEquals("Hash code should be consistent", hashCode, dataset.hashCode());
    }

    // Clone Tests

    @Test
    public void testClone_CreatesEqualButSeparateInstance() throws CloneNotSupportedException {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("key", 100);
        
        Object cloned = original.clone();
        
        assertNotSame("Clone should be different instance", original, cloned);
        assertTrue("Clone should be equal to original", original.equals(cloned));
        assertTrue("Clone should be instance of DefaultKeyedValueDataset", 
                  cloned instanceof DefaultKeyedValueDataset);
    }

    @Test
    public void testClone_EmptyDataset() throws CloneNotSupportedException {
        DefaultKeyedValueDataset emptyOriginal = new DefaultKeyedValueDataset();
        
        Object cloned = emptyOriginal.clone();
        
        assertNotSame("Clone should be different instance", emptyOriginal, cloned);
        assertTrue("Clone should be equal to original", emptyOriginal.equals(cloned));
    }

    // Copy Constructor Tests

    @Test
    public void testCopyConstructor_CopiesDataCorrectly() {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("key", 100);
        
        DefaultKeyedValueDataset copy = new DefaultKeyedValueDataset(original);
        
        assertTrue("Copy should equal original", copy.equals(original));
        assertEquals("Key should be copied", original.getKey(), copy.getKey());
        assertEquals("Value should be copied", original.getValue(), copy.getValue());
    }

    @Test
    public void testCopyConstructor_WithEmptyDataset() {
        DefaultKeyedValueDataset emptyOriginal = new DefaultKeyedValueDataset();
        
        DefaultKeyedValueDataset copy = new DefaultKeyedValueDataset(emptyOriginal);
        
        assertTrue("Copy should equal empty original", copy.equals(emptyOriginal));
        assertNull("Copied key should be null", copy.getKey());
        assertNull("Copied value should be null", copy.getValue());
    }
}