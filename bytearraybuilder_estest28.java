package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on specific edge cases.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling getClearAndRelease() on a ByteArrayBuilder initialized
     * with a null buffer throws a NullPointerException.
     *
     * This scenario tests the handling of an invalid initial state where the underlying
     * byte array is null, which should lead to an exception upon access.
     */
    @Test(expected = NullPointerException.class)
    public void getClearAndReleaseShouldThrowNpeWhenInitializedWithNullBuffer() {
        // Arrange: Create a ByteArrayBuilder with a null initial buffer.
        // The length parameter is not relevant to this test, as the null buffer itself
        // is the cause of the expected exception.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, 128);

        // Act: Attempt to retrieve the contents and release the buffer.
        // This is expected to fail because the internal buffer is null.
        builder.getClearAndRelease();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}