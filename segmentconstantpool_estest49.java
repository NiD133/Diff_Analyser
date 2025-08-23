package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that getConstantPoolEntry() throws a NullPointerException if the
     * SegmentConstantPool was constructed with a null CpBands dependency.
     *
     * This is expected because the method will attempt to access the null 'bands'
     * field, leading to the exception.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryThrowsNPEWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with its required CpBands dependency set to null.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int entryType = SegmentConstantPool.CP_FIELD; // Use the constant for clarity
        final long arbitraryIndex = 139L; // The specific index value is not relevant for this test

        // Act: Call the method under test.
        // The @Test(expected=...) annotation will automatically assert that a
        // NullPointerException is thrown.
        segmentConstantPool.getConstantPoolEntry(entryType, arbitraryIndex);
    }
}