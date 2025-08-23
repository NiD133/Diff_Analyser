package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest11 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    // examples and algorithm rules from:  http://www.genealogy.com/articles/research/00000060.html
    void testGenealogy() {
        // treat vowels and HW as silent
        final Soundex s = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("H251", s.encode("Heggenburger"));
        assertEquals("B425", s.encode("Blackman"));
        assertEquals("S530", s.encode("Schmidt"));
        assertEquals("L150", s.encode("Lippmann"));
        // Additional local example
        // 'o' is not a separator here - it is silent
        assertEquals("D200", s.encode("Dodds"));
        // 'h' is silent
        assertEquals("D200", s.encode("Dhdds"));
        // 'w' is silent
        assertEquals("D200", s.encode("Dwdds"));
    }
}
