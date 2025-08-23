package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * This test suite focuses on the SegmentConstantPool class.
 * This specific test case verifies the behavior of the getClassSpecificPoolEntry method
 * when the SegmentConstantPool is in an invalid state.
 */
public class SegmentConstantPool_ESTestTest60 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that getClassSpecificPoolEntry throws a NullPointerException if the
     * SegmentConstantPool was constructed with null CpBands.
     *
     * The method relies on the internal 'bands' field, which is initialized in the
     * constructor. Providing a null 'bands' object leads to an invalid state,
     * and any method attempting to access it should fail fast.
     *
     * @throws Pack200Exception This exception is declared by the method under test,
     *                          but a NullPointerException is expected before it can be thrown.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getClassSpecificPoolEntryThrowsNPEWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null bands, which is an invalid state.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to get a class-specific pool entry. This is expected to throw
        // a NullPointerException because the internal 'bands' field is null.
        // The specific arguments for cp, index, and class name are arbitrary
        // as the NullPointerException should occur before they are processed.
        segmentConstantPool.getClassSpecificPoolEntry(SegmentConstantPool.CP_FIELD, 4, "anyClassName");

        // Assert: The 'expected' attribute of the @Test annotation handles the
        // verification that a NullPointerException was thrown.
    }
}