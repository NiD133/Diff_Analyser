package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Vector;
import org.jfree.chart.api.SortOrder;
import org.jfree.chart.api.TableOrder;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.general.DefaultKeyedValuesDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * Test suite for DefaultPieDataset functionality.
 * Tests basic operations, edge cases, and error conditions.
 */
public class DefaultPieDataset_ESTest {

    // Test constants for better readability
    private static final Integer DRAG_LAYER_KEY = 400;
    private static final Integer POPUP_LAYER_KEY = 300;
    private static final Integer MODAL_LAYER_KEY = 200;
    private static final Integer FRAME_CONTENT_LAYER_KEY = -30000;
    private static final Integer DEFAULT_LAYER_KEY = 0;

    // === Basic Functionality Tests ===

    @Test
    public void testSetValueAndGetKey() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.setValue(DRAG_LAYER_KEY, DRAG_LAYER_KEY);
        
        Integer retrievedKey = dataset.getKey(0);
        assertEquals("Key should match the inserted value", DRAG_LAYER_KEY, retrievedKey);
    }

    @Test
    public void testSetValueAndGetValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.setValue(DRAG_LAYER_KEY, 0.8);
        
        assertEquals("Item count should be 1", 1, dataset.getItemCount());
    }

    @Test
    public void testSetNullValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.setValue(DRAG_LAYER_KEY, null);
        
        Number value = dataset.getValue(DRAG_LAYER_KEY);
        assertNull("Value should be null", value);
        
        Number valueByIndex = dataset.getValue(0);
        assertNull("Value retrieved by index should also be null", valueByIndex);
    }

    @Test
    public void testInsertValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.insertValue(0, FRAME_CONTENT_LAYER_KEY, 0.0);
        
        assertEquals("Item count should be 1 after insertion", 1, dataset.getItemCount());
    }

    @Test
    public void testGetIndex() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER_KEY, null);
        
        int index = dataset.getIndex(DRAG_LAYER_KEY);
        
        assertEquals("Index should be 0 for first item", 0, index);
    }

    @Test
    public void testGetIndexWithMultipleItems() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER_KEY, 0.8);
        dataset.setValue(MODAL_LAYER_KEY, -1.0);
        
        int index = dataset.getIndex(MODAL_LAYER_KEY);
        
        assertEquals("Index should be 1 for second item", 1, index);
    }

    @Test
    public void testRemoveValue() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        dataset.setValue(DRAG_LAYER_KEY, 1036.25471);
        
        dataset.remove(DRAG_LAYER_KEY);
        
        assertTrue("Dataset should still have notify enabled", dataset.getNotify());
    }

    @Test
    public void testClearDataset() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.clear();
        
        assertTrue("Dataset should have notify enabled after clear", dataset.getNotify());
    }

    @Test
    public void testSortByKeysOnEmptyDataset() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.sortByKeys(SortOrder.DESCENDING);
        
        assertEquals("Empty dataset should remain empty after sorting", 0, dataset.getItemCount());
    }

    @Test
    public void testSortByValuesOnEmptyDataset() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.sortByValues(SortOrder.ASCENDING);
        
        assertEquals("Empty dataset should remain empty after sorting", 0, dataset.getItemCount());
    }

    @Test
    public void testGetKeysReturnsEmptyList() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        List<Integer> keys = dataset.getKeys();
        
        assertTrue("Keys list should be empty for empty dataset", keys.isEmpty());
    }

    @Test
    public void testGetIndexForNonExistentKey() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        int index = dataset.getIndex(DRAG_LAYER_KEY);
        
        assertEquals("Index should be -1 for non-existent key", -1, index);
    }

    @Test
    public void testEmptyDatasetItemCount() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        int itemCount = dataset.getItemCount();
        
        assertEquals("Empty dataset should have 0 items", 0, itemCount);
    }

    // === Equality Tests ===

    @Test
    public void testEqualityWithDifferentDatasetTypes() {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        pieDataset.setValue(POPUP_LAYER_KEY, POPUP_LAYER_KEY);
        
        DefaultKeyedValuesDataset<Integer> keyedDataset = new DefaultKeyedValuesDataset<>();
        
        boolean isEqual = keyedDataset.equals(pieDataset);
        
        assertFalse("Different datasets with different data should not be equal", isEqual);
    }

    @Test
    public void testEqualityAfterDataModification() {
        DefaultKeyedValuesDataset<Integer> dataset1 = new DefaultKeyedValuesDataset<>();
        dataset1.setValue(FRAME_CONTENT_LAYER_KEY, FRAME_CONTENT_LAYER_KEY);
        
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>(dataset1);
        dataset2.setValue(FRAME_CONTENT_LAYER_KEY, -140.1353984740134);
        
        boolean isEqual = dataset1.equals(dataset2);
        
        assertFalse("Datasets should not be equal after modification", isEqual);
    }

    @Test
    public void testEqualityWithIdenticalData() {
        DefaultKeyedValuesDataset<Integer> dataset1 = new DefaultKeyedValuesDataset<>();
        dataset1.setValue(FRAME_CONTENT_LAYER_KEY, FRAME_CONTENT_LAYER_KEY);
        
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>(dataset1);
        
        boolean isEqual = dataset1.equals(dataset2);
        
        assertTrue("Datasets with identical data should be equal", isEqual);
    }

    @Test
    public void testEqualityWithDifferentTypes() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        boolean isEqual = dataset.equals(DRAG_LAYER_KEY);
        
        assertFalse("Dataset should not equal a different type", isEqual);
    }

    @Test
    public void testSelfEquality() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        boolean isEqual = dataset.equals(dataset);
        
        assertTrue("Dataset should equal itself", isEqual);
    }

    // === Error Condition Tests ===

    @Test(expected = IllegalArgumentException.class)
    public void testSortByValuesWithNullOrder() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.sortByValues(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSortByKeysWithNullOrder() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.sortByKeys(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNullKey() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.setValue(null, (Number) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDoubleValueWithNullKey() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.setValue(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentKey() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.remove(POPUP_LAYER_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKey() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithNullKey() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        dataset.getValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIndexWithNullKey() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.getIndex(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueForNonExistentKey() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.getValue(DRAG_LAYER_KEY);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.getKey(-2210);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        
        dataset.getValue(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertValueAtInvalidPosition() {
        DefaultKeyedValues<Integer> keyedValues = new DefaultKeyedValues<>();
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>(keyedValues);
        
        dataset.insertValue(1, POPUP_LAYER_KEY, -3722.9802);
    }

    // === Constructor Tests ===

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSource() {
        new DefaultPieDataset<Integer>((KeyedValues<Integer>) null);
    }

    @Test
    public void testConstructorWithValidSource() {
        DefaultKeyedValues<Integer> keyedValues = new DefaultKeyedValues<>();
        
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>(keyedValues);
        
        assertTrue("Dataset should have notify enabled", dataset.getNotify());
    }

    // === Clone and Hash Tests ===

    @Test
    public void testClone() throws CloneNotSupportedException {
        DefaultKeyedValuesDataset<Integer> original = new DefaultKeyedValuesDataset<>();
        
        DefaultPieDataset cloned = (DefaultPieDataset) original.clone();
        
        assertTrue("Cloned dataset should have notify enabled", cloned.getNotify());
    }

    @Test
    public void testHashCode() {
        DefaultKeyedValues<Integer> keyedValues = new DefaultKeyedValues<>();
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>(keyedValues);
        
        // Should not throw exception
        dataset.hashCode();
    }

    // === Complex Scenario Tests ===

    @Test
    public void testInsertValueWithExistingData() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        dataset.insertValue(0, DRAG_LAYER_KEY, DRAG_LAYER_KEY);
        
        assertTrue("Dataset should have notify enabled after insertion", dataset.getNotify());
    }

    @Test
    public void testDatasetEqualityAfterCopyAndModification() {
        DefaultPieDataset<Integer> original = new DefaultPieDataset<>();
        original.setValue(DRAG_LAYER_KEY, null);
        
        DefaultPieDataset<Integer> copy = new DefaultPieDataset<>(original);
        assertTrue("Copy should initially equal original", copy.equals(original));
        
        copy.setValue(DRAG_LAYER_KEY, DRAG_LAYER_KEY);
        
        boolean isEqual = original.equals(copy);
        assertFalse("Datasets should not be equal after modification", isEqual);
    }

    @Test
    public void testDatasetEqualityWithDifferentKeys() {
        DefaultPieDataset<Integer> dataset1 = new DefaultPieDataset<>();
        dataset1.setValue(DRAG_LAYER_KEY, null);
        
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>(dataset1);
        dataset2.clear();
        dataset2.setValue(POPUP_LAYER_KEY, DRAG_LAYER_KEY);
        
        boolean isEqual = dataset1.equals(dataset2);
        
        assertEquals("Second dataset should have 1 item", 1, dataset2.getItemCount());
        assertFalse("Datasets with different keys should not be equal", isEqual);
    }

    @Test
    public void testDatasetEqualityWithDifferentItemCounts() {
        DefaultKeyedValuesDataset<Integer> dataset1 = new DefaultKeyedValuesDataset<>();
        DefaultKeyedValuesDataset<Integer> dataset2 = new DefaultKeyedValuesDataset<>();
        
        assertTrue("Empty datasets should be equal", dataset2.equals(dataset1));
        
        dataset1.setValue(FRAME_CONTENT_LAYER_KEY, FRAME_CONTENT_LAYER_KEY);
        
        boolean isEqual = dataset1.equals(dataset2);
        assertFalse("Datasets with different item counts should not be equal", isEqual);
    }

    @Test
    public void testCopyConstructorPreservesEquality() {
        DefaultPieDataset<Integer> original = new DefaultPieDataset<>();
        original.setValue(DRAG_LAYER_KEY, null);
        
        DefaultPieDataset<Integer> copy = new DefaultPieDataset<>(original);
        
        boolean isEqual = copy.equals(original);
        assertTrue("Copy should equal original", isEqual);
    }
}