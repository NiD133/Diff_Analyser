package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test class has been improved for understandability.
 * The original test was auto-generated and difficult to read.
 *
 * Test for {@link SegmentConstantPool}.
 */
public class SegmentConstantPool_ESTestTest52 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that calling {@code getConstantPoolEntry} on a {@code SegmentConstantPool}
     * instance that was initialized with null {@code CpBands} throws a
     * {@code NullPointerException}.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getConstantPoolEntryShouldThrowNPEWhenBandsAreNull() {
        // Arrange: Create a SegmentConstantPool with null bands, which is an invalid state
        // for retrieving entries.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final long arbitraryIndex = 2009L;

        // Act: Attempt to retrieve a constant pool entry. This is expected to fail
        // by throwing a NullPointerException because the internal 'bands' field is null.
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_STRING, arbitraryIndex);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test annotation's 'expected' parameter.
    }
}