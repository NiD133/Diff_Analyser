package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest21 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    // examples and algorithm rules from:  http://west-penwith.org.uk/misc/soundex.htm
    void testSimplifiedSoundex() {
        // treat vowels and HW as separators
        final Soundex s = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals("W452", s.encode("WILLIAMS"));
        assertEquals("B625", s.encode("BARAGWANATH"));
        assertEquals("D540", s.encode("DONNELL"));
        assertEquals("L300", s.encode("LLOYD"));
        assertEquals("W422", s.encode("WOOLCOCK"));
        // Additional local examples
        assertEquals("D320", s.encode("Dodds"));
        // w is a separator
        assertEquals("D320", s.encode("Dwdds"));
        // h is a separator
        assertEquals("D320", s.encode("Dhdds"));
    }
}
