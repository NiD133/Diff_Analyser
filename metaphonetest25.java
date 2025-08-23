package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@link Metaphone} algorithm.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @DisplayName("Metaphone encoding should convert 'TIA' and 'TIO' to 'X'")
    @ParameterizedTest(name = "Input: {0}, Expected: {1}")
    @CsvSource({
        "OTIA,    OX",
        "PORTION, PRXN"
    })
    void shouldEncodeTiaAndTioAsX(final String input, final String expected) {
        assertEquals(expected, getStringEncoder().metaphone(input));
    }
}