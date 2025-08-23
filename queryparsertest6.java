package org.jsoup.select;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for error handling scenarios in the {@link QueryParser}.
 */
@DisplayName("QueryParser Error Handling")
class QueryParserErrorTest {

    @Test
    @DisplayName("should throw a parse exception for an unclosed attribute selector")
    void unclosedAttributeThrowsParseException() {
        // Arrange: Define the invalid query and the expected error message.
        String queryWithUnclosedAttribute = "section > a[href=\"]";
        String expectedMessage = "Did not find balanced marker at 'href=\"]'";

        // Act: Attempt to parse the invalid query and capture the thrown exception.
        SelectorParseException thrown = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(queryWithUnclosedAttribute)
        );

        // Assert: Verify that the exception's message is what we expect.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}