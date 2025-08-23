package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Entities} class.
 */
public class EntitiesTest {

    @Test
    void unescapeDoesNotAlterStringWithNonEntityAmpersands() {
        // Arrange: A string containing ampersands that are not part of any valid HTML entity.
        // A URL with query parameters is a common real-world example.
        String urlWithNonEntityAmpersands = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";

        // Act: Attempt to unescape the string.
        String result = Entities.unescape(urlWithNonEntityAmpersands);

        // Assert: The string should remain unchanged. This verifies that substrings like
        // "&num_rooms" or "&int" are not incorrectly interpreted as HTML entities.
        assertEquals(urlWithNonEntityAmpersands, result);
    }
}