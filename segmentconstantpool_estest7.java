package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Improved test for the {@link SegmentConstantPool} class, focusing on understandability.
 * This test verifies the behavior of the class when its dependencies are not properly initialized.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that the {@code getValue} method throws a {@code NullPointerException}
     * when the {@code SegmentConstantPool} is constructed with null {@code CpBands}.
     *
     * This test ensures that the method correctly handles an invalid internal state
     * where the necessary data bands are missing, preventing potential silent failures
     * or unexpected behavior downstream.
     */
    @Test(expected = NullPointerException.class)
    public void getValueShouldThrowNullPointerExceptionWhenConstructedWithNullBands() {
        // Arrange: Create a SegmentConstantPool with null bands. This is the specific
        // invalid state under test, as the getValue method requires a non-null bands object
        // to retrieve data.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act: Call the getValue method with arbitrary arguments. The values themselves
        // (type and index) are not important, as the NullPointerException should occur
        // when the method first attempts to access the null bands object.
        // Using a named constant for the type improves readability over a magic number.
        segmentConstantPool.getValue(SegmentConstantPool.CP_DOUBLE, 4L);

        // Assert: The test will pass if a NullPointerException is thrown, which is
        // handled by the @Test(expected = ...) annotation. If no exception or a
        // different exception is thrown, the test will fail.
    }
}