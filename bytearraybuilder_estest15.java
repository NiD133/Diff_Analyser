package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that a ByteArrayBuilder created with the `fromInitial` factory method
     * correctly reports its size based on the provided initial length. This is true
     * even when the underlying initial byte array is smaller than the specified length.
     */
    @Test
    public void fromInitial_shouldSetSizeToInitialLength() {
        // Arrange
        // The `fromInitial` method allows creating a builder that "wraps" an existing
        // buffer, treating the first `initialLength` bytes as already written content.
        final int expectedInitialSize = 6;

        // Act
        // Create a builder using an empty byte array but specifying an initial length.
        // This simulates a scenario where the builder is initialized to a state
        // where it believes it already contains `expectedInitialSize` bytes.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, expectedInitialSize);

        // Assert
        // The size() method should reflect the initial length provided, not the
        // actual length of the underlying (and in this case, empty) byte array.
        assertEquals(expectedInitialSize, builder.size());
    }
}