package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that calling getValue() on a SegmentConstantPool initialized with null CpBands
     * throws a NullPointerException, as it cannot proceed without its required dependency.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNPEWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Call getValue(). This is expected to fail immediately because it will
        // attempt to access the null CpBands object. The arguments are representative
        // values to trigger the method's logic.
        segmentConstantPool.getValue(SegmentConstantPool.CP_INT, 2L);

        // Assert: The test is successful if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}