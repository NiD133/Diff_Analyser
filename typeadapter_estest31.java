package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of Gson's internal FutureTypeAdapter.
 * Note: The original class name "TypeAdapter_ESTestTest31" was auto-generated.
 */
public class TypeAdapter_ESTestTest31 extends TypeAdapter_ESTest_scaffolding {

    /**
     * Tests that using a FutureTypeAdapter for deserialization before its delegate
     * adapter has been set results in an IllegalStateException.
     *
     * This is a crucial safeguard for handling cyclic type dependencies, ensuring
     * the adapter isn't used in an invalid, unresolved state.
     */
    @Test
    public void fromJsonTree_onUnresolvedFutureAdapter_throwsIllegalStateException() {
        // Arrange: Create a FutureTypeAdapter which has not been resolved with a delegate.
        // This simulates the state during the setup of a type with a cyclic dependency.
        Gson.FutureTypeAdapter<Object> futureAdapter = new Gson.FutureTypeAdapter<>();
        
        // Wrap it with nullSafe(), which is a common use case. The behavior should be the same.
        TypeAdapter<Object> nullSafeFutureAdapter = futureAdapter.nullSafe();
        
        // Create an arbitrary JSON element to use as input for deserialization.
        JsonPrimitive jsonInput = new JsonPrimitive('B');

        // Act & Assert: Attempting to use the unresolved adapter should fail.
        try {
            nullSafeFutureAdapter.fromJsonTree(jsonInput);
            fail("Expected an IllegalStateException because the FutureTypeAdapter's delegate has not been set.");
        } catch (IllegalStateException expected) {
            // Verify that the exception message clearly explains the problem.
            String expectedMessage = "Adapter for type with cyclic dependency has been used before dependency has been resolved";
            assertEquals(expectedMessage, expected.getMessage());
        }
    }
}