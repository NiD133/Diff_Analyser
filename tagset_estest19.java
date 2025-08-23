package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link TagSet} class.
 */
public class TagSetTest {

    @Test
    public void addShouldThrowNullPointerExceptionWhenTagIsNull() {
        // Arrange
        TagSet tagSet = TagSet.Html();

        // Act & Assert
        // The add(Tag) method should reject null input to ensure data integrity.
        assertThrows(NullPointerException.class, () -> {
            tagSet.add(null);
        });
    }
}