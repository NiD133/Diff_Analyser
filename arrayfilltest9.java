package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 */
// The class name has been simplified for clarity and to follow common conventions.
public class ArrayFillTest extends AbstractLangTest {

    @Test
    void testFillFloatArray() {
        // Arrange: Set up the test data and expected results.
        final float[] originalArray = new float[3];
        final float fillValue = 1.0f;
        final float[] expectedArray = {1.0f, 1.0f, 1.0f};

        // Act: Call the method under test.
        final float[] resultArray = ArrayFill.fill(originalArray, fillValue);

        // Assert: Verify the results.
        // 1. The method's contract states it returns the same array instance.
        assertSame(originalArray, resultArray);

        // 2. The array's contents must match the expected filled state.
        // Using assertArrayEquals is more declarative and provides a better
        // failure message than iterating and checking each element individually.
        assertArrayEquals(expectedArray, resultArray);
    }
}