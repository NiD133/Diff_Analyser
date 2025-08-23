package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class, focusing on value creation methods.
 */
public class TypeHandlerTest {

    /**
     * Verifies that createURL() throws a ParseException when given a string
     * that is not a valid URL because it lacks a protocol.
     *
     * @throws ParseException because the input string is a malformed URL.
     */
    @Test(expected = ParseException.class)
    public void createURL_withMalformedURL_shouldThrowParseException() throws ParseException {
        // The input string "N)i" is not a valid URL as it lacks a protocol (e.g., "http://").
        TypeHandler.createURL("N)i");
    }
}