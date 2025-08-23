package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * This test class contains tests for the SimpleBloomFilter.
 * Note: This specific test focuses on the state of a SimpleBloomFilter
 * created by flattening an empty LayeredBloomFilter.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that a SimpleBloomFilter created by flattening an empty LayeredBloomFilter
     * is not considered full.
     */
    @Test
    public void isFull_shouldReturnFalse_whenFilterIsCreatedFromEmptyLayers() {
        // Arrange
        // 1. Define a shape for the Bloom filters. The specific dimensions are not
        // critical for this test.
        final Shape shape = Shape.fromNM(10, 2);

        // 2. Mock a LayerManager to behave as if it contains no data.
        // The processBloomFilters method returning 'false' signifies that no filters
        // were processed, effectively making the layer empty.
        @SuppressWarnings("unchecked") // Required for mocking generic types
        final LayerManager<SparseBloomFilter> mockLayerManager = mock(LayerManager.class);
        doReturn(false).when(mockLayerManager).processBloomFilters(any(Predicate.class));

        // 3. Create a LayeredBloomFilter that will be empty due to the mock's behavior.
        final LayeredBloomFilter<SparseBloomFilter> emptyLayeredFilter = new LayeredBloomFilter<>(shape, mockLayerManager);

        // Act
        // Flatten the empty layered filter. The result is the SimpleBloomFilter under test.
        final SimpleBloomFilter flattenedFilter = emptyLayeredFilter.flatten();

        // Assert
        // An empty Bloom filter should not be full.
        assertFalse("A filter created by flattening empty layers should not be full.", flattenedFilter.isFull());
    }
}