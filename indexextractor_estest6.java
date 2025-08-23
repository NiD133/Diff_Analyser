package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * This test class contains the improved test case.
 * The original test was part of an auto-generated suite (IndexExtractor_ESTestTest6).
 */
public class IndexExtractor_ESTestTest6 extends IndexExtractor_ESTest_scaffolding {

    /**
     * Tests that creating a SimpleBloomFilter with a shape requiring an extremely
     * large memory allocation throws an OutOfMemoryError.
     *
     * <p>This test verifies the behavior of the system when requested to create a
     * Bloom filter that is too large to fit into the available heap space. The
     * constructor for SimpleBloomFilter is expected to fail during the allocation
     * of its internal bitmap.</p>
     *
     * <p><b>Note:</b> This type of test can be brittle as it is dependent on the
     * JVM's memory configuration. It is designed to verify behavior under
     * resource constraints.</p>
     */
    @Test(timeout = 4000, expected = OutOfMemoryError.class)
    public void creatingFilterWithExtremelyLargeShapeShouldThrowOutOfMemoryError() {
        // A shape with a number of bits close to Integer.MAX_VALUE requires a
        // bitmap of approximately 256 MiB, which is likely to exceed the
        // default heap size for a test environment.
        final int hugeNumberOfBits = 2_147_352_576;
        final int hugeNumberOfItems = 2_147_352_576;
        Shape extremelyLargeShape = Shape.fromNM(hugeNumberOfItems, hugeNumberOfBits);

        // The following line is expected to throw an OutOfMemoryError because it
        // attempts to allocate a long array of over 33 million elements.
        // The original test called asIndexArray() after this, but execution
        // should never reach that point.
        new SimpleBloomFilter(extremelyLargeShape);
    }
}