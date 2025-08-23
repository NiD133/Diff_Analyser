package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// The test class name has been simplified to follow standard conventions.
public class JsonParserTest {

    /**
     * Tests that `parseReader(JsonReader)` throws a `JsonSyntaxException`
     * when the provided reader is in STRICT mode and encounters malformed JSON.
     * This verifies that the parser respects the strictness setting of the reader.
     */
    @Test
    public void parseReader_withStrictReaderAndMalformedJson_throwsJsonSyntaxException() {
        // Arrange: Create a JsonReader in STRICT mode with an unquoted string,
        // which is an invalid JSON literal.
        String malformedJson = "unquoted_string";
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
        jsonReader.setStrictness(Strictness.STRICT);

        // Act & Assert
        try {
            JsonParser.parseReader(jsonReader);
            fail("Expected JsonSyntaxException for malformed JSON in strict mode, but no exception was thrown.");
        } catch (JsonSyntaxException e) {
            // Verify that the exception is caused by a MalformedJsonException,
            // as expected from the underlying strict reader.
            Throwable cause = e.getCause();
            assertNotNull("JsonSyntaxException should have a cause.", cause);
            assertTrue("The cause should be a MalformedJsonException.", cause instanceof MalformedJsonException);

            // Verify the error message to ensure it provides helpful feedback to the user.
            String expectedMessage = "Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON"
                + " at line 1 column 1 path $";
            assertEquals(expectedMessage, cause.getMessage());
        }
    }
}