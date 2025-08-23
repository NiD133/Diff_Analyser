package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class focuses on verifying the behavior of the {@link NumberOutput} class,
 * specifically its handling of invalid arguments.
 */
// The original test extended a scaffolding class, which is preserved here
// to maintain compatibility with the existing test suite structure.
public class NumberOutput_ESTestTest76 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that {@link NumberOutput#outputInt(int, char[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when provided with a negative offset.
     * This is a critical boundary check to ensure the method safely handles
     * invalid offsets that would otherwise lead to illegal memory access.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntShouldThrowExceptionForNegativeOffset() {
        // Arrange: Prepare the inputs for the method under test.
        // The value to be written is not the focus of this test, but Integer.MIN_VALUE
        // serves as a valid edge case for the number itself.
        int valueToWrite = Integer.MIN_VALUE;

        // A buffer large enough to hold the string representation of any integer.
        // The length of Integer.MIN_VALUE ("-2147483648") is 11.
        char[] buffer = new char[20];

        // The key element for this test: an invalid, negative offset.
        // Using Integer.MIN_VALUE is an extreme example of a negative offset.
        int invalidNegativeOffset = Integer.MIN_VALUE;

        // Act & Assert: Call the method with the invalid offset.
        // The @Test(expected=...) annotation asserts that an
        // ArrayIndexOutOfBoundsException is thrown.
        NumberOutput.outputInt(valueToWrite, buffer, invalidNegativeOffset);
    }
}