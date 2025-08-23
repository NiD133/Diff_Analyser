package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest6 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples from http://www.archives.gov/research_room/genealogy/census/soundex.html
     */
    @Test
    void testEncodeBatch3() {
        assertEquals("W252", getStringEncoder().encode("Washington"));
        assertEquals("L000", getStringEncoder().encode("Lee"));
        assertEquals("G362", getStringEncoder().encode("Gutierrez"));
        assertEquals("P236", getStringEncoder().encode("Pfister"));
        assertEquals("J250", getStringEncoder().encode("Jackson"));
        assertEquals("T522", getStringEncoder().encode("Tymczak"));
        // For VanDeusen: D-250 (D, 2 for the S, 5 for the N, 0 added) is also
        // possible.
        assertEquals("V532", getStringEncoder().encode("VanDeusen"));
    }
}
