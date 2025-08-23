package com.google.gson;

import org.junit.Test;

/**
 * This test suite focuses on the behavior of the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that calling {@code getAsFloat()} on a JsonArray containing a single, non-numeric
     * character element throws a {@link NumberFormatException}.
     *
     * <p>The {@code getAsFloat()} method on a {@code JsonArray} is a convenience method that
     * should only succeed if the array contains exactly one element that can be converted to a float.
     * In this case, the character '|' cannot be parsed as a float, so an exception is expected.
     */
    @Test(expected = NumberFormatException.class)
    public void getAsFloat_whenArrayContainsSingleNonNumericCharacter_throwsNumberFormatException() {
        // Arrange: Create a JsonArray and add a single character that is not a number.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add('|');

        // Act: Attempt to get the contents of the array as a float.
        // This line is expected to throw the NumberFormatException.
        jsonArray.getAsFloat();

        // Assert: The test passes if the expected exception is thrown, as declared
        // in the @Test annotation. No further assertions are needed.
    }
}