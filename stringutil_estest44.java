package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link StringUtil.StringJoiner} inner class.
 */
public class StringUtilStringJoinerTest {

    /**
     * Verifies that the {@link StringUtil.StringJoiner#add(Object)} method
     * consistently returns the same instance, which allows for method chaining (a fluent API).
     */
    @Test
    public void addMethodReturnsSameInstanceForChaining() {
        // Arrange: Create a StringJoiner instance.
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner(" ");

        // Act: Call the add() method multiple times and capture the returned instances.
        StringUtil.StringJoiner resultFromFirstCall = joiner.add("one");
        StringUtil.StringJoiner resultFromSecondCall = joiner.add("two");

        // Assert: Verify that each call returned the original joiner instance.
        assertSame(
            "The first call to add() should return the original instance to enable chaining.",
            joiner,
            resultFromFirstCall
        );
        assertSame(
            "Subsequent calls to add() should also return the original instance.",
            joiner,
            resultFromSecondCall
        );
    }
}