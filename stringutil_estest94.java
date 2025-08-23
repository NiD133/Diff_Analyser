package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the inner class {@link StringUtil.StringJoiner}.
 */
public class StringUtilStringJoinerTest {

    /**
     * Verifies that the append() method returns the same StringJoiner instance
     * to allow for fluent method chaining.
     */
    @Test
    public void appendReturnsSameInstanceForChaining() {
        // Arrange: Create a StringJoiner with a simple separator.
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(", ");

        // Act: Call the append method.
        StringUtil.StringJoiner result = joiner.append("some text");

        // Assert: The returned object should be the exact same instance as the original.
        assertSame("The append() method must return 'this' to support a fluent API.", joiner, result);
    }
}