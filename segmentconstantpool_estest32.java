package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test suite focuses on verifying the behavior of the {@link SegmentConstantPool} class.
 * This specific test case validates the handling of null inputs.
 */
public class SegmentConstantPool_ESTestTest32 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that {@code matchSpecificPoolEntryIndex} throws a {@code NullPointerException}
     * when the primary name array provided is null. The method should not attempt to process
     * a null array and should fail fast.
     */
    @Test(expected = NullPointerException.class)
    public void matchSpecificPoolEntryIndexShouldThrowNullPointerExceptionForNullArray() {
        // Arrange: Create an instance of SegmentConstantPool.
        // The CpBands dependency can be null because it is not used by the method under test.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act & Assert: Call the method with a null array, which is expected to throw.
        // The other arguments can be arbitrary values as the null array should cause an
        // immediate exception.
        segmentConstantPool.matchSpecificPoolEntryIndex(null, "anyString", 0);
    }
}