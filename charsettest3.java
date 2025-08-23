package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSet#getInstance(String...)} focusing on parsing strings
 * that contain the negation character {@code '^'}.
 *
 * <p>These tests verify that the parser correctly interprets negated characters,
 * negated ranges, and combinations thereof by inspecting the internal
 * {@link CharRange} objects created by the factory method.</p>
 */
class CharSetGetInstanceWithNegationTest extends AbstractLangTest {

    /**
     * Tests that a negation character at the beginning of the string
     * only negates the character immediately following it.
     */
    @Test
    void shouldParseNegationAtStartOfSetDefinition() {
        // Arrange: A set definition where '^' negates 'a', but not 'b' or 'c'.
        final String setDefinition = "^abc";
        final Set<CharRange> expectedRanges = Set.of(
            CharRange.isNot('a'),
            CharRange.is('b'),
            CharRange.is('c')
        );

        // Act
        final CharSet charSet = CharSet.getInstance(setDefinition);
        final Set<CharRange> actualRanges = Set.of(charSet.getCharRanges());

        // Assert
        assertEquals(expectedRanges, actualRanges);
    }

    /**
     * Tests that a negation character in the middle of the string
     * only negates the character immediately following it.
     */
    @Test
    void shouldParseNegationInMiddleOfSetDefinition() {
        // Arrange: A set definition where '^' negates 'a', but not 'b' or 'c'.
        final String setDefinition = "b^ac";
        final Set<CharRange> expectedRanges = Set.of(
            CharRange.is('b'),
            CharRange.isNot('a'),
            CharRange.is('c')
        );

        // Act
        final CharSet charSet = CharSet.getInstance(setDefinition);
        final Set<CharRange> actualRanges = Set.of(charSet.getCharRanges());

        // Assert
        assertEquals(expectedRanges, actualRanges);
    }

    /**
     * Tests a more complex case with a negation character in the middle of the string.
     */
    @Test
    void shouldParseComplexDefinitionWithNegationInMiddle() {
        // Arrange: A set definition with multiple leading characters before a negated character.
        final String setDefinition = "db^ac";
        final Set<CharRange> expectedRanges = Set.of(
            CharRange.is('d'),
            CharRange.is('b'),
            CharRange.isNot('a'),
            CharRange.is('c')
        );

        // Act
        final CharSet charSet = CharSet.getInstance(setDefinition);
        final Set<CharRange> actualRanges = Set.of(charSet.getCharRanges());

        // Assert
        assertEquals(expectedRanges, actualRanges);
    }

    /**
     * Tests that multiple negation characters are handled correctly, each
     * applying to its subsequent character.
     */
    @Test
    void shouldParseMultipleNegationsInSetDefinition() {
        // Arrange: A set definition where '^' negates 'b' and a separate '^' negates 'a'.
        final String setDefinition = "^b^a";
        final Set<CharRange> expectedRanges = Set.of(
            CharRange.isNot('b'),
            CharRange.isNot('a')
        );

        // Act
        final CharSet charSet = CharSet.getInstance(setDefinition);
        final Set<CharRange> actualRanges = Set.of(charSet.getCharRanges());

        // Assert
        assertEquals(expectedRanges, actualRanges);
    }

    /**
     * Tests a combination of a normal character, a negated range, and a negated character.
     */
    @Test
    void shouldParseCombinationOfNormalCharAndNegatedRanges() {
        // Arrange: A set definition with a normal char 'b', a negated range '^a-c',
        // and a negated char '^z'.
        final String setDefinition = "b^a-c^z";
        final Set<CharRange> expectedRanges = Set.of(
            CharRange.is('b'),
            CharRange.isNotIn('a', 'c'),
            CharRange.isNot('z')
        );

        // Act
        final CharSet charSet = CharSet.getInstance(setDefinition);
        final Set<CharRange> actualRanges = Set.of(charSet.getCharRanges());

        // Assert
        assertEquals(expectedRanges, actualRanges);
    }
}