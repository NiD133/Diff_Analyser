package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the isMetaphoneEqual method of the Metaphone class.
 */
@DisplayName("Metaphone.isMetaphoneEqual")
class MetaphoneIsEqualTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Provides a stream of names that should be phonetically equivalent to "John".
     * Match data was originally computed from http://www.lanw.com/java/phonetic/default.htm
     *
     * @return A stream of names.
     */
    private static Stream<String> phoneticMatchesForJohn() {
        return Stream.of(
            "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny", "Giana",
            "Gianna", "Gina", "Ginni", "Ginnie", "Ginny", "Jaine", "Jan", "Jana",
            "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne", "Jean",
            "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne", "Jeannie", "Jen",
            "Jena", "Jeni", "Jenn", "Jenna", "Jennee", "Jenni", "Jennie", "Jenny",
            "Jinny", "Jo Ann", "Jo-Ann", "Jo-Anne", "Joan", "Joana", "Joane",
            "Joanie", "Joann", "Joanna", "Joanne", "Joeann", "Johna", "Johnna",
            "Joni", "Jonie", "Juana", "June", "Junia", "Junie"
        );
    }

    @DisplayName("should return true for names phonetically similar to 'John'")
    @ParameterizedTest(name = "\"{0}\" should be metaphone-equal to \"John\"")
    @MethodSource("phoneticMatchesForJohn")
    void returnsTrueForSimilarSoundingNames(final String similarName) {
        final String sourceName = "John";
        
        assertTrue(
            getStringEncoder().isMetaphoneEqual(sourceName, similarName),
            () -> "Expected '" + sourceName + "' to be metaphone-equal to '" + similarName + "'"
        );
    }
}