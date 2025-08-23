package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test class contains an improved test case for the SegmentConstantPool class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class SegmentConstantPool_ESTestTest35 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that calling {@link SegmentConstantPool#getValue(int, long)} throws a
     * {@code NullPointerException} if the {@code SegmentConstantPool} was constructed
     * with a null {@code CpBands} object. This ensures the method correctly handles
     * an invalid internal state.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void getValueShouldThrowNPEWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with null CpBands. This simulates an
        // invalid state where the necessary band data is missing.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a value from the constant pool.
        // This action is expected to fail because the internal 'bands' field is null.
        // The arguments (CP_DESCR, 7L) are arbitrary values chosen to trigger the method call.
        segmentConstantPool.getValue(SegmentConstantPool.CP_DESCR, 7L);

        // Assert: The test passes if a NullPointerException is thrown, which is
        // verified by the `expected` attribute of the @Test annotation.
    }
}