package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that calling getValue() on a SegmentConstantPool initialized with null bands
     * throws a NullPointerException, as it cannot access the underlying data structures.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNullPointerExceptionWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null CpBands. This simulates an
        // invalid state where the pool has no data source.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to get a value from the constant pool. This should trigger a
        // NullPointerException because the internal 'bands' field is null. The
        // arguments are arbitrary, as any call should fail.
        segmentConstantPool.getValue(SegmentConstantPool.CP_DOUBLE, 1L);

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
    }
}