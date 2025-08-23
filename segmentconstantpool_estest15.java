package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that getConstantPoolEntry() throws a NullPointerException when the
     * SegmentConstantPool is constructed with null CpBands. This is because the
     * internal bands object is required to look up any constant pool entry.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryShouldThrowNPEWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null bands, representing an invalid state.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int arbitraryType = SegmentConstantPool.CP_CLASS;
        final long arbitraryIndex = 1152L;

        // Act & Assert: Attempting to get an entry should trigger a NullPointerException,
        // which is caught and verified by the @Test(expected) annotation.
        segmentConstantPool.getConstantPoolEntry(arbitraryType, arbitraryIndex);
    }
}