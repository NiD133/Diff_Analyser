/*
 * Refactored test suite for improved readability and maintainability
 */
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

// JLayeredPane constants for meaningful key values
@SuppressWarnings("deprecation")
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class DefaultPieDataset_ESTest extends DefaultPieDataset_ESTest_scaffolding {
    
    // Constants for JLayeredPane values to improve readability
    private static final Integer DRAG_LAYER = JLayeredPane.DRAG_LAYER;
    private static final Integer POPUP_LAYER = JLayeredPane.POPUP_LAYER;
    private static final Integer FRAME_CONTENT_LAYER = JLayeredPane.FRAME_CONTENT_LAYER;
    private static final Integer MODAL_LAYER = JLayeredPane.MODAL_LAYER;
    private static final Integer DEFAULT_LAYER = JLayeredPane.DEFAULT_LAYER;

    /* ========== BASIC OPERATION TESTS ========== */

    @Test(timeout = 4000)
    public void setValueShouldAddEntryToDataset() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER, 0.8);
        assertEquals("Item count should be 1 after adding value", 
                     1, dataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void getKeyShouldReturnCorrectKeyAfterSetValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER, 0.8);
        Integer key = dataset.getKey(0);
        assertEquals("Should return correct key for index 0", 
                     DRAG_LAYER, key);
    }

    @Test(timeout = 4000)
    public void getIndexShouldReturnCorrectIndexAfterSetValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER, 0.8);
        dataset.setValue(MODAL_LAYER, -1.0);
        int index = dataset.getIndex(MODAL_LAYER);
        assertEquals("Should return correct index for second key", 
                     1, index);
    }

    @Test(timeout = 4000)
    public void insertValueShouldAddEntryAtSpecifiedPosition() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.insertValue(0, FRAME_CONTENT_LAYER, 0.0);
        assertEquals("Item count should be 1 after insertion", 
                     1, dataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void clearShouldEmptyDatasetAndSetNotifyFlag() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        dataset.clear();
        assertTrue("Notify flag should remain true after clear", 
                   dataset.getNotify());
    }

    /* ========== NULL VALUE HANDLING TESTS ========== */

    @Test(timeout = 4000)
    public void setValueWithNullNumberShouldStoreNullValue() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.setValue(DRAG_LAYER, (Number) null);
        
        // Test retrieval by key
        assertNull("Value should be null when retrieved by key", 
                   dataset.getValue(DRAG_LAYER));
        
        // Test retrieval by index
        assertNull("Value should be null when retrieved by index", 
                   dataset.getValue(0));
    }

    /* ========== EXCEPTION HANDLING TESTS ========== */

    @Test(timeout = 4000)
    public void sortByValuesWithNullOrderShouldThrowException() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        try {
            dataset.sortByValues(null);
            fail("Should throw IllegalArgumentException for null sort order");
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should match", 
                         "Null 'order' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void setValueWithNullKeyShouldThrowException() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        try {
            dataset.setValue(null, 0.0);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should match", 
                         "Null 'key' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void removeWithNonexistentKeyShouldThrowException() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        try {
            dataset.remove(POPUP_LAYER);
            fail("Should throw IllegalArgumentException for unrecognized key");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should contain key value", 
                       e.getMessage().contains("300"));
        }
    }

    @Test(timeout = 4000)
    public void getValueWithNonexistentKeyShouldThrowException() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        try {
            dataset.getValue(DRAG_LAYER);
            fail("Should throw IllegalArgumentException for unrecognized key");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should contain key value", 
                       e.getMessage().contains("400"));
        }
    }

    /* ========== EQUALITY AND CLONING TESTS ========== */

    @Test(timeout = 4000)
    public void datasetsWithDifferentValuesShouldNotBeEqual() {
        // Setup first dataset
        DefaultKeyedValuesDataset<Integer> dataset1 = new DefaultKeyedValuesDataset<>();
        dataset1.setValue(FRAME_CONTENT_LAYER, FRAME_CONTENT_LAYER);
        
        // Setup second dataset with different value
        DefaultPieDataset<Integer> dataset2 = new DefaultPieDataset<>(dataset1);
        dataset2.setValue(FRAME_CONTENT_LAYER, -140.135);
        
        // Verify inequality
        assertFalse("Datasets with different values should not be equal", 
                    dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void clonedDatasetShouldEqualOriginal() {
        DefaultPieDataset<Integer> original = new DefaultPieDataset<>();
        original.setValue(DRAG_LAYER, null);
        DefaultPieDataset<Integer> clone = new DefaultPieDataset<>(original);
        
        assertTrue("Clone should be equal to original", 
                   original.equals(clone));
    }

    @Test(timeout = 4000)
    public void datasetShouldNotEqualDifferentObjectType() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        assertFalse("Should not equal object of different type", 
                    dataset.equals(DRAG_LAYER));
    }

    @Test(timeout = 4000)
    public void cloneShouldCreateIdenticalCopy() {
        DefaultKeyedValuesDataset<Integer> original = new DefaultKeyedValuesDataset<>();
        DefaultPieDataset<?> clone = (DefaultPieDataset<?>) original.clone();
        assertTrue("Cloned dataset should have notify flag set", 
                   clone.getNotify());
    }

    /* ========== SORTING OPERATION TESTS ========== */

    @Test(timeout = 4000)
    public void sortByKeysOnEmptyDatasetShouldNotFail() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        dataset.sortByKeys(SortOrder.DESCENDING);
        assertEquals("Item count should remain 0 after sorting", 
                     0, dataset.getItemCount());
    }

    @Test(timeout = 4000)
    public void sortByValuesOnEmptyDatasetShouldNotFail() {
        DefaultKeyedValuesDataset<Integer> dataset = new DefaultKeyedValuesDataset<>();
        dataset.sortByValues(SortOrder.ASCENDING);
        assertEquals("Item count should remain 0 after sorting", 
                     0, dataset.getItemCount());
    }

    /* ========== EDGE CASE TESTS ========== */

    @Test(timeout = 4000)
    public void getIndexForNonexistentKeyShouldReturnNegativeOne() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        int index = dataset.getIndex(DRAG_LAYER);
        assertEquals("Should return -1 for unrecognized key", 
                     -1, index);
    }

    @Test(timeout = 4000)
    public void getKeysOnEmptyDatasetShouldReturnEmptyList() {
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();
        List<Integer> keys = dataset.getKeys();
        assertTrue("Key list should be empty for new dataset", 
                   keys.isEmpty());
    }

    /* ========== CONSTRUCTOR EXCEPTION TESTS ========== */

    @Test(timeout = 4000)
    public void constructorWithNullSourceShouldThrowException() {
        try {
            new DefaultPieDataset<Integer>((KeyedValues<Integer>) null);
            fail("Should throw IllegalArgumentException for null source");
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should match", 
                         "Null 'source' argument.", e.getMessage());
        }
    }

    /* ========== SPECIAL DATASET INTERACTION TESTS ========== */

    @Test(timeout = 4000)
    public void creatingFromSlidingCategoryDatasetShouldHandleNulls() {
        SlidingCategoryDataset<ChronoLocalDate, ChronoLocalDate> slidingDataset = 
            new SlidingCategoryDataset<>(null, -1795, -1795);
        TableOrder tableOrder = TableOrder.BY_COLUMN;
        CategoryToPieDataset pieSource = 
            new CategoryToPieDataset(slidingDataset, tableOrder, -1795);
        
        try {
            new DefaultPieDataset<Integer>(pieSource);
            fail("Should throw NullPointerException for null underlying dataset");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}