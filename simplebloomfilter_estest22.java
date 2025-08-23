package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

/**
 * Contains an improved test case for the {@link SimpleBloomFilter} class.
 * The original test was auto-generated and has been refactored for better
 * understandability and maintainability.
 */
// The test runner and scaffolding are kept from the original generated code.
@RunWith(EvoRunner.class)
public class SimpleBloomFilter_ESTestTest22 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging a hasher with a Bloom filter defined by an impractically
     * large shape results in a timeout.
     *
     * <p>The {@code merge(Hasher)} operation's complexity is proportional to the
     * number of hash functions (k) in the filter's shape. By setting 'k' to a
     * value near {@code Integer.MAX_VALUE}, the operation becomes computationally
     * infeasible to complete within the test's timeout period.</p>
     *
     * <p>Note: This test also requires significant memory to construct the Bloom
     * filter. If it fails with an {@code OutOfMemoryError}, it may indicate an
     * environment with insufficient heap space to even begin the test.</p>
     */
    @Test(timeout = 4000)
    public void mergeWithImpracticallyLargeHasherShouldTimeOut() {
        // Arrange
        // Define a shape with a number of hash functions (k) so large that the
        // subsequent merge operation cannot complete in a reasonable time.
        // The original test used 2_147_483_605, which is Integer.MAX_VALUE - 42.
        final int impracticallyLargeK = 2_147_483_605;
        final int numberOfBits = 2_147_483_605; // Also large, affects memory usage.
        Shape hugeShape = Shape.fromKM(impracticallyLargeK, numberOfBits);

        SimpleBloomFilter filter = new SimpleBloomFilter(hugeShape);

        // A simple hasher is sufficient; the seed values are not critical for this test.
        Hasher hasher = new EnhancedDoubleHasher(1, 1);

        // Act & Assert
        // The merge operation iterates 'k' times. With 'k' being nearly Integer.MAX_VALUE,
        // this loop is expected to exceed the 4-second timeout. The test runner will
        // interrupt the thread and report the test as failed due to the timeout,
        // which is the expected outcome.
        filter.merge(hasher);
    }
}