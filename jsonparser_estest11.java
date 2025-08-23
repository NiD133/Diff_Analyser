package com.google.gson;

import org.junit.Test;

/**
 * Tests the behavior of the {@link JsonParser} class, specifically how it handles
 * invalid or exceptional inputs.
 */
public class JsonParser_ESTestTest11 extends JsonParser_ESTest_scaffolding {

    /**
     * Verifies that {@link JsonParser#parseString(String)} throws a NullPointerException
     * when the input string is null. This is the expected behavior because the method
     * internally attempts to create a {@code new StringReader(null)}.
     */
    @Test(expected = NullPointerException.class)
    public void parseStringWithNullInputThrowsNullPointerException() {
        JsonParser.parseString(null);
    }
}