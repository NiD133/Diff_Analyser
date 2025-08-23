package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that calling getValue() throws a NullPointerException if the SegmentConstantPool
     * was constructed with a null CpBands dependency. This is expected because the method
     * needs to delegate to the bands object, which is null in this scenario.
     */
    @Test(expected = NullPointerException.class)
    public void getValue_whenConstructedWithNullBands_throwsNullPointerException() {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency to
        // simulate a state where the underlying data source is not available.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a value from the constant pool.
        // The specific arguments are representative valid inputs; any call would trigger the NPE.
        segmentConstantPool.getValue(SegmentConstantPool.SIGNATURE, 8L);

        // Assert: The test is configured to pass only if a NullPointerException is thrown,
        // as specified by the @Test(expected = NullPointerException.class) annotation.
    }
}