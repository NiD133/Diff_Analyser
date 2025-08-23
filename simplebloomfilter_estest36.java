package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that the SimpleBloomFilter constructor throws a NullPointerException
     * when the provided Shape is null. The Shape is a mandatory dependency.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenShapeIsNull() {
        // Attempt to create a SimpleBloomFilter with a null Shape.
        // This action is expected to throw a NullPointerException, which is
        // handled and asserted by the @Test(expected=...) annotation.
        new SimpleBloomFilter(null);
    }
}