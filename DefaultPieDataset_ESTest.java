package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Vector;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.SortOrder;
import org.jfree.chart.api.TableOrder;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.general.DefaultKeyedValuesDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class DefaultPieDataset_ESTest extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEqualityWithDifferentDatasetTypes() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.POPUP_LAYER;
        pieDataset.setValue(key, key);
        
        DefaultKeyedValuesDataset<Integer> keyedValuesDataset = new DefaultKeyedValuesDataset<>();
        boolean areEqual = keyedValuesDataset.equals(pieDataset);
        
        assertFalse("Datasets of different types should not be equal", areEqual);
    }

    @Test(timeout = 4000)
    public void testInsertValueAndNotify() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        dataset.insertValue(0, key, key);
        
        assertTrue("Dataset should notify listeners after value insertion", dataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testGetKeyAfterSettingValue() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, key);
        
        Integer retrievedKey = pieDataset.getKey(0);
        
        assertEquals("Retrieved key should match the inserted key", 400, (int)retrievedKey);
    }

    @Test(timeout = 4000)
    public void testSortByKeysDescending() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        pieDataset.sortByKeys(SortOrder.DESCENDING);
        
        assertEquals("Dataset should have no items initially", 0, pieDataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void testRemoveValueAndNotify() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        dataset.setValue(key, 1036.25471);
        dataset.remove(key);
        
        assertTrue("Dataset should notify listeners after value removal", dataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testInsertValueInPieDataset() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.FRAME_CONTENT_LAYER;
        pieDataset.insertValue(0, key, 0.0);
        
        assertEquals("Dataset should contain one item after insertion", 1, pieDataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void testSetValueToNull() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        Number value = pieDataset.getValue(key);
        
        assertNull("Value should be null after setting it to null", value);
    }

    @Test(timeout = 4000)
    public void testGetValueByIndexAfterSettingNull() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        Number value = pieDataset.getValue(0);
        
        assertNull("Value should be null when retrieved by index after setting it to null", value);
    }

    @Test(timeout = 4000)
    public void testGetItemCountAfterSettingValue() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, 0.8);
        
        int itemCount = pieDataset.getItemCount();
        
        assertEquals("Item count should be 1 after setting a value", 1, itemCount);
    }

    @Test(timeout = 4000)
    public void testGetIndexAfterSettingNullValue() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        int index = pieDataset.getIndex(key);
        
        assertEquals("Index should be 0 after setting a null value", 0, index);
    }

    @Test(timeout = 4000)
    public void testSetMultipleValuesAndGetIndex() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key1 = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key1, 0.8);
        
        Integer key2 = JLayeredPane.MODAL_LAYER;
        pieDataset.setValue(key2, -1.0);
        
        int index = pieDataset.getIndex(key2);
        
        assertEquals("Index of the second key should be 1", 1, index);
    }

    @Test(timeout = 4000)
    public void testSortByValuesWithNullOrder() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        
        try {
            pieDataset.sortByValues(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetValueWithNullKey() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        try {
            dataset.setValue(null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetValueWithNullKeyAndDoubleValue() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        try {
            dataset.setValue(null, 0.0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveUnrecognizedKey() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.POPUP_LAYER;
        
        try {
            pieDataset.remove(key);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.DefaultKeyedValues", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertValueOutOfBounds() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.DEFAULT_LAYER;
        dataset.setValue(key, key);
        
        try {
            dataset.insertValue(1, key, key);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertValueOutOfBoundsWithDouble() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.DEFAULT_LAYER;
        dataset.setValue(key, key);
        
        try {
            dataset.insertValue(1, key, 1.0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetValueWithNullKey() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        try {
            dataset.getValue(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetIndexWithNullKey() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        
        try {
            pieDataset.getIndex(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreatePieDatasetFromNullCategoryDataset() throws Throwable {
        SlidingCategoryDataset<ChronoLocalDate, ChronoLocalDate> slidingDataset = new SlidingCategoryDataset<>(null, -1795, -1795);
        TableOrder tableOrder = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset = new CategoryToPieDataset(slidingDataset, tableOrder, -1795);
        
        try {
            new DefaultPieDataset<>(categoryToPieDataset);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.data.category.SlidingCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreatePieDatasetFromEmptyBoxAndWhiskerDataset() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> boxAndWhiskerDataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        Vector<Integer> vector = new Vector<>();
        Integer key = JLayeredPane.MODAL_LAYER;
        boxAndWhiskerDataset.add(vector, key, key);
        
        TableOrder tableOrder = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset = new CategoryToPieDataset(boxAndWhiskerDataset, tableOrder, -374);
        
        try {
            new DefaultPieDataset<>(categoryToPieDataset);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCreatePieDatasetFromNullKeyedValues() throws Throwable {
        try {
            new DefaultPieDataset<>((KeyedValues<Integer>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreatePieDatasetFromEmptyIntervalCategoryDataset() throws Throwable {
        double[][] data = new double[3][9];
        data[2] = new double[0];
        DefaultIntervalCategoryDataset intervalDataset = new DefaultIntervalCategoryDataset(data, data);
        TableOrder tableOrder = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset = new CategoryToPieDataset(intervalDataset, tableOrder, 1);
        
        try {
            new DefaultPieDataset<>(categoryToPieDataset);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetKeyWithInvalidIndex() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        
        try {
            pieDataset.getKey(-2210);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetItemCountForEmptyDataset() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        
        assertEquals("Item count should be 0 for an empty dataset", 0, pieDataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void testGetValueWithInvalidIndex() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        
        try {
            pieDataset.getValue(0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualityAfterSettingNegativeValue() throws Throwable {
        DefaultKeyedValuesDataset<Integer> keyedValuesDataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.FRAME_CONTENT_LAYER;
        keyedValuesDataset.setValue(key, key);
        
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>(keyedValuesDataset);
        assertEquals("Pie dataset should have one item after initialization", 1, pieDataset.getItemCount());
        
        pieDataset.setValue(key, -140.1353984740134);
        boolean areEqual = keyedValuesDataset.equals(pieDataset);
        
        assertFalse("Datasets should not be equal after setting a negative value", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityAfterSettingNullAndNonNullValues() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        DefaultPieDataset<Integer> copiedPieDataset = new DefaultPieDataset<>(pieDataset);
        assertTrue("Copied dataset should be equal to the original", copiedPieDataset.equals(pieDataset));
        
        copiedPieDataset.setValue(key, key);
        boolean areEqual = pieDataset.equals(copiedPieDataset);
        
        assertFalse("Datasets should not be equal after setting different values", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithSameKeyedValues() throws Throwable {
        DefaultKeyedValuesDataset<Integer> keyedValuesDataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.FRAME_CONTENT_LAYER;
        keyedValuesDataset.setValue(key, key);
        
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>(keyedValuesDataset);
        boolean areEqual = keyedValuesDataset.equals(pieDataset);
        
        assertTrue("Datasets should be equal when initialized with the same values", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityAfterClearingAndSettingValue() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        DefaultPieDataset<Integer> copiedPieDataset = new DefaultPieDataset<>(pieDataset);
        Integer newKey = JLayeredPane.POPUP_LAYER;
        copiedPieDataset.clear();
        copiedPieDataset.setValue(newKey, key);
        
        boolean areEqual = pieDataset.equals(copiedPieDataset);
        
        assertEquals("Copied dataset should have one item after setting a new value", 1, copiedPieDataset.getItemCount());
        assertFalse("Datasets should not be equal after clearing and setting a new value", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentValues() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset1 = new DefaultKeyedValuesDataset<>();
        DefaultKeyedValuesDataset<Integer> dataset2 = new DefaultKeyedValuesDataset<>();
        
        assertTrue("Datasets should be equal when both are empty", dataset2.equals(dataset1));
        
        Integer key = JLayeredPane.FRAME_CONTENT_LAYER;
        dataset1.setValue(key, key);
        boolean areEqual = dataset1.equals(dataset2);
        
        assertFalse("Datasets should not be equal after setting a value in one", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentObjectTypes() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        
        boolean areEqual = pieDataset.equals(key);
        
        assertFalse("Dataset should not be equal to an object of a different type", areEqual);
    }

    @Test(timeout = 4000)
    public void testSelfEquality() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        boolean areEqual = dataset.equals(dataset);
        
        assertTrue("Dataset should be equal to itself", areEqual);
    }

    @Test(timeout = 4000)
    public void testClearDataset() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        pieDataset.clear();
        
        assertTrue("Dataset should notify listeners after clearing", pieDataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testSortByValuesAscending() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        dataset.sortByValues(SortOrder.ASCENDING);
        
        assertEquals("Dataset should have no items initially", 0, dataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void testHashCodeComputation() throws Throwable {
        DefaultKeyedValues<Integer> keyedValues = new DefaultKeyedValues<>();
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>(keyedValues);
        
        pieDataset.hashCode(); // Ensure no exceptions are thrown
    }

    @Test(timeout = 4000)
    public void testRemoveWithNullKey() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        try {
            dataset.remove(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetIndexForNonExistentKey() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        
        int index = pieDataset.getIndex(key);
        
        assertEquals("Index should be -1 for a non-existent key", -1, index);
    }

    @Test(timeout = 4000)
    public void testGetKeysForEmptyDataset() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        List<Integer> keys = pieDataset.getKeys();
        
        assertTrue("Keys list should be empty for an empty dataset", keys.isEmpty());
    }

    @Test(timeout = 4000)
    public void testEqualityAfterCopyingDataset() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        pieDataset.setValue(key, null);
        
        DefaultPieDataset<Integer> copiedPieDataset = new DefaultPieDataset<>(pieDataset);
        boolean areEqual = copiedPieDataset.equals(pieDataset);
        
        assertTrue("Copied dataset should be equal to the original", areEqual);
    }

    @Test(timeout = 4000)
    public void testSortByKeysWithNullOrder() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        
        try {
            dataset.sortByKeys(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetValueWithUnrecognizedKey() throws Throwable {
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>();
        Integer key = JLayeredPane.DRAG_LAYER;
        
        try {
            pieDataset.getValue(key);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.DefaultKeyedValues", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertValueOutOfBoundsInEmptyDataset() throws Throwable {
        DefaultKeyedValues<Integer> keyedValues = new DefaultKeyedValues<>();
        DefaultPieDataset<Integer> pieDataset = new DefaultPieDataset<>(keyedValues);
        Integer key = JLayeredPane.POPUP_LAYER;
        
        try {
            pieDataset.insertValue(1, key, -3722.9802);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.DefaultKeyedValues", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneDataset() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        DefaultPieDataset clonedDataset = (DefaultPieDataset) dataset.clone();
        
        assertTrue("Cloned dataset should notify listeners", clonedDataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testInsertValueOutOfBoundsInEmptyKeyedValuesDataset() throws Throwable {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        Integer key = JLayeredPane.DEFAULT_LAYER;
        
        try {
            dataset.insertValue(1, key, key);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.DefaultKeyedValues", e);
        }
    }
}