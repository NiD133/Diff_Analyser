package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

/**
 * Contains tests for the {@link JsonTreeReader} class, focusing on numeric parsing behavior.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling {@code nextDouble()} on a reader created from a non-numeric
     * string {@code JsonPrimitive} correctly throws a {@code NumberFormatException}.
     */
    @Test(expected = NumberFormatException.class)
    public void nextDouble_whenReadingNonNumericString_throwsNumberFormatException() throws IOException {
        // Arrange: Create a JsonTreeReader with a JsonPrimitive containing a non-numeric string.
        JsonPrimitive nonNumericStringPrimitive = new JsonPrimitive("not a valid double");
        JsonTreeReader reader = new JsonTreeReader(nonNumericStringPrimitive);

        // Act & Assert: Attempting to read the string as a double should throw an exception.
        reader.nextDouble();
    }
}