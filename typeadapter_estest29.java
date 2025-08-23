package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Test suite for the {@link TypeAdapter} class, focusing on specific internal behaviors.
 */
public class TypeAdapterTest {

    /**
     * Tests that calling toJson() on a FutureTypeAdapter before its delegate
     * has been set throws an IllegalStateException.
     *
     * <p>A {@code FutureTypeAdapter} is an internal Gson mechanism for handling
     * cyclic dependencies. It acts as a placeholder for a TypeAdapter that is
     * still being created. Using this placeholder before it is resolved (i.e.,
     * before its delegate is set) is an illegal state and should fail fast.
     */
    @Test
    public void toJsonOnUnresolvedFutureAdapterThrowsIllegalStateException() {
        // Arrange: Create an unresolved FutureTypeAdapter.
        // This adapter is not yet linked to a real TypeAdapter.
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        StringWriter writer = new StringWriter();

        // Act & Assert
        try {
            // Attempt to use the adapter, which should throw an exception.
            futureAdapter.toJson(writer, null);
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException e) {
            // Verify that the exception has the expected, informative message.
            String expectedMessage = "Adapter for type with cyclic dependency has been used before dependency has been resolved";
            assertEquals(expectedMessage, e.getMessage());
        } catch (IOException e) {
            // The toJson method signature allows for IOException, but it's not
            // expected in this specific scenario.
            fail("Expected an IllegalStateException, but caught an unexpected IOException: " + e.getMessage());
        }
    }
}