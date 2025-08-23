package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class contains tests for the {@link NumberOutput} class.
 * This specific test focuses on ensuring proper handling of null inputs.
 */
// The original class name and inheritance are kept to match the provided context.
public class NumberOutput_ESTestTest17 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that {@code outputInt} throws a {@code NullPointerException}
     * when the provided byte array buffer is null. This is the expected behavior,
     * as the method cannot write to a non-existent buffer.
     */
    @Test(expected = NullPointerException.class)
    public void outputInt_shouldThrowNullPointerException_whenBufferIsNull() {
        // Define irrelevant but necessary parameters for the method call.
        // The value and offset do not matter since the null check on the buffer happens first.
        int anyValue = 10;
        int anyOffset = 0;

        // Act & Assert: Call the method with a null buffer, which is expected to throw.
        NumberOutput.outputInt(anyValue, null, anyOffset);
    }
}