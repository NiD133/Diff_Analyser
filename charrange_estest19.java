package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the {@link CharRange} class, focusing on the equals() method.
 */
public class CharRangeTest {

    @Test
    public void testEqualsReturnsFalseForRangesWithDifferentEndPoints() {
        // Arrange
        // Create a range from '%' to '+'. Note: The isIn() factory method automatically
        // sorts the characters, so '%' (ASCII 37) becomes the start and '+' (ASCII 43) the end.
        CharRange multiCharRange = CharRange.isIn('+', '%');

        // Create a range representing only the single character '%'.
        CharRange singleCharRange = CharRange.is('%');

        // Sanity checks to confirm the state of our test objects.
        // This makes it clear why they should not be equal.
        assertEquals("Start of multi-char range should be '%'", '%', multiCharRange.getStart());
        assertEquals("End of multi-char range should be '+'", '+', multiCharRange.getEnd());
        assertEquals("Start of single-char range should be '%'", '%', singleCharRange.getStart());
        assertEquals("End of single-char range should be '%'", '%', singleCharRange.getEnd());

        // Act & Assert
        // The core purpose of the test: verify that two ranges with different
        // definitions are not considered equal.
        assertFalse("A multi-character range should not equal a single-character range", multiCharRange.equals(singleCharRange));
        assertFalse("The equals check should be symmetric", singleCharRange.equals(multiCharRange));

        // A more modern and direct assertion for inequality.
        assertNotEquals(multiCharRange, singleCharRange);
    }
}