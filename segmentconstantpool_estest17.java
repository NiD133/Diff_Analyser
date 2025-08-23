package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test verifies the behavior of the {@link SegmentConstantPool} class when its
 * dependencies are not properly initialized.
 */
public class SegmentConstantPoolImprovedTest {

    /**
     * Tests that calling {@code getConstantPoolEntry} on a {@code SegmentConstantPool}
     * instance created with null {@code CpBands} throws a {@code NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void getConstantPoolEntryWithNullBandsShouldThrowNullPointerException() throws Exception {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency,
        // which is the precondition for the expected exception.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a constant pool entry. This action is expected
        // to trigger the exception because the internal 'bands' field is null.
        // The constant CP_LONG is used as a representative type.
        segmentConstantPool.getConstantPoolEntry(SegmentConstantPool.CP_LONG, 0L);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as declared by the @Test(expected=...) annotation. No further assertions are needed.
    }
}