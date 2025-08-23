package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the isMetaphoneEqual method of the Metaphone class.
 */
public class MetaphoneIsMetaphoneEqualTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Provides pairs of strings that are expected to have the same Metaphone encoding.
     *
     * @return A stream of arguments, where each argument is a pair of strings.
     */
    private static Stream<Arguments> metaphoneEqualPairs() {
        // Matches computed from http://www.lanw.com/java/phonetic/default.htm
        return Stream.of(
            Arguments.of("Lawrence", "Lorenza"),
            Arguments.of("Gary", "Cahra")
        );
    }

    /**
     * Tests that isMetaphoneEqual returns true for words that should be phonetically equivalent.
     * The isMetaphoneEqual method is expected to be symmetric, so checking both
     * isMetaphoneEqual(str1, str2) and isMetaphoneEqual(str2, str1) is redundant.
     *
     * @param string1 the first string to compare
     * @param string2 the second string to compare
     */
    @ParameterizedTest
    @MethodSource("metaphoneEqualPairs")
    void shouldReturnTrueForPhoneticallyEquivalentWords(final String string1, final String string2) {
        assertTrue(
            getStringEncoder().isMetaphoneEqual(string1, string2),
            () -> String.format("Expected '%s' and '%s' to have the same Metaphone code", string1, string2)
        );
    }
}