package org.jfree.data.general;

import org.jfree.chart.api.SortOrder;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.UnknownKeyException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable tests for DefaultPieDataset focusing on core behaviors:
 * - construction and basic queries
 * - adding, inserting, updating, and removing values
 * - sorting, equality/hashCode, cloning
 * - argument validation and error handling
 */
public class DefaultPieDatasetTest {

    // ---------- Construction and basics ----------

    @Test
    public void emptyDataset_hasZeroItems_andNoKeys() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        assertEquals(0, ds.getItemCount());
        assertTrue(ds.getKeys().isEmpty());
        // default change notification is enabled
        assertTrue(ds.getNotify());
    }

    @Test
    public void constructFromKeyedValues_copiesContent() {
        DefaultKeyedValues<String> src = new DefaultKeyedValues<>();
        src.addValue("A", 1);
        src.addValue("B", 2);

        DefaultPieDataset<String> ds = new DefaultPieDataset<>(src);

        assertEquals(2, ds.getItemCount());
        assertEquals("A", ds.getKey(0));
        assertEquals(1, ds.getValue("A"));
        assertEquals("B", ds.getKey(1));
        assertEquals(2, ds.getValue("B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructFromKeyedValues_nullSource_throws() {
        new DefaultPieDataset<String>(null);
    }

    // ---------- Adding / updating values ----------

    @Test
    public void setValue_addsAndUpdates_valueAccessibleByKeyAndIndex() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 1.0);
        assertEquals(1, ds.getItemCount());
        assertEquals("A", ds.getKey(0));
        assertEquals(1.0, ds.getValue(0).doubleValue(), 0.0);
        assertEquals(1.0, ds.getValue("A").doubleValue(), 0.0);

        // update existing key
        ds.setValue("A", 2.5);
        assertEquals(1, ds.getItemCount());
        assertEquals(2.5, ds.getValue("A").doubleValue(), 0.0);
    }

    @Test
    public void setValue_allowsNullValue() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", (Number) null);
        assertNull(ds.getValue("A"));
        assertNull(ds.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValue_nullKey_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue(null, 1.0);
    }

    // ---------- Inserting at a position ----------

    @Test
    public void insertValue_insertsAtPosition_andShiftsIndices() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 1);
        ds.setValue("B", 2);
        // Current order is ["A", "B"], insert "C" at position 1
        ds.insertValue(1, "C", 3);

        assertEquals(3, ds.getItemCount());
        assertEquals("A", ds.getKey(0));
        assertEquals("C", ds.getKey(1));
        assertEquals("B", ds.getKey(2));
        assertEquals(1, ds.getIndex("A"));
        assertEquals(0, ds.getIndex("A")); // sanity check original remains at 0
        assertEquals(1, ds.getIndex("C"));
        assertEquals(2, ds.getIndex("B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertValue_positionOutOfBounds_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        // valid positions are [0..getItemCount()], here count is 0
        ds.insertValue(1, "X", 1.0);
    }

    // ---------- Removing and clearing ----------

    @Test
    public void clear_removesAllItems() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 1);
        ds.clear();
        assertEquals(0, ds.getItemCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void remove_unknownKey_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.remove("missing");
    }

    // ---------- Queries and error handling ----------

    @Test
    public void getIndex_unknownKey_returnsMinusOne() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 1);
        assertEquals(-1, ds.getIndex("B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getIndex_nullKey_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.getIndex(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getKey_negativeIndex_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.getKey(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getValue_indexOutOfBounds_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.getValue(0);
    }

    @Test
    public void getKeys_isUnmodifiable() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 1);
        List<String> keys = ds.getKeys();
        assertEquals(1, keys.size());
        try {
            keys.add("B");
            fail("Expected UnsupportedOperationException when modifying keys list");
        } catch (UnsupportedOperationException expected) {
            // ok
        }
    }

    @Test
    public void getValue_unknownKey_throwsOrSignalsMissing() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        // JFreeChart may throw UnknownKeyException or IllegalArgumentException, accept either
        try {
            ds.getValue("nope");
            fail("Expected exception for unknown key");
        } catch (UnknownKeyException | IllegalArgumentException expected) {
            // ok
        }
    }

    // ---------- Sorting ----------

    @Test
    public void sortByKeys_ordersByKey() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("b", 2);
        ds.setValue("a", 1);

        ds.sortByKeys(SortOrder.ASCENDING);
        assertEquals("a", ds.getKey(0));
        assertEquals("b", ds.getKey(1));

        ds.sortByKeys(SortOrder.DESCENDING);
        assertEquals("b", ds.getKey(0));
        assertEquals("a", ds.getKey(1));
    }

    @Test
    public void sortByValues_ordersByValue() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.setValue("A", 5);
        ds.setValue("B", 1);
        ds.setValue("C", 3);

        ds.sortByValues(SortOrder.ASCENDING);
        assertEquals("B", ds.getKey(0));
        assertEquals("C", ds.getKey(1));
        assertEquals("A", ds.getKey(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void sortByKeys_nullOrder_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.sortByKeys(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sortByValues_nullOrder_throws() {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        ds.sortByValues(null);
    }

    // ---------- Equality, hashCode, clone ----------

    @Test
    public void equalsAndHashCode_reflectContent() {
        DefaultPieDataset<String> a = new DefaultPieDataset<>();
        DefaultPieDataset<String> b = new DefaultPieDataset<>();
        a.setValue("A", 1);
        b.setValue("A", 1);

        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());

        b.setValue("A", 2);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void clone_createsIndependentCopy() throws CloneNotSupportedException {
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("A", 1);

        DefaultPieDataset<String> copy = (DefaultPieDataset<String>) original.clone();
        assertNotSame(original, copy);
        assertTrue(original.equals(copy));

        // change the clone, original should not change
        copy.setValue("A", 2);
        assertEquals(1, original.getValue("A"));
        assertEquals(2, copy.getValue("A"));
        assertFalse(original.equals(copy));
    }
}