package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original test class name and inheritance are kept to show a direct improvement
// of the provided code. In a real-world scenario, the class might be renamed
// to ToNumberPolicyTest.
public class ToNumberPolicy_ESTestTest4 extends ToNumberPolicy_ESTest_scaffolding {

    /**
     * Tests that the LONG_OR_DOUBLE policy throws a JsonParseException when
     * attempting to read an unparseable string in lenient mode.
     *
     * In lenient mode, the JsonReader can read an unquoted string. The
     * LONG_OR_DOUBLE policy then attempts to parse this string as a Long or Double.
     * Since the string "not-a-number" is not a valid number, this parsing fails,
     * and a JsonParseException is expected.
     */
    @Test(timeout = 4000)
    public void longOrDoublePolicy_withUnparseableStringInLenientMode_throwsException() {
        // Arrange
        String unparseableString = "not-a-number";
        StringReader stringReader = new StringReader(unparseableString);
        JsonReader jsonReader = new JsonReader(stringReader);
        // Lenient mode is required to allow the reader to consume an unquoted string value.
        jsonReader.setLenient(true);

        ToNumberStrategy policy = ToNumberPolicy.LONG_OR_DOUBLE;

        // Act & Assert
        try {
            policy.readNumber(jsonReader);
            fail("Expected a JsonParseException to be thrown for an unparseable number string.");
        } catch (JsonParseException e) {
            // Verify that the exception message is informative and correct.
            String expectedMessage = "Cannot parse " + unparseableString + "; at path $";
            assertEquals(expectedMessage, e.getMessage());
        } catch (IOException e) {
            // The readNumber method is declared to throw IOException, but it's not expected in this test case.
            fail("Test should not have thrown an IOException, but it did: " + e.getMessage());
        }
    }
}