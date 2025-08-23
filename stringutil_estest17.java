package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the StringBuilder pooling mechanism in {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Verifies that {@link StringUtil#releaseBuilder(StringBuilder)} clears the
     * contents of a StringBuilder after its use. This is crucial for ensuring
     * that reused builders from the pool do not contain stale data from previous operations.
     */
    @Test
    public void releaseBuilderShouldClearTheBuilder() {
        // Arrange: Borrow a builder from the pool and add some content.
        StringBuilder builder = StringUtil.borrowBuilder();
        builder.append("some test content");

        // Act: Release the builder back to the pool.
        StringUtil.releaseBuilder(builder);

        // Assert: The builder's content should be cleared after being released.
        assertEquals("The StringBuilder should be empty after being released.", 0, builder.length());
    }
}