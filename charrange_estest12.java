package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Tests for {@link CharRange#contains(CharRange)}.
 */
public class CharRangeTest {

    @Test(expected = NullPointerException.class)
    public void containsRangeShouldThrowNullPointerExceptionForNullInput() {
        // Arrange
        final CharRange range = CharRange.is('a');

        // Act
        range.contains((CharRange) null);

        // Assert: The @Test(expected) annotation handles the assertion.
    }
}