package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

// Note: The original class name and inheritance are kept to provide a focused refactoring.
// In a real-world scenario, these would likely be renamed (e.g., to JsonArrayTest).
public class JsonArray_ESTestTest81 extends JsonArray_ESTest_scaffolding {

    /**
     * Verifies that calling {@code getAsInt()} on an empty {@code JsonArray}
     * throws an {@code IllegalStateException}.
     * <p>
     * The various {@code getAs...()} methods on {@code JsonArray} are convenience
     * methods that are only valid when the array contains exactly one element.
     * This test covers the case where the array is empty.
     */
    @Test
    public void getAsInt_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();
        String expectedMessage = "Array must have size 1, but has size 0";

        // Act & Assert
        try {
            emptyArray.getAsInt();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception has the expected message
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}