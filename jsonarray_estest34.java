package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link JsonArray} class, focusing on type conversion behavior.
 */
public class JsonArrayTest {

    /**
     * Verifies that calling getAsInt() on a JsonArray containing a single boolean element
     * throws a NumberFormatException.
     *
     * The getAsInt() method on a JsonArray delegates to the getAsInt() method of its single
     * element. For a JsonPrimitive representing a boolean, this attempts to parse the
     * string representation of the boolean (e.g., "false") as an integer, which correctly
     * results in a NumberFormatException.
     */
    @Test
    public void getAsInt_onArrayWithSingleBoolean_throwsNumberFormatException() {
        // Arrange: Create a JsonArray containing a single boolean `false`.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(false);

        // Act & Assert: Expect a NumberFormatException when trying to get the value as an int.
        try {
            jsonArray.getAsInt();
            fail("A NumberFormatException should have been thrown.");
        } catch (NumberFormatException expected) {
            // The exception is expected. We verify the message to confirm the cause of the failure.
            assertEquals("For input string: \"false\"", expected.getMessage());
        }
    }
}