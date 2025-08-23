package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link RefinedSoundex#difference(String, String)} method.
 */
@DisplayName("RefinedSoundex difference calculation")
class RefinedSoundexTest {

    private RefinedSoundex encoder;

    @BeforeEach
    void setUp() {
        this.encoder = new RefinedSoundex();
    }

    @Test
    @DisplayName("should return 0 for null, empty, or blank string inputs")
    void testDifferenceOnEdgeCases() throws EncoderException {
        assertAll(
            () -> assertEquals(0, encoder.difference(null, null), "Null inputs should result in 0 difference"),
            () -> assertEquals(0, encoder.difference("", ""), "Empty inputs should result in 0 difference"),
            () -> assertEquals(0, encoder.difference(" ", " "), "Blank inputs should result in 0 difference")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringPairsForDifferenceTest")
    @DisplayName("should correctly calculate the difference between two strings")
    void testDifference(final int expectedDifference, final String s1, final String s2, final String description) throws EncoderException {
        assertEquals(expectedDifference, encoder.difference(s1, s2), description);
    }

    private static Stream<Arguments> provideStringPairsForDifferenceTest() {
        return Stream.of(
            // General test cases
            Arguments.of(6, "Smith", "Smythe", "Similar names should have a high difference score"),
            Arguments.of(3, "Ann", "Andrew", "Partially similar names"),
            Arguments.of(1, "Margaret", "Andrew", "Dissimilar names should have a low score"),
            Arguments.of(1, "Janet", "Margaret", "Dissimilar names should have a low score"),

            // Examples from MS T-SQL DIFFERENCE documentation
            // Source: https://msdn.microsoft.com/library/en-us/tsqlref/ts_de-dz_8co5.asp
            Arguments.of(5, "Green", "Greene", "MSDN Example: Green vs Greene"),
            Arguments.of(1, "Blotchet-Halls", "Greene", "MSDN Example: Blotchet-Halls vs Greene"),

            // Examples from other MS T-SQL documentation
            // Source: https://msdn.microsoft.com/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
            Arguments.of(6, "Smith", "Smythe", "MSDN Example: Smith vs Smythe"),
            Arguments.of(8, "Smithers", "Smythers", "MSDN Example: Smithers vs Smythers"),
            Arguments.of(5, "Anothers", "Brothers", "MSDN Example: Anothers vs Brothers")
        );
    }
}