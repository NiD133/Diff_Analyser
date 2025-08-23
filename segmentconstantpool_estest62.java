package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that attempting to get a constant pool entry from a SegmentConstantPool
     * initialized with null CpBands throws a NullPointerException. This is expected
     * because the internal bands object is required for this operation.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryShouldThrowNPEWhenBandsAreNull() {
        // Arrange: Create a SegmentConstantPool with null bands.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Define arbitrary arguments for the method call.
        final int arbitraryType = SegmentConstantPool.CP_INT;
        final long arbitraryIndex = 1137L;

        // Act & Assert: Attempt to retrieve an entry, which should trigger a
        // NullPointerException. The exception is caught and verified by the
        // @Test(expected=...) annotation.
        segmentConstantPool.getConstantPoolEntry(arbitraryType, arbitraryIndex);
    }
}