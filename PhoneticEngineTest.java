package org.apache.commons.codec.language.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the PhoneticEngine class.
 */
class PhoneticEngineTest {

    private static final int DEFAULT_MAX_PHONEMES = 10;

    /**
     * Provides test data for valid phonetic encoding scenarios.
     */
    public static Stream<Arguments> validEncodingData() {
        return Stream.of(
            Arguments.of("Renault", "rinD|rinDlt|rina|rinalt|rino|rinolt|rinu|rinult", NameType.GENERIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("Renault", "rYnDlt|rYnalt|rYnult|rinDlt|rinalt|rinolt|rinult", NameType.ASHKENAZI, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("Renault", "rinDlt", NameType.ASHKENAZI, RuleType.APPROX, true, 1),
            Arguments.of("Renault", "rinDlt", NameType.SEPHARDIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("SntJohn-Smith", "sntjonsmit", NameType.GENERIC, RuleType.EXACT, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("d'ortley", "(ortlaj|ortlej)-(dortlaj|dortlej)", NameType.GENERIC, RuleType.EXACT, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("van helsing", "(elSink|elsink|helSink|helsink|helzink|xelsink)-(banhelsink|fanhelsink|fanhelzink|vanhelsink|vanhelzink|vanjelsink)", NameType.GENERIC, RuleType.EXACT, false, DEFAULT_MAX_PHONEMES),
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", NameType.GENERIC, RuleType.APPROX, true, DEFAULT_MAX_PHONEMES),
            Arguments.of("Judenburg", "iudnbYrk|iudnbirk|iudnburk|xudnbirk|xudnburk|zudnbirk|zudnburk", NameType.GENERIC, RuleType.APPROX, true, Integer.MAX_VALUE)
        );
    }

    /**
     * Provides test data for invalid phonetic encoding scenarios.
     */
    public static Stream<Arguments> invalidEncodingData() {
        return Stream.of(
            Arguments.of("bar", "bar|bor|var|vor", NameType.ASHKENAZI, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
            Arguments.of("al", "|al", NameType.SEPHARDIC, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
            Arguments.of("da", "da|di", NameType.GENERIC, RuleType.EXACT, false, DEFAULT_MAX_PHONEMES),
            Arguments.of("'''", "", NameType.SEPHARDIC, RuleType.APPROX, false, DEFAULT_MAX_PHONEMES),
            Arguments.of("'''", "", NameType.SEPHARDIC, RuleType.APPROX, false, Integer.MAX_VALUE)
        );
    }

    /**
     * Tests the encoding of valid names using the PhoneticEngine.
     */
    @ParameterizedTest
    @MethodSource("validEncodingData")
    void testValidEncoding(final String name, final String expectedPhonetic, final NameType nameType,
                           final RuleType ruleType, final boolean concat, final int maxPhonemes) {
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);
        String actualPhonetic = engine.encode(name);

        assertEquals(expectedPhonetic, actualPhonetic, "Phonetic encoding is incorrect");

        if (concat) {
            String[] phonemes = actualPhonetic.split("\\|");
            assertTrue(phonemes.length <= maxPhonemes, "Exceeded maximum phonemes");
        } else {
            String[] words = actualPhonetic.split("-");
            for (String word : words) {
                String[] phonemes = word.split("\\|");
                assertTrue(phonemes.length <= maxPhonemes, "Exceeded maximum phonemes for word");
            }
        }
    }

    /**
     * Tests the encoding of invalid names using the PhoneticEngine.
     */
    @ParameterizedTest
    @MethodSource("invalidEncodingData")
    void testInvalidEncoding(final String input, final String expectedPhonetic, final NameType nameType,
                             final RuleType ruleType, final boolean concat, final int maxPhonemes) {
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);
        assertEquals(expectedPhonetic, engine.encode(input), "Phonetic encoding for invalid input is incorrect");
    }
}