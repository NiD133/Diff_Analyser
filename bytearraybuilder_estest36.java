package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.fail; // Maintained for comparison, but not used in the final version.

/**
 * Tests for the {@link ByteArrayBuilder} class, focusing on edge cases.
 */
// The original test class extended a scaffolding class. We retain this to ensure
// any required setup from the parent class is preserved.
public class ByteArrayBuilder_ESTestTest36 extends ByteArrayBuilder_ESTest_scaffolding {

    /**
     * This test verifies that appendTwoBytes() throws an ArrayIndexOutOfBoundsException
     * if the builder is in an inconsistent state with a negative internal pointer.
     * This can happen if the builder is constructed improperly or its internal state is corrupted.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendTwoBytes_shouldThrowException_whenInternalPointerIsNegative() {
        // Arrange: Create a ByteArrayBuilder in an invalid state.
        // The fromInitial() factory method allows setting an initial buffer and length.
        // We use it here to simulate a corrupted state where the current position
        // (_currBlockPtr) is a negative number.
        int negativePosition = -733;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, negativePosition);

        // Act: Attempt to append two bytes. This should try to write to a negative
        // array index, triggering the expected exception.
        // The integer value to append (130739) is arbitrary.
        builder.appendTwoBytes(130739);

        // Assert: The test will pass only if an ArrayIndexOutOfBoundsException is thrown.
        // This is handled declaratively by the `expected` attribute of the @Test annotation.
        // If no exception is thrown, JUnit will automatically fail the test.
    }
}