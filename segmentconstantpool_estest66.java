package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@link SegmentConstantPool#toIntExact(long)} correctly converts a long
     * value that is within the valid range of an integer.
     * @throws Pack200Exception if the conversion fails, which is not expected in this test case.
     */
    @Test
    public void toIntExactShouldReturnSameValueForInputWithinIntRange() throws Pack200Exception {
        // Arrange: Define a long value that can be safely cast to an int.
        final long input = 1L;
        final int expected = 1;

        // Act: Call the method under test.
        final int result = SegmentConstantPool.toIntExact(input);

        // Assert: Verify that the result matches the expected integer value.
        assertEquals(expected, result);
    }
}