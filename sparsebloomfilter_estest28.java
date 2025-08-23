package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that the constructor throws a NullPointerException when the provided
     * Shape is null, as required by the contract.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullShape() {
        // The constructor uses Objects.requireNonNull(shape, "shape"), so we expect
        // a NullPointerException with the message "shape".
        NullPointerException e = assertThrows(NullPointerException.class, () -> {
            new SparseBloomFilter(null);
        });

        assertEquals("shape", e.getMessage());
    }
}