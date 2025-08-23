package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() returns null when a null array is provided
     * along with a generator function.
     */
    @Test
    public void testFillWithGeneratorShouldReturnNullForNullArray() {
        // Arrange
        // The generator is not used when the input array is null, but one is needed
        // to satisfy the method signature. FailableIntFunction.nop() is a simple no-op implementation.
        final FailableIntFunction<Object, ?> generator = FailableIntFunction.nop();
        final String[] inputArray = null;

        // Act
        final String[] resultArray = ArrayFill.fill(inputArray, generator);

        // Assert
        assertNull("The method should return null for a null input array.", resultArray);
    }
}