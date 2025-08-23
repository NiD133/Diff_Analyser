package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Test suite for the {@link TypeAdapter} class, focusing on specific internal behaviors.
 */
public class TypeAdapterTest {

    /**
     * Verifies that attempting to use a {@code Gson.FutureTypeAdapter} before it has been
     * configured with a delegate adapter throws an {@link IllegalStateException}.
     *
     * This is a critical safeguard within Gson's internal machinery to correctly handle
     * and report errors related to cyclic type dependencies during object graph construction.
     */
    @Test
    public void fromJson_onUnresolvedFutureTypeAdapter_throwsIllegalStateException() {
        // Arrange: Create a FutureTypeAdapter without setting its delegate. This simulates
        // the state where a cyclic dependency has been detected but not yet resolved.
        Gson.FutureTypeAdapter<String> futureAdapter = new Gson.FutureTypeAdapter<>();

        // Act & Assert: Attempting to deserialize JSON with this unresolved adapter
        // should immediately fail with a clear error message.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> futureAdapter.fromJson("\"some json\"") // The actual JSON content is irrelevant.
        );

        // Assert: Verify the exception message is informative.
        assertEquals(
            "Adapter for type with cyclic dependency has been used before dependency has been resolved",
            exception.getMessage()
        );
    }
}