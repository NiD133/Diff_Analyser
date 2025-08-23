package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Contains tests for the constructor of {@link ByteArrayBuilder}.
 */
public class ByteArrayBuilderConstructorTest {

    /**
     * Verifies that the constructor propagates an {@link IndexOutOfBoundsException}
     * when given a {@link BufferRecycler} that is improperly configured.
     * <p>
     * The {@link ByteArrayBuilder} constructor attempts to allocate a buffer from the
     * provided recycler. If the recycler is initialized with parameters that prevent
     * this allocation (for example, by having internal arrays that are too small),
     * the constructor should fail by propagating the underlying exception.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void constructorWithMisconfiguredRecyclerShouldThrowException() {
        // GIVEN a BufferRecycler initialized with non-standard, zero-sized buffers.
        // This specific configuration is known to cause an internal IndexOutOfBoundsException
        // when ByteArrayBuilder requests its standard "write concatenation buffer".
        BufferRecycler misconfiguredRecycler = new BufferRecycler(0, 0);

        // WHEN a new ByteArrayBuilder is created with this faulty recycler
        // THEN an IndexOutOfBoundsException is expected to be thrown and propagated.
        new ByteArrayBuilder(misconfiguredRecycler, 0);
    }
}