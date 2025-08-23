package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that calling getValue() on a SegmentConstantPool initialized with null CpBands
     * throws a NullPointerException. This is expected because the method requires access
     * to the band data, which is not available.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNullPointerExceptionWhenBandsAreNull() {
        // Arrange: Create a SegmentConstantPool with null bands, representing an invalid state
        // for operations that require band data.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act & Assert: Attempting to retrieve a value should trigger a NullPointerException
        // because the internal 'bands' field is null. The specific constant pool type and
        // index are arbitrary as the null check happens first.
        segmentConstantPool.getValue(SegmentConstantPool.CP_FLOAT, 1622L);
    }
}