package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that getConstantPoolEntry() throws a NullPointerException
     * when the SegmentConstantPool is constructed with null CpBands.
     * <p>
     * This test ensures that the method fails fast if its essential dependency (CpBands)
     * is not provided, preventing potential issues later in the process.
     * </p>
     */
    @Test
    public void getConstantPoolEntryShouldThrowNPEWhenBandsAreNull() {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final long arbitraryIndex = 2L;

        // Act & Assert: Expect a NullPointerException when trying to access a constant pool entry.
        // The method internally tries to access the null 'bands' field, which triggers the exception.
        assertThrows(NullPointerException.class, () -> {
            segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_DOUBLE, arbitraryIndex);
        });
    }
}