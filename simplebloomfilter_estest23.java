package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * This test class contains tests for the SimpleBloomFilter class.
 * The original test was auto-generated and has been refactored for clarity.
 */
// The test class name and runner from the original file are kept to preserve
// the test execution context. In a real-world scenario, these might also be
// refactored to be more descriptive and use a standard JUnit runner.
public class SimpleBloomFilter_ESTestTest23 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that calling merge() with a null Hasher throws a NullPointerException.
     * This verifies the input validation on the merge(Hasher) method.
     */
    @Test
    public void mergeWithNullHasherShouldThrowNullPointerException() {
        // Arrange: Create a Bloom filter. The specific shape is not important for
        // this null-check test, so any valid shape will suffice.
        final Shape shape = Shape.fromKM(10, 1024);
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act & Assert: Verify that a NullPointerException is thrown when a null
        // hasher is passed to the merge method.
        final NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            bloomFilter.merge((Hasher) null);
        });

        // Assert that the exception message correctly identifies the null parameter.
        // This is a more standard way to verify the behavior that the original
        // test's `verifyException("java.util.Objects", e)` was checking.
        assertEquals("hasher", exception.getMessage());
    }
}