package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This class contains tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that {@code getConstantPoolEntry} throws a {@code NullPointerException}
     * when the {@code SegmentConstantPool} is instantiated with a null {@code CpBands}.
     * <p>
     * The method relies on the internal {@code CpBands} object to access constant pool data.
     * If this dependency is null, the method should fail immediately.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryThrowsNPEWhenBandsIsNull() throws Exception {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a constant pool entry. This is expected to throw
        // a NullPointerException because the internal 'bands' field is null.
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_FLOAT, 1152L);

        // Assert: The @Test(expected) annotation verifies that the expected exception is thrown.
    }
}