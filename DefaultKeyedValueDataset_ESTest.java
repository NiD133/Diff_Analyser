package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultKeyedValueDataset} class.
 * These tests focus on construction, data manipulation, and object behavior
 * like equality, hashing, and cloning.
 */
public class DefaultKeyedValueDatasetTest {

    //region Constructor Tests

    @Test
    public void defaultConstructor_shouldCreateEmptyDataset() {
        // Arrange & Act
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Assert
        assertNull("An empty dataset should have a null key.", dataset.getKey());
        assertNull("An empty dataset should have a null value.", dataset.getValue());
    }

    @Test
    public void constructorWithKeyAndValue_shouldInitializeState() {
        // Arrange
        Comparable<String> key = "Test Key";
        Number value = 123.45;

        // Act
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);

        // Assert
        assertEquals("The key should be correctly initialized.", key, dataset.getKey());
        assertEquals("The value should be correctly initialized.", value, dataset.getValue());
    }

    @Test
    public void constructorWithKeyAndNullValue_shouldInitializeState() {
        // Arrange
        Comparable<String> key = "Test Key";

        // Act
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, null);

        // Assert
        assertEquals("The key should be correctly initialized.", key, dataset.getKey());
        assertNull("The value should be null as initialized.", dataset.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullKey_shouldThrowIllegalArgumentException() {
        // Arrange, Act & Assert
        new DefaultKeyedValueDataset(null, 100);
    }

    //endregion

    //region Setter and Getter Tests

    @Test
    public void setValue_onEmptyDataset_shouldSetKeyAndValue() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        Comparable<String> key = "New Key";
        Number value = 200;

        // Act
        dataset.setValue(key, value);

        // Assert
        assertEquals("The key should be updated.", key, dataset.getKey());
        assertEquals("The value should be updated.", value, dataset.getValue());
    }

    @Test
    public void setValue_onPopulatedDataset_shouldUpdateKeyAndValue() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("Old Key", 100);
        Comparable<String> newKey = "New Key";
        Number newValue = 200;

        // Act
        dataset.setValue(newKey, newValue);

        // Assert
        assertEquals("The key should be updated to the new key.", newKey, dataset.getKey());
        assertEquals("The value should be updated to the new value.", newValue, dataset.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValue_withNullKey_shouldThrowIllegalArgumentException() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act & Assert
        dataset.setValue(null, 100);
    }

    @Test
    public void updateValue_onPopulatedDataset_changesValueButNotKey() {
        // Arrange
        Comparable<String> originalKey = "Original Key";
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(originalKey, 100);
        Number newValue = 500.5;

        // Act
        dataset.updateValue(newValue);

        // Assert
        assertEquals("The key should remain unchanged.", originalKey, dataset.getKey());
        assertEquals("The value should be updated.", newValue, dataset.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void updateValue_onEmptyDataset_shouldThrowRuntimeException() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();

        // Act & Assert
        dataset.updateValue(100);
    }

    //endregion

    //region Equals, HashCode, and Clone Tests

    @Test
    public void equals_shouldReturnTrueForSameInstance() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("Key", 10);

        // Act & Assert
        assertTrue("A dataset should be equal to itself.", dataset.equals(dataset));
    }

    @Test
    public void equals_shouldReturnTrueForIdenticalDatasets() {
        // Arrange
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key", 10);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key", 10);

        // Act & Assert
        assertTrue("Two datasets with the same key and value should be equal.", dataset1.equals(dataset2));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentKeys() {
        // Arrange
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key1", 10);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key2", 10);

        // Act & Assert
        assertFalse("Datasets with different keys should not be equal.", dataset1.equals(dataset2));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentValues() {
        // Arrange
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key", 10);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key", 20);

        // Act & Assert
        assertFalse("Datasets with different values should not be equal.", dataset1.equals(dataset2));
    }
    
    @Test
    public void equals_shouldReturnFalseWhenComparingWithNullValue() {
        // Arrange
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key", 10);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key", null);

        // Act & Assert
        assertFalse("A dataset with a value should not equal one with a null value.", dataset1.equals(dataset2));
        assertFalse("A dataset with a null value should not equal one with a value.", dataset2.equals(dataset1));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentObjectTypes() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("Key", 10);
        String otherObject = "Not a dataset";

        // Act & Assert
        assertFalse("A dataset should not be equal to an object of a different type.", dataset.equals(otherObject));
    }

    @Test
    public void equals_shouldReturnFalseForNull() {
        // Arrange
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset("Key", 10);

        // Act & Assert
        assertFalse("A dataset should not be equal to null.", dataset.equals(null));
    }

    @Test
    public void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Key", 100);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Key", 100);

        // Act & Assert
        assertEquals("Equal datasets should have the same hash code.", dataset1.hashCode(), dataset2.hashCode());
    }

    @Test
    public void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Original Key", 100);

        // Act
        DefaultKeyedValueDataset clone = (DefaultKeyedValueDataset) original.clone();

        // Assert
        assertNotSame("Clone should be a different instance from the original.", original, clone);
        assertEquals("Clone should be equal to the original.", original, clone);

        // Further assert independence by modifying the clone
        clone.setValue("Cloned Key", 200);
        assertFalse("Modifying the clone should not affect the original.", original.equals(clone));
        assertEquals("Original key should be unchanged.", "Original Key", original.getKey());
        assertEquals("Original value should be unchanged.", 100, original.getValue());
    }

    //endregion
}