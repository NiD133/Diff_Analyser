package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Tests for {@link SimpleBloomFilter#processBitMaps(java.util.function.LongPredicate)}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that processBitMaps() throws a NullPointerException when the provided consumer is null.
     * The method contract requires a non-null consumer to process the bit maps.
     */
    @Test(expected = NullPointerException.class)
    public void processBitMapsShouldThrowNullPointerExceptionWhenConsumerIsNull() {
        // Arrange: Create a filter with any valid shape.
        // The shape's specific values do not affect this test's outcome.
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert: Calling processBitMaps with a null argument should throw.
        // The @Test(expected=...) annotation handles the assertion.
        filter.processBitMaps(null);
    }
}