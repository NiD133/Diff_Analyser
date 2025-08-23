package com.google.gson;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link JsonParser} focusing on handling of invalid inputs.
 */
public class JsonParserTest {

  /**
   * Tests that attempting to parse a string literal that is not enclosed in quotes
   * and contains multiple words results in a {@link JsonSyntaxException}.
   * According to the JSON specification, a string value must be enclosed in double quotes.
   * The parser should correctly identify this as a syntax error.
   */
  @Test
  public void parseString_withUnquotedMultiWordString_throwsJsonSyntaxException() {
    // Arrange: Define an invalid JSON string which is not quoted.
    String unquotedMultiWordString = "Test is a test..blah blah";

    // Act & Assert: Verify that parsing the invalid string throws a JsonSyntaxException.
    assertThrows(
        JsonSyntaxException.class,
        () -> JsonParser.parseString(unquotedMultiWordString)
    );
  }
}