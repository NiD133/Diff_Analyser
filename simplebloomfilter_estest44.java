package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the SimpleBloomFilter.
 * The original name {@code SimpleBloomFilter_ESTestTest44} was preserved,
 * but a more conventional name would be {@code SimpleBloomFilterTest}.
 */
public class SimpleBloomFilter_ESTestTest44 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that a newly created SimpleBloomFilter reports itself as empty.
     */
    @Test
    public void testIsEmptyReturnsTrueForNewFilter() {
        // Arrange: Create a new, empty SimpleBloomFilter.
        // The specific shape parameters are not critical for this test,
        // as any valid shape will result in an empty filter upon creation.
        Shape shape = Shape.fromNM(100, 10);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act: Check if the filter is empty.
        boolean isEmpty = bloomFilter.isEmpty();

        // Assert: The filter should be empty.
        assertTrue("A newly instantiated SimpleBloomFilter should be empty", isEmpty);
    }
}