package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TagSet} class, focusing on its public API.
 */
public class TagSetTest {

    /**
     * Verifies that attempting to register a null customizer via onNewTag
     * throws an IllegalArgumentException, as this is an invalid argument.
     */
    @Test
    public void onNewTagWithNullCustomizerThrowsIllegalArgumentException() {
        // Arrange: Create a standard HTML TagSet instance.
        TagSet tagSet = TagSet.Html();

        try {
            // Act: Attempt to register a null consumer.
            tagSet.onNewTag(null);
            fail("Expected an IllegalArgumentException to be thrown for a null customizer, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify the exception message is as expected from the validation check.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}