package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@link SegmentConstantPool#getConstantPoolEntry(int, long)} returns null
     * when provided with negative, and therefore invalid, indices. This ensures the method
     * handles invalid inputs gracefully without throwing an exception.
     */
    @Test
    public void getConstantPoolEntryShouldReturnNullForNegativeIndices() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool. The CpBands dependency can be null
        // because it is not accessed when the input indices are invalid.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int invalidPoolType = -1;
        final long invalidPoolIndex = -1;

        // Act: Attempt to retrieve a constant pool entry with invalid indices.
        final ConstantPoolEntry result = segmentConstantPool.getConstantPoolEntry(invalidPoolType, invalidPoolIndex);

        // Assert: The method should return null for invalid negative inputs.
        assertNull("getConstantPoolEntry() should return null for a negative type and index.", result);
    }
}