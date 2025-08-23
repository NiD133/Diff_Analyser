package com.google.gson;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the inner class {@link Gson.FutureTypeAdapter}.
 */
public class GsonFutureTypeAdapterTest {

    /**
     * Verifies that calling {@code write} on a {@link Gson.FutureTypeAdapter} before its
     * delegate has been set results in an {@link IllegalStateException}. This is a key
     * mechanism for detecting and handling cyclic dependencies during type adapter creation.
     */
    @Test
    public void writeOnUnresolvedAdapterShouldThrowIllegalStateException() {
        // Arrange: Create a FutureTypeAdapter that has not been resolved (i.e., no delegate is set).
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();

        try {
            // Act: Attempt to use the adapter for writing. Since the delegate is not set,
            // this is expected to fail.
            futureAdapter.write(null, null);
            fail("Expected an IllegalStateException to be thrown, but the method completed successfully.");
        } catch (IllegalStateException expected) {
            // Assert: Verify that the exception has the expected message.
            String expectedMessage = "Adapter for type with cyclic dependency has been used "
                                   + "before dependency has been resolved";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}