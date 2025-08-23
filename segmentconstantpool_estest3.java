package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * This test suite focuses on the SegmentConstantPool class.
 * Note: The original test class name 'SegmentConstantPool_ESTestTest3' was preserved.
 * A more conventional name would be 'SegmentConstantPoolTest'.
 */
public class SegmentConstantPool_ESTestTest3 {

    /**
     * Verifies that {@link SegmentConstantPool#getValue(int, long)} throws a
     * {@code NullPointerException} when the pool is constructed with a null {@code CpBands} instance.
     * This is a critical check to ensure that the class fails fast if its essential dependencies
     * are not provided during initialization.
     *
     * @throws Pack200Exception if an unexpected unpacking error occurs, though an NPE is expected here.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getValueShouldThrowNPEIfBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null bands, which is the specific state under test.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a value. This action is expected to throw a
        // NullPointerException because the internal 'bands' field is null.
        // The specific arguments (CP_DESCR, index 9) are arbitrary; any valid
        // arguments would trigger the same exception under this condition.
        segmentConstantPool.getValue(SegmentConstantPool.CP_DESCR, 9);

        // Assert: The test passes if a NullPointerException is thrown, as specified
        // by the 'expected' parameter of the @Test annotation.
    }
}