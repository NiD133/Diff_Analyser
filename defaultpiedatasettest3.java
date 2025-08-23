package org.jfree.data.general;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test suite for the getIndex() method in the DefaultPieDataset class.
 */
@DisplayName("DefaultPieDataset.getIndex()")
class DefaultPieDatasetGetIndexTest {

    private DefaultPieDataset<String> dataset;

    @BeforeEach
    void setUp() {
        dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);
    }

    @Test
    @DisplayName("should return the correct index for an existing key")
    void getIndex_withExistingKey_shouldReturnCorrectIndex() {
        // Act & Assert
        assertEquals(0, dataset.getIndex("A"), "The index of the first key 'A' should be 0.");
        assertEquals(1, dataset.getIndex("B"), "The index of the second key 'B' should be 1.");
    }

    @Test
    @DisplayName("should return -1 for a non-existent key")
    void getIndex_withNonExistentKey_shouldReturnNegativeOne() {
        // Arrange
        String nonExistentKey = "XX";

        // Act
        int index = dataset.getIndex(nonExistentKey);

        // Assert
        assertEquals(-1, index, "The index for a non-existent key should be -1.");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for a null key")
    void getIndex_withNullKey_shouldThrowIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            dataset.getIndex(null);
        }, "Passing a null key to getIndex() should throw an IllegalArgumentException.");
    }
}