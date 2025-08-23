package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the StringBuilder pooling mechanism in {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void releasingFreshlyBorrowedBuilderReturnsEmptyString() {
        // Arrange: Get a StringBuilder from the internal pool.
        // It is expected to be empty/cleared when borrowed.
        StringBuilder builder = StringUtil.borrowBuilder();

        // Act: Immediately release the builder to get its string content.
        String result = StringUtil.releaseBuilder(builder);

        // Assert: The content of a new builder should be an empty string.
        assertEquals("", result);
    }
}