package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that calling fill() with a null generator function returns the same array instance
     * without modifying its contents, effectively performing a no-op.
     */
    @Test
    public void testFillWithNullGeneratorReturnsSameArrayUnchanged() {
        // Arrange
        final Object[] inputArray = new Object[4];
        // Create an array representing the expected state after the operation.
        final Object[] expectedArray = new Object[4];

        // Act
        // The cast to FailableIntFunction is required to resolve ambiguity between the
        // fill(T[], T) and fill(T[], FailableIntFunction) overloads when passing null.
        final Object[] resultArray = ArrayFill.fill(inputArray, (FailableIntFunction<?, Throwable>) null);

        // Assert
        // The method should return the exact same array instance.
        assertSame("The returned array should be the same instance as the input", inputArray, resultArray);
        // The contents of the array should remain unchanged.
        assertArrayEquals("The array contents should not be modified", expectedArray, resultArray);
    }
}