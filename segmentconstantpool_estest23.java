package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for the static utility methods in {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@code toIndex()} correctly converts a small, positive long value
     * into its integer equivalent. This covers the basic "happy path" scenario.
     * @throws Pack200Exception if the conversion fails, which is not expected for this input.
     */
    @Test
    public void toIndexShouldConvertPositiveLongToInt() throws Pack200Exception {
        // Arrange: Define the input and the expected outcome.
        final long inputIndex = 1L;
        final int expectedIndex = 1;

        // Act: Call the method under test.
        final int actualIndex = SegmentConstantPool.toIndex(inputIndex);

        // Assert: Verify that the actual outcome matches the expected outcome.
        assertEquals("The long value 1L should be converted to the integer 1.", expectedIndex, actualIndex);
    }
}