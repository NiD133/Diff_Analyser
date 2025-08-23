package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Test suite for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that calling getInitMethodPoolEntry on an instance of
     * SegmentConstantPool created with a null CpBands dependency results
     * in a NullPointerException.
     *
     * This test ensures that the method correctly handles an invalid state where
     * its essential dependencies are not provided.
     */
    @Test(expected = NullPointerException.class)
    public void getInitMethodPoolEntryShouldThrowNPEWhenConstructedWithNullBands() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency,
        // which is an invalid state for this operation.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to get an init method pool entry. This action is expected
        // to fail immediately by throwing a NullPointerException when it tries to
        // access the null 'bands' field.
        final long arbitraryIndex = 11;
        final String arbitraryClassName = "any/class/Name";
        segmentConstantPool.getInitMethodPoolEntry(SegmentConstantPool.CP_METHOD, arbitraryIndex, arbitraryClassName);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}