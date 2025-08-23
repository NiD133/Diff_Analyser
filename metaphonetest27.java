package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests a specific rule of the Metaphone algorithm: the translation of "DGE",
 * "DGI", and "DGY" to the 'J' sound.
 */
public class MetaphoneDgeDgiDgyRuleTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @DisplayName("Metaphone should translate 'DGE', 'DGI', and 'DGY' to 'J'")
    @ParameterizedTest(name = "Input: \"{1}\", Expected: \"{0}\"")
    @CsvSource({
        // The 'DGY' combination is translated to 'J'
        "TJ,   DODGY",

        // The 'DGE' combination is translated to 'J'
        "TJ,   DODGE",

        // The 'DGI' combination is translated to 'J'
        "AJMT, ADGIEMTI"
    })
    void shouldTranslateDgeDgiDgyToJ(final String expectedMetaphone, final String source) {
        assertEquals(expectedMetaphone, getStringEncoder().metaphone(source));
    }
}