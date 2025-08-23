package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class SegmentConstantPool_ESTestTest47 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that getConstantPoolEntry() throws a NullPointerException if the
     * SegmentConstantPool was constructed with null CpBands.
     */
    @Test
    public void getConstantPoolEntryShouldThrowNullPointerExceptionWhenBandsAreNull() {
        // Arrange: Create a SegmentConstantPool with null CpBands. This simulates an
        // invalid state where a required dependency is missing.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final long arbitraryIndex = 139L;

        // Act & Assert: Verify that calling getConstantPoolEntry throws a NullPointerException.
        // This is expected because the method will attempt to access the null bands object.
        // The constant CP_IMETHOD is used as a representative type; any type would trigger the error.
        assertThrows(NullPointerException.class, () -> {
            segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_IMETHOD, arbitraryIndex);
        });
    }
}