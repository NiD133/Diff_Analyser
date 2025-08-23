package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Entities}.
 */
public class EntitiesTest {

    @Test
    void shouldUnescapeNumericHtmlEntityForEmoji() {
        // Arrange
        String htmlWithNumericEmojiEntity = "&#128175;"; // The "hundred points" emoji: 💯
        String expectedEmoji = "💯";

        // Act
        // The original test used Parser.unescapeEntities. Calling Entities.unescape directly
        // makes the test more focused on the actual class under test.
        String actualEmoji = Entities.unescape(htmlWithNumericEmojiEntity);

        // Assert
        assertEquals(expectedEmoji, actualEmoji);
    }
}