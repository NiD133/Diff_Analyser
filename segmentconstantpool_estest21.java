package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Unit tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that the {@code toIntExact} method correctly converts a negative long value
     * that is within the valid range of an integer.
     *
     * @throws Pack200Exception if the conversion fails, which is not expected in this test case.
     */
    @Test
    public void toIntExactShouldCorrectlyConvertNegativeLongWithinIntRange() throws Pack200Exception {
        // Arrange: Define a long value that can be safely cast to an int.
        final long inputValue = -119L;
        final int expectedValue = -119;

        // Act: Call the method under test.
        final int actualValue = SegmentConstantPool.toIntExact(inputValue);

        // Assert: Verify that the converted value is correct.
        assertEquals(expectedValue, actualValue);
    }
}