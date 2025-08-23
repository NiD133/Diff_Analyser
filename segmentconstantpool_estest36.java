package org.apache.commons.compress.harmony.unpack200;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that {@code getValue()} throws a {@code NullPointerException}
     * when the {@code SegmentConstantPool} is constructed with null {@code CpBands}.
     * This is expected behavior as the method will attempt to dereference the null bands object.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNullPointerExceptionWhenBandsAreNull() throws Pack200Exception {
        // Arrange: Create a SegmentConstantPool with a null CpBands dependency.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Call the method under test. This is expected to throw the exception.
        // The arguments (SIGNATURE, 2L) are representative valid inputs to ensure
        // the failure is caused by the null state, not by the arguments themselves.
        segmentConstantPool.getValue(SegmentConstantPool.SIGNATURE, 2L);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}