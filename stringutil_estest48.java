package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the StringUtil.StringJoiner inner class.
 */
public class StringUtilStringJoinerTest {

    @Test
    public void completeOnEmptyJoinerShouldReturnEmptyString() {
        // Arrange: Create a StringJoiner with a null separator, to which no items are added.
        // This also tests that a null separator is handled gracefully.
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(null);

        // Act: Immediately complete the joining process.
        String result = joiner.complete();

        // Assert: The result for an empty joiner should be an empty string.
        assertEquals("", result);
    }
}