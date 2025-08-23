package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the Soundex algorithm using examples from a genealogy website.
 * <p>
 * Source: <a href="http://www.bradandkathy.com/genealogy/overviewofsoundex.html">Overview of Soundex</a>
 * </p>
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @DisplayName("Soundex should encode names from genealogy examples correctly")
    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @CsvSource({
        "Allricht,      A462",
        "Eberhard,      E166",
        "Engebrethson,  E521",
        "Heimbach,      H512",
        "Hanselmann,    H524",
        "Hildebrand,    H431",
        "Kavanagh,      K152",
        "Lind,          L530",
        "Lukaschowsky,  L222",
        "McDonnell,     M235",
        "McGee,         M200",
        "Opnian,        O155",
        "Oppenheimer,   O155",
        "Riedemanas,    R355",
        "Zita,          Z300",
        "Zitzmeinn,     Z325"
    })
    void testGenealogyNameExamples(final String name, final String expectedSoundex) {
        assertEquals(expectedSoundex, getStringEncoder().encode(name));
    }
}