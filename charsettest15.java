package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the static constants of {@link CharSet}.
 *
 * <p>This test class verifies the internal structure (the character ranges)
 * of each predefined {@code CharSet} constant.</p>
 */
// The original class name "CharSetTestTest15" is unconventional.
// A more descriptive name like "CharSetStaticsTest" is recommended.
public class CharSetStaticsTest extends AbstractLangTest {

    @Test
    @DisplayName("CharSet.EMPTY should contain no character ranges")
    void testEmptySetContainsNoRanges() {
        // Act
        final CharRange[] ranges = CharSet.EMPTY.getCharRanges();

        // Assert
        assertEquals(0, ranges.length, "CharSet.EMPTY should be empty.");
    }

    @Test
    @DisplayName("CharSet.ASCII_ALPHA should contain ranges for 'a-z' and 'A-Z'")
    void testAsciiAlphaSetContainsUpperAndLowerCaseRanges() {
        // Arrange
        final CharRange expectedLower = CharRange.isIn('a', 'z');
        final CharRange expectedUpper = CharRange.isIn('A', 'Z');

        // Act
        final CharRange[] ranges = CharSet.ASCII_ALPHA.getCharRanges();

        // Assert
        assertEquals(2, ranges.length, "CharSet.ASCII_ALPHA should contain two ranges.");
        assertTrue(ArrayUtils.contains(ranges, expectedLower), "Should contain the range 'a-z'.");
        assertTrue(ArrayUtils.contains(ranges, expectedUpper), "Should contain the range 'A-Z'.");
    }

    @Test
    @DisplayName("CharSet.ASCII_ALPHA_LOWER should contain the range for 'a-z'")
    void testAsciiAlphaLowerSetContainsLowerCaseRange() {
        // Arrange
        final CharRange expectedRange = CharRange.isIn('a', 'z');

        // Act
        final CharRange[] ranges = CharSet.ASCII_ALPHA_LOWER.getCharRanges();

        // Assert
        assertEquals(1, ranges.length, "CharSet.ASCII_ALPHA_LOWER should contain one range.");
        assertTrue(ArrayUtils.contains(ranges, expectedRange), "Should contain the range 'a-z'.");
    }

    @Test
    @DisplayName("CharSet.ASCII_ALPHA_UPPER should contain the range for 'A-Z'")
    void testAsciiAlphaUpperSetContainsUpperCaseRange() {
        // Arrange
        final CharRange expectedRange = CharRange.isIn('A', 'Z');

        // Act
        final CharRange[] ranges = CharSet.ASCII_ALPHA_UPPER.getCharRanges();

        // Assert
        assertEquals(1, ranges.length, "CharSet.ASCII_ALPHA_UPPER should contain one range.");
        assertTrue(ArrayUtils.contains(ranges, expectedRange), "Should contain the range 'A-Z'.");
    }

    @Test
    @DisplayName("CharSet.ASCII_NUMERIC should contain the range for '0-9'")
    void testAsciiNumericSetContainsNumericRange() {
        // Arrange
        final CharRange expectedRange = CharRange.isIn('0', '9');

        // Act
        final CharRange[] ranges = CharSet.ASCII_NUMERIC.getCharRanges();

        // Assert
        assertEquals(1, ranges.length, "CharSet.ASCII_NUMERIC should contain one range.");
        assertTrue(ArrayUtils.contains(ranges, expectedRange), "Should contain the range '0-9'.");
    }
}