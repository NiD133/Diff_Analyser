package org.apache.commons.lang3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link CharRange}.
 * This class provides a more understandable and maintainable version of the original generated test.
 */
public class CharRangeTest {

    /**
     * Tests that the {@code CharRange.isIn(start, end)} factory method creates a
     * correct, non-negated range and that the resulting range is correctly iterable.
     */
    @Test
    public void testIsInCreatesCorrectRangeAndIsIterable() {
        // Arrange: Define the boundaries for the character range.
        final char start = '8';
        final char end = 'A';

        // Act: Create the CharRange instance using the factory method.
        final CharRange range = CharRange.isIn(start, end);

        // Assert: Verify the state of the created range object.
        assertFalse("The range should not be negated", range.isNegated());
        assertEquals("The start of the range should be correct", start, range.getStart());
        assertEquals("The end of the range should be correct", end, range.getEnd());

        // Assert: Verify the iteration behavior of the range.
        // 1. Create the expected list of characters in the range.
        final List<Character> expectedChars = IntStream.rangeClosed(start, end)
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        // 2. Collect the actual characters by iterating over the range.
        final List<Character> actualChars = new ArrayList<>();
        range.forEach(actualChars::add);

        // 3. Compare the actual list with the expected list.
        assertEquals("The iterated characters should match the expected sequence", expectedChars, actualChars);
    }
}