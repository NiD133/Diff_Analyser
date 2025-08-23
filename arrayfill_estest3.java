package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() with a generator function returns the same
     * instance when given an empty array, as no filling is necessary.
     */
    @Test
    public void testFillWithGeneratorOnEmptyArrayReturnsSameInstance() {
        // Arrange: Create an empty array and a no-op generator function.
        final Object[] emptyArray = new Object[0];
        final FailableIntFunction<Object, ?> generator = FailableIntFunction.nop();

        // Act: Call the fill method with the empty array.
        final Object[] resultArray = ArrayFill.fill(emptyArray, generator);

        // Assert: The returned array should be the exact same instance as the input array.
        assertSame("For an empty array, the returned instance should be the same", emptyArray, resultArray);
    }
}