package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * This test class focuses on the behavior of SegmentConstantPool.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class SegmentConstantPool_ESTestTest59 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that {@link SegmentConstantPool#getClassSpecificPoolEntry(int, long, String)}
     * throws a NullPointerException if the pool was constructed with null CpBands.
     *
     * This test ensures that the method fails fast when the SegmentConstantPool is in an
     * invalid state (i.e., not properly initialized with constant pool bands).
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void getClassSpecificPoolEntryThrowsNPEWhenBandsIsNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null CpBands, which represents an
        // invalid initialization state for the object.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to get a class-specific pool entry. This is expected to throw a
        // NullPointerException because the internal 'bands' field is null and will be
        // dereferenced. The specific arguments passed to the method are arbitrary, as
        // the exception should occur before they are used.
        final int constantPoolType = SegmentConstantPool.CP_METHOD;
        final long entryIndex = 11L;
        final String className = "any.class.Name";

        segmentConstantPool.getClassSpecificPoolEntry(constantPoolType, entryIndex, className);

        // Assert: The test is successful if a NullPointerException is thrown.
        // This is handled declaratively by the `expected` attribute of the @Test annotation.
    }
}