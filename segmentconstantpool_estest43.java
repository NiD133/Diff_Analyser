package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.junit.Test;

import static org.junit.Assert.assertNull;

// The test class name and inheritance are kept from the original to show the
// change in context, though in a real-world scenario, these would also be improved.
public class SegmentConstantPool_ESTestTest43 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that the getValue() method returns null when provided with a negative index.
     * A negative index is considered invalid and should be handled gracefully
     * without throwing an exception.
     * @throws Pack200Exception if an unexpected error occurs during constant pool processing.
     */
    @Test(timeout = 4000)
    public void getValueShouldReturnNullForNegativeIndex() throws Pack200Exception {
        // Arrange
        // The CpBands dependency can be null for this test, as it's not accessed
        // when the method short-circuits due to an invalid index.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int constantPoolType = SegmentConstantPool.CP_IMETHOD;
        final long invalidIndex = -1L;

        // Act
        ClassFileEntry result = segmentConstantPool.getValue(constantPoolType, invalidIndex);

        // Assert
        assertNull("getValue() should return null when the index is negative.", result);
    }
}