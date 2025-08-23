package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that calling getConstantPoolEntry() on a SegmentConstantPool initialized
     * with null CpBands throws a NullPointerException. This scenario simulates an
     * invalid internal state.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryShouldThrowNullPointerExceptionWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with null CpBands, which is an invalid state.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a constant pool entry. This is expected to fail
        // because the internal 'bands' field is null, and the method will try to
        // access it.
        final long arbitraryIndex = 11L;
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_METHOD, arbitraryIndex);

        // Assert: The @Test(expected) annotation handles the assertion, ensuring
        // that a NullPointerException is thrown as expected.
    }
}