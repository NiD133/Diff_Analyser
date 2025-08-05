package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.Spliterator;

/**
 * Test suite for CompactLinkedHashSet functionality.
 * Tests creation, manipulation, and edge cases of the compact linked hash set implementation.
 */
public class CompactLinkedHashSetTest {

    // ========== Creation Tests ==========
    
    @Test
    public void testCreateEmptySet() {
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        
        assertTrue("Empty set should be empty", set.isEmpty());
        assertEquals("Empty set should have size 0", 0, set.size());
    }

    @Test
    public void testCreateWithExpectedSize() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(31);
        
        assertTrue("Set with expected size should be empty initially", set.isEmpty());
        assertNotNull("Set should be created successfully", set);
    }

    @Test
    public void testCreateFromArray() {
        Integer[] array = new Integer[2]; // Contains [null, null]
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(array);
        
        assertEquals("Set should contain one null element", 1, set.size());
        assertTrue("Set should contain null", set.contains(null));
    }

    @Test
    public void testCreateFromEmptyArray() {
        Integer[] emptyArray = new Integer[0];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(emptyArray);
        
        assertTrue("Set created from empty array should be empty", set.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        CompactLinkedHashSet<Locale.Category> originalSet = new CompactLinkedHashSet<>();
        CompactLinkedHashSet<Object> newSet = CompactLinkedHashSet.create((Collection<?>) originalSet);
        
        assertTrue("Set created from empty collection should be empty", newSet.isEmpty());
    }

    // ========== Array Operations Tests ==========
    
    @Test
    public void testToArrayOnEmptySet() {
        CompactLinkedHashSet<Locale.Category> set = new CompactLinkedHashSet<>();
        Object[] array = set.toArray();
        
        assertEquals("Empty set should return empty array", 0, array.length);
    }

    @Test
    public void testToArrayWithProvidedArray() {
        Integer[] emptyArray = new Integer[0];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(emptyArray);
        Integer[] result = set.toArray(emptyArray);
        
        assertSame("Should return same array when it fits", emptyArray, result);
    }

    @Test
    public void testToArrayWithLargerProvidedArray() {
        Locale.Category[] largeArray = new Locale.Category[3];
        CompactLinkedHashSet<Object> emptySet = CompactLinkedHashSet.create();
        Object[] result = emptySet.toArray(largeArray);
        
        assertSame("Should return same array when larger than needed", largeArray, result);
    }

    // ========== Iterator and Spliterator Tests ==========
    
    @Test
    public void testSpliterator() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(31);
        Spliterator<Integer> spliterator = set.spliterator();
        
        assertNotNull("Spliterator should not be null", spliterator);
    }

    @Test
    public void testFirstEntryIndexOnNonEmptySet() {
        Locale.Category[] array = new Locale.Category[9]; // Contains nulls
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(array);
        int firstIndex = set.firstEntryIndex();
        
        assertEquals("First entry should be at index 0", 0, firstIndex);
        assertEquals("Set should have one element", 1, set.size());
    }

    @Test
    public void testFirstEntryIndexOnEmptySet() {
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        int firstIndex = set.firstEntryIndex();
        
        assertEquals("Empty set should return -2 for first entry", -2, firstIndex);
    }

    // ========== Modification Tests ==========
    
    @Test
    public void testClearEmptySet() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(2497);
        set.clear();
        
        assertEquals("Cleared set should have size 0", 0, set.size());
    }

    @Test
    public void testClearNonEmptySet() {
        Locale.Category[] array = new Locale.Category[1]; // Contains [null]
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create((Object[]) array);
        set.clear();
        
        assertEquals("Cleared set should have size 0", 0, set.size());
    }

    @Test
    public void testRetainAllWithEmptyCollection() {
        Object[] array = new Object[2];
        array[0] = new Object(); // Add one non-null element
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(array);
        
        // Create empty collection for retainAll
        CompactLinkedHashSet<Object> emptySet = new CompactLinkedHashSet<>();
        boolean changed = set.retainAll(emptySet);
        
        assertTrue("RetainAll with empty collection should change the set", changed);
        assertTrue("Set should be empty after retainAll with empty collection", set.isEmpty());
    }

    // ========== Internal Operations Tests ==========
    
    @Test
    public void testConvertToHashFloodingResistantImplementation() {
        CompactLinkedHashSet<Integer> set = new CompactLinkedHashSet<>(8232);
        Set<Integer> converted = set.convertToHashFloodingResistantImplementation();
        
        assertEquals("Converted set should have same size", 0, converted.size());
    }

    @Test
    public void testAdjustAfterRemoveOperations() {
        CompactLinkedHashSet<Comparable<Object>> set = CompactLinkedHashSet.createWithExpectedSize(133);
        
        // Test different scenarios of adjustAfterRemove
        int result1 = set.adjustAfterRemove(1941, 0);
        assertEquals("Should return 0 when removing first element", 0, result1);
        
        Object[] array = new Object[2];
        CompactLinkedHashSet<Object> set2 = CompactLinkedHashSet.create(array);
        int result2 = set2.adjustAfterRemove(1941, 1941);
        assertEquals("Should return same index when removing self", 1941, result2);
        
        Integer[] emptyArray = new Integer[0];
        CompactLinkedHashSet<Integer> set3 = CompactLinkedHashSet.create(emptyArray);
        int result3 = set3.adjustAfterRemove(73, -1);
        assertEquals("Should return -1 for invalid removal", -1, result3);
    }

    // ========== Exception Tests ==========
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeExpectedSize() {
        CompactLinkedHashSet.createWithExpectedSize(-1086);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullArray() {
        CompactLinkedHashSet.create((Locale.Category[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullCollection() {
        CompactLinkedHashSet.create((Collection<? extends Integer>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSize() {
        new CompactLinkedHashSet<>(-439);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNegativeSize() {
        CompactLinkedHashSet<Object> set = new CompactLinkedHashSet<>();
        set.init(-1191);
    }

    // ========== Internal State Tests ==========
    
    @Test
    public void testInternalOperationsOnPopulatedSet() {
        Integer[] array = new Integer[2];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(array);
        
        // Test internal operations that should work on populated set
        set.moveLastEntry(0, 0);
        assertEquals("Set should maintain size after moveLastEntry", 1, set.size());
        
        set.insertEntry(2, 1, 1, 2788);
        assertEquals("Set should have correct size after insertEntry", 1, set.size());
    }

    @Test
    public void testResizeOperations() {
        Object[] array = new Object[1];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(array);
        
        set.resizeEntries(41);
        assertEquals("Set should maintain size after resize", 1, set.size());
    }

    @Test
    public void testInitializationOperations() {
        Object[] array = new Object[2];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(array);
        
        set.init(0);
        assertFalse("Set should not contain 0 after init", set.contains(0));
    }
}