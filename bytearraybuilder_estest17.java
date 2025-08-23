package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that creating a ByteArrayBuilder via the {@code fromInitial} factory method
     * with an empty byte array correctly initializes the builder with a size of zero.
     */
    @Test
    public void fromInitialWithEmptyArrayShouldHaveZeroSize() {
        // Arrange: Use the fromInitial factory with the public static empty array
        // and an initial length of 0.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, 0);

        // Assert: The size of the newly created builder should be 0.
        assertEquals(0, builder.size());
    }
}