package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that {@link ArrayFill#fill(Object[], FailableIntFunction)}
     * modifies the array in-place and returns the same instance.
     * It also verifies that the generator function is correctly applied to each element.
     */
    @Test
    public void testFillWithGeneratorReturnsSameArrayInstanceAndFillsCorrectly() throws Throwable {
        // Arrange: Create an array and a generator function.
        // The FailableIntFunction.nop() generator returns null for every element.
        final Object[] inputArray = new Object[6];
        final FailableIntFunction<Object, Throwable> nopGenerator = FailableIntFunction.nop();

        // The expected result after filling is an array of nulls.
        final Object[] expectedArray = {null, null, null, null, null, null};

        // Act: Call the method under test.
        final Object[] resultArray = ArrayFill.fill(inputArray, nopGenerator);

        // Assert: Verify the behavior of the method.
        // 1. The method should return the same array instance for fluent chaining.
        assertSame("The returned array should be the same instance as the input array.", inputArray, resultArray);

        // 2. The array content should be modified according to the generator.
        assertArrayEquals("The array should be filled with nulls by the nop() generator.", expectedArray, resultArray);
    }
}