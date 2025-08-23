package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the Soundex algorithm with a variety of names that should encode to the same value.
 */
// The class name is simplified for clarity and follows standard conventions.
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @DisplayName("Test that a variety of phonetically similar names are all encoded to B650")
    @ParameterizedTest(name = "{index}: {0} -> B650")
    @MethodSource("provideNamesForB650")
    void shouldEncodeVariousSimilarNamesToB650(final String name) throws EncoderException {
        // The 'getStringEncoder()' method is inherited from AbstractStringEncoderTest
        // and provides an initialized Soundex instance.
        assertEquals("B650", getStringEncoder().encode(name));
    }

    /**
     * Provides a stream of names that are phonetically similar and should all be
     * encoded to the Soundex code "B650".
     *
     * @return A stream of test names.
     */
    private static Stream<String> provideNamesForB650() {
        return Stream.of(
            "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM",
            "BOREN", "BORN", "BOURN", "BOURNE", "BOWRON", "BRAIN", "BRAME",
            "BRANN", "BRAUN", "BREEN", "BRIEN", "BRIM", "BRIMM", "BRINN",
            "BRION", "BROOM", "BROOME", "BROWN", "BROWNE", "BRUEN", "BRUHN",
            "BRUIN", "BRUMM", "BRUN", "BRUNO", "BRYAN", "BURIAN", "BURN",
            "BURNEY", "BYRAM", "BYRNE", "BYRON", "BYRUM"
        );
    }
}