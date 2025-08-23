package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Contains tests for the {@link SegmentConstantPool} class, focusing on its behavior
 * under specific initialization conditions.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that calling {@link SegmentConstantPool#getValue(int, long)} throws a
     * {@link NullPointerException} if the pool was constructed with a null {@code CpBands} instance.
     * This test ensures that the class correctly handles the absence of its essential dependencies.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNullPointerExceptionWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with its CpBands dependency set to null.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Attempt to retrieve a value. This action is expected to fail because the internal
        // 'bands' field is null, leading to a NullPointerException when accessed.
        // We use CP_INT and a sample index to trigger the code path that uses the 'bands' field.
        segmentConstantPool.getValue(SegmentConstantPool.CP_INT, 10L);

        // Assert: The test is successful if a NullPointerException is thrown, as specified by the
        // @Test(expected=...) annotation. No further assertions are needed.
    }
}