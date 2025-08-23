package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the internal {@link Gson.FutureTypeAdapter} class.
 */
public class TypeAdapterTest {

    /**
     * Tests that using a {@link Gson.FutureTypeAdapter} before its delegate
     * has been resolved (i.e., set) results in an {@link IllegalStateException}.
     * This is a crucial check for handling cyclic dependencies in Gson's type
     * adapter resolution logic.
     */
    @Test
    public void futureTypeAdapter_readBeforeBeingResolved_throwsIllegalStateException() throws IOException {
        // Arrange: Create a FutureTypeAdapter which has not been resolved with a delegate.
        // This simulates the state during the resolution of a cyclic dependency.
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();

        // Act & Assert
        try {
            // Attempting to read should fail because the delegate adapter is not yet set.
            futureAdapter.read(null);
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException expected) {
            // Verify that the exception message is correct, confirming the reason for the failure.
            String expectedMessage = "Adapter for type with cyclic dependency has been used before dependency has been resolved";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}