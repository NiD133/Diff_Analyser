package org.jsoup.select;

import org.jsoup.select.Selector.SelectorParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for exception handling in the {@link QueryParser}.
 */
public class QueryParserTest {

    @Test
    public void parseThrowsExceptionForUnbalancedDoubleQuoteInContains() {
        // Arrange: Define an invalid query with an unbalanced double quote and the expected error message.
        String invalidQuery = "p:contains(One \" One)";
        String expectedErrorMessage = "Did not find balanced marker at 'One \" One)'";

        // Act & Assert: Verify that parsing the invalid query throws a SelectorParseException.
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(invalidQuery)
        );

        // Assert: Confirm that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}