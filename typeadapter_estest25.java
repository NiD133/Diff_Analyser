package com.google.gson;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Gson.FutureTypeAdapter}, an internal helper class in Gson.
 */
public class GsonFutureTypeAdapterTest {

    /**
     * Tests that calling fromJson on a FutureTypeAdapter before its delegate
     * has been set throws an IllegalStateException.
     *
     * <p>A {@code FutureTypeAdapter} is a placeholder used by Gson internally to handle
     * cyclic type dependencies. It must be "resolved" by setting a delegate
     * adapter before it can be used. This test verifies the protective check
     * against such premature usage.
     */
    @Test
    public void fromJson_whenDelegateNotSet_throwsIllegalStateException() throws IOException {
        // Arrange: Create a FutureTypeAdapter without setting its delegate. This simulates
        // a scenario where an adapter for a cyclic dependency is used before it's resolved.
        TypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        Reader emptyJsonInput = new StringReader("");

        // Act & Assert
        try {
            futureAdapter.fromJson(emptyJsonInput);
            fail("Expected an IllegalStateException because the delegate adapter was not set.");
        } catch (IllegalStateException expected) {
            String expectedMessage = "Adapter for type with cyclic dependency has been used "
                                   + "before dependency has been resolved";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}