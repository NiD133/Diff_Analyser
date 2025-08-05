package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultPieDatasetTest implements DatasetChangeListener {

    private DatasetChangeEvent lastEvent;

    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastEvent = event;
    }

    @BeforeEach
    public void setup() {
        this.lastEvent = null;
    }

    @Test
    public void clear_WhenDatasetIsEmpty_ShouldNotFireChangeEvent() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.addChangeListener(this);
        
        dataset.clear();
        assertNull(lastEvent, "No event should be fired when clearing empty dataset");
    }

    @Test
    public void clear_WhenDatasetHasData_ShouldFireChangeEventAndClearData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.addChangeListener(this);
        dataset.setValue("A", 1.0);
        
        dataset.clear();
        
        assertNotNull(lastEvent, "Change event should be fired");
        assertEquals(0, dataset.getItemCount(), "Dataset should be empty after clear");
    }

    @Test
    public void getKey_WithValidIndex_ShouldReturnCorrectKey() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);
        
        assertEquals("A", dataset.getKey(0), "Key at index 0 should be 'A'");
        assertEquals("B", dataset.getKey(1), "Key at index 1 should be 'B'");
    }

    @Test
    public void getKey_WithNegativeIndex_ShouldThrowIndexOutOfBoundsException() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(-1),
            "Should throw when accessing negative index");
    }

    @Test
    public void getKey_WithIndexOutOfBounds_ShouldThrowIndexOutOfBoundsException() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(1),
            "Should throw when accessing index beyond dataset size");
    }

    @Test
    public void getIndex_WithExistingKey_ShouldReturnCorrectIndex() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);
        
        assertEquals(0, dataset.getIndex("A"), "Index of 'A' should be 0");
        assertEquals(1, dataset.getIndex("B"), "Index of 'B' should be 1");
    }

    @Test
    public void getIndex_WithNonExistingKey_ShouldReturnNegativeOne() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        
        assertEquals(-1, dataset.getIndex("XX"), "Should return -1 for unknown key");
    }

    @Test
    public void getIndex_WithNullKey_ShouldThrowIllegalArgumentException() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        assertThrows(IllegalArgumentException.class, () -> dataset.getIndex(null),
            "Should throw when key is null");
    }

    @Test
    public void cloning_ShouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("V1", 1);
        original.setValue("V2", null);
        original.setValue("V3", 3);
        
        DefaultPieDataset<String> clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be distinct instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }

    @Test
    public void serialization_ShouldProduceEqualDataset() {
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("C1", 234.2);
        original.setValue("C2", null);
        original.setValue("C3", 345.9);
        original.setValue("C4", 452.7);

        DefaultPieDataset<String> deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized dataset should equal original");
    }

    @Test
    public void getValue_WithInvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        // Test empty dataset
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(-1),
            "Should throw for negative index on empty dataset");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(0),
            "Should throw for valid index on empty dataset");
        
        // Test populated dataset
        dataset.setValue("A", 1.0);
        assertEquals(1.0, dataset.getValue(0), "Valid index should return correct value");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(1),
            "Should throw for index beyond dataset size");
    }
}