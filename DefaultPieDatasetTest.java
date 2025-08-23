package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest implements DatasetChangeListener {

    private DatasetChangeEvent lastEvent;

    /**
     * Sets up the test environment by resetting the last event.
     */
    @BeforeEach
    public void setUp() {
        this.lastEvent = null;
    }

    /**
     * Records the last dataset change event.
     *
     * @param event  the dataset change event.
     */
    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastEvent = event;
    }

    /**
     * Tests the {@link DefaultPieDataset#clear()} method to ensure it behaves correctly.
     */
    @Test
    public void testClear() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.addChangeListener(this);

        // Test clearing an already empty dataset
        dataset.clear();
        assertNull(this.lastEvent, "No event should be generated when clearing an empty dataset.");

        // Test clearing a non-empty dataset
        dataset.setValue("A", 1.0);
        assertEquals(1, dataset.getItemCount(), "Dataset should contain one item.");
        dataset.clear();
        assertNotNull(this.lastEvent, "An event should be generated when clearing a non-empty dataset.");
        assertEquals(0, dataset.getItemCount(), "Dataset should be empty after clearing.");
    }

    /**
     * Tests the {@link DefaultPieDataset#getKey(int)} method for correct key retrieval.
     */
    @Test
    public void testGetKey() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);

        assertEquals("A", dataset.getKey(0), "The key at index 0 should be 'A'.");
        assertEquals("B", dataset.getKey(1), "The key at index 1 should be 'B'.");

        // Test for invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(-1), "Should throw exception for negative index.");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(2), "Should throw exception for index out of bounds.");
    }

    /**
     * Tests the {@link DefaultPieDataset#getIndex(Object)} method for correct index retrieval.
     */
    @Test
    public void testGetIndex() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);

        assertEquals(0, dataset.getIndex("A"), "The index of 'A' should be 0.");
        assertEquals(1, dataset.getIndex("B"), "The index of 'B' should be 1.");
        assertEquals(-1, dataset.getIndex("XX"), "The index of an unknown key should be -1.");

        // Test for null key
        assertThrows(IllegalArgumentException.class, () -> dataset.getIndex(null), "Should throw exception for null key.");
    }

    /**
     * Tests the cloning functionality of the {@link DefaultPieDataset} class.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("V1", 1);
        original.setValue("V2", null);
        original.setValue("V3", 3);

        DefaultPieDataset<String> clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different instance.");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same class.");
        assertEquals(original, clone, "Clone should be equal to the original.");
    }

    /**
     * Tests the serialization and deserialization of a {@link DefaultPieDataset} instance.
     */
    @Test
    public void testSerialization() {
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("C1", 234.2);
        original.setValue("C2", null);
        original.setValue("C3", 345.9);
        original.setValue("C4", 452.7);

        DefaultPieDataset<String> deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized dataset should be equal to the original.");
    }

    /**
     * Tests for bug report https://github.com/jfree/jfreechart/issues/212.
     */
    @Test
    public void testBug212() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Test for invalid index access
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(-1), "Should throw exception for negative index.");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(0), "Should throw exception for index out of bounds.");

        // Test valid index access
        dataset.setValue("A", 1.0);
        assertEquals(1.0, dataset.getValue(0), "The value at index 0 should be 1.0.");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(1), "Should throw exception for index out of bounds.");
    }
}