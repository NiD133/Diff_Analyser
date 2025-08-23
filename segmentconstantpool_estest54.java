package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

// NOTE: The original test class name and scaffolding are preserved to fit into the existing suite.
// Unused imports from the original file have been removed for clarity for this specific test.
public class SegmentConstantPool_ESTestTest54 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that {@link SegmentConstantPool#getConstantPoolEntry(int, long)} throws a
     * {@code NullPointerException} if the pool was constructed with a null {@code CpBands} instance.
     *
     * This test ensures that the method correctly handles the invalid state where
     * its essential dependency (the bands object) is missing.
     *
     * @throws Pack200Exception This is part of the method signature under test, but it is
     *                          not expected to be thrown in this specific scenario.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getConstantPoolEntryShouldThrowNullPointerExceptionWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with a null CpBands object, which is an
        // invalid setup for retrieving constant pool entries.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act & Assert: Attempt to retrieve a constant pool entry.
        // This call is expected to throw a NullPointerException because the internal 'bands'
        // field is null. The specific arguments (type and index) are arbitrary as the
        // exception should occur before they are processed.
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_LONG, 2L);
    }
}