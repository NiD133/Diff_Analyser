package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This class contains tests for {@link SegmentConstantPool}.
 * This specific test case was improved for clarity and maintainability.
 */
public class SegmentConstantPool_ESTestTest16 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that getConstantPoolEntry throws a NullPointerException when the
     * SegmentConstantPool is constructed with null CpBands.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryThrowsNPEWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with a null dependency (CpBands).
        // This simulates a state where the constant pool bands are not available.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        long arbitraryIndex = 11L;

        // Act & Assert: Attempt to retrieve a constant pool entry.
        // This action is expected to throw a NullPointerException because the method
        // will internally try to access the null CpBands dependency.
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_DOUBLE, arbitraryIndex);
    }
}