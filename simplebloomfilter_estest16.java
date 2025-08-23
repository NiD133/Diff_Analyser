package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that passing a null consumer to {@code processIndices} results in a
     * NullPointerException. This verifies the method's input validation.
     */
    @Test
    public void testProcessIndicesShouldThrowExceptionForNullConsumer() {
        // Arrange: Create a bloom filter instance. The specific shape is not
        // relevant for this null-check test.
        final Shape shape = Shape.fromNM(10, 1);
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act & Assert: Call the method with null and verify the exception.
        try {
            bloomFilter.processIndices(null);
            fail("A NullPointerException was expected but not thrown.");
        } catch (final NullPointerException e) {
            // The implementation is expected to use Objects.requireNonNull("consumer", ...),
            // so we verify the message to confirm the check is for the correct parameter.
            assertEquals("consumer", e.getMessage());
        }
    }
}