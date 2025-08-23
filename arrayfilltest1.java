package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 * This version focuses on the boolean[] overload.
 */
public class ArrayFillTestTest1 extends AbstractLangTest {

    @Test
    void testFill_withBooleanArray_fillsArrayAndReturnsSameInstance() {
        // Arrange: Set up the test data and expectations.
        final boolean[] inputArray = new boolean[3]; // Initial state: {false, false, false}
        final boolean fillValue = true;
        final boolean[] expectedArray = {true, true, true};

        // Act: Call the method under test.
        final boolean[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert: Verify the results.
        // 1. The method should fill the array with the specified value.
        assertArrayEquals(expectedArray, resultArray);

        // 2. The method should return the same array instance to allow for method chaining.
        assertSame(inputArray, resultArray);
    }
}