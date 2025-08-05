package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for ComparableObjectItem class.
 * Tests the basic functionality of creating, comparing, and manipulating
 * ComparableObjectItem instances which hold a Comparable key and an Object value.
 */
public class ComparableObjectItemTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void constructor_WithValidComparableAndNullObject_ShouldCreateItem() {
        // Given
        String key = "test-key";
        Object value = null;
        
        // When
        ComparableObjectItem item = new ComparableObjectItem(key, value);
        
        // Then
        assertEquals(key, item.getComparable());
        assertNull(item.getObject());
    }
    
    @Test
    public void constructor_WithValidComparableAndObject_ShouldCreateItem() {
        // Given
        String key = "test-key";
        String value = "test-value";
        
        // When
        ComparableObjectItem item = new ComparableObjectItem(key, value);
        
        // Then
        assertEquals(key, item.getComparable());
        assertEquals(value, item.getObject());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNullComparable_ShouldThrowException() {
        // When & Then
        new ComparableObjectItem(null, "any-value");
    }

    // ========== Getter Tests ==========
    
    @Test
    public void getComparable_ShouldReturnStoredComparable() {
        // Given
        String expectedKey = "my-key";
        ComparableObjectItem item = new ComparableObjectItem(expectedKey, "value");
        
        // When
        Comparable actualKey = item.getComparable();
        
        // Then
        assertEquals(expectedKey, actualKey);
    }
    
    @Test
    public void getObject_WithNullValue_ShouldReturnNull() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", null);
        
        // When
        Object result = item.getObject();
        
        // Then
        assertNull(result);
    }

    // ========== Setter Tests ==========
    
    @Test
    public void setObject_WithNewValue_ShouldUpdateStoredObject() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", null);
        Object newValue = new Object();
        
        // When
        item.setObject(newValue);
        
        // Then
        assertEquals(newValue, item.getObject());
    }

    // ========== Comparison Tests ==========
    
    @Test
    public void compareTo_WithSameItem_ShouldReturnZero() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", "value");
        
        // When
        int result = item.compareTo(item);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void compareTo_WithItemsHavingSameComparable_ShouldReturnZero() {
        // Given
        String sameKey = "same-key";
        ComparableObjectItem item1 = new ComparableObjectItem(sameKey, "value1");
        ComparableObjectItem item2 = new ComparableObjectItem(sameKey, "value2");
        
        // When
        int result = item1.compareTo(item2);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void compareTo_WithDifferentComparables_ShouldDelegateToComparableCompareTo() {
        // Given
        ComparableObjectItem item1 = new ComparableObjectItem("a", "value1");
        ComparableObjectItem item2 = new ComparableObjectItem("b", "value2");
        
        // When
        int result = item1.compareTo(item2);
        
        // Then
        assertTrue("Expected negative result for 'a' < 'b'", result < 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void compareTo_WithNullArgument_ShouldThrowException() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", "value");
        
        // When & Then
        item.compareTo(null);
    }

    // ========== Equals Tests ==========
    
    @Test
    public void equals_WithSameInstance_ShouldReturnTrue() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", "value");
        
        // When & Then
        assertTrue(item.equals(item));
    }
    
    @Test
    public void equals_WithEqualItems_ShouldReturnTrue() {
        // Given
        String key = "same-key";
        String value = "same-value";
        ComparableObjectItem item1 = new ComparableObjectItem(key, value);
        ComparableObjectItem item2 = new ComparableObjectItem(key, value);
        
        // When & Then
        assertTrue(item1.equals(item2));
        assertTrue(item2.equals(item1));
    }
    
    @Test
    public void equals_WithDifferentComparables_ShouldReturnFalse() {
        // Given
        ComparableObjectItem item1 = new ComparableObjectItem("key1", "value");
        ComparableObjectItem item2 = new ComparableObjectItem("key2", "value");
        
        // When & Then
        assertFalse(item1.equals(item2));
    }
    
    @Test
    public void equals_WithDifferentObjects_ShouldReturnFalse() {
        // Given
        String sameKey = "key";
        ComparableObjectItem item1 = new ComparableObjectItem(sameKey, "value1");
        ComparableObjectItem item2 = new ComparableObjectItem(sameKey, "value2");
        
        // When & Then
        assertFalse(item1.equals(item2));
    }
    
    @Test
    public void equals_WithNonComparableObjectItemType_ShouldReturnFalse() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", "value");
        Object other = new Object();
        
        // When & Then
        assertFalse(item.equals(other));
    }
    
    @Test
    public void equals_WithOneNullObject_ShouldReturnFalse() {
        // Given
        String sameKey = "key";
        ComparableObjectItem itemWithValue = new ComparableObjectItem(sameKey, "value");
        ComparableObjectItem itemWithNull = new ComparableObjectItem(sameKey, null);
        
        // When & Then
        assertFalse(itemWithValue.equals(itemWithNull));
        assertFalse(itemWithNull.equals(itemWithValue));
    }
    
    @Test
    public void equals_WithBothNullObjects_ShouldReturnTrue() {
        // Given
        String sameKey = "key";
        ComparableObjectItem item1 = new ComparableObjectItem(sameKey, null);
        ComparableObjectItem item2 = new ComparableObjectItem(sameKey, null);
        
        // When & Then
        assertTrue(item1.equals(item2));
    }

    // ========== HashCode Tests ==========
    
    @Test
    public void hashCode_WithNullObject_ShouldNotThrowException() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", null);
        
        // When & Then (should not throw)
        item.hashCode();
    }
    
    @Test
    public void hashCode_WithValidObject_ShouldNotThrowException() {
        // Given
        ComparableObjectItem item = new ComparableObjectItem("key", "value");
        
        // When & Then (should not throw)
        item.hashCode();
    }
    
    @Test
    public void hashCode_ForEqualItems_ShouldBeEqual() {
        // Given
        String key = "same-key";
        String value = "same-value";
        ComparableObjectItem item1 = new ComparableObjectItem(key, value);
        ComparableObjectItem item2 = new ComparableObjectItem(key, value);
        
        // When
        int hash1 = item1.hashCode();
        int hash2 = item2.hashCode();
        
        // Then
        assertEquals("Equal objects should have equal hash codes", hash1, hash2);
    }

    // ========== Clone Tests ==========
    
    @Test
    public void clone_ShouldCreateDistinctButEqualInstance() throws CloneNotSupportedException {
        // Given
        ComparableObjectItem original = new ComparableObjectItem("key", "value");
        
        // When
        Object cloned = original.clone();
        
        // Then
        assertNotSame("Clone should be different instance", original, cloned);
        assertTrue("Clone should be equal to original", original.equals(cloned));
        assertTrue("Original should be equal to clone", cloned.equals(original));
    }
}