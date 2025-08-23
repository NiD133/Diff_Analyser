package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

// The test class name and inheritance are preserved from the original generated code.
public class SimpleBloomFilter_ESTestTest15 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that attempting to process indices on a Bloom filter with an extremely large
     * shape (approaching Integer.MAX_VALUE bits) results in an OutOfMemoryError.
     *
     * <p>This test case validates the system's behavior under extreme memory allocation
     * pressure, which was the likely intent of the original auto-generated test that
     * expected an "Undeclared exception!".</p>
     */
    @Test(timeout = 4000, expected = OutOfMemoryError.class)
    public void processIndicesWithExtremelyLargeFilterShouldThrowOutOfMemoryError() {
        // Arrange: Create a Bloom filter with a shape that requires a massive bitmap array.
        // The number of bits is set close to Integer.MAX_VALUE. This will require
        // a long[] of size 33,554,432, consuming over 256MB of heap space.
        // On a typical test environment, this is expected to exhaust available memory.
        final int hugeNumberOfBits = 2_147_483_605;
        final Shape massiveShape = Shape.fromNM(hugeNumberOfBits, hugeNumberOfBits);

        // Note: The OutOfMemoryError might be thrown during the creation of these
        // objects. The test will still pass, as it correctly verifies that such large
        // structures cannot be handled with default memory settings.
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(massiveShape);
        final IndexFilter.ArrayTracker tracker = new IndexFilter.ArrayTracker(massiveShape);

        // Act: Attempt to process the indices of the massive filter.
        bloomFilter.processIndices(tracker);

        // Assert: The test is expected to throw an OutOfMemoryError.
        // This is handled by the 'expected' parameter in the @Test annotation.
        // If no exception is thrown, the test will fail.
    }
}