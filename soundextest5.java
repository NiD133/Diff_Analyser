package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest5 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples from http://www.bradandkathy.com/genealogy/overviewofsoundex.html
     */
    @Test
    void testEncodeBatch2() {
        assertEquals("A462", getStringEncoder().encode("Allricht"));
        assertEquals("E166", getStringEncoder().encode("Eberhard"));
        assertEquals("E521", getStringEncoder().encode("Engebrethson"));
        assertEquals("H512", getStringEncoder().encode("Heimbach"));
        assertEquals("H524", getStringEncoder().encode("Hanselmann"));
        assertEquals("H431", getStringEncoder().encode("Hildebrand"));
        assertEquals("K152", getStringEncoder().encode("Kavanagh"));
        assertEquals("L530", getStringEncoder().encode("Lind"));
        assertEquals("L222", getStringEncoder().encode("Lukaschowsky"));
        assertEquals("M235", getStringEncoder().encode("McDonnell"));
        assertEquals("M200", getStringEncoder().encode("McGee"));
        assertEquals("O155", getStringEncoder().encode("Opnian"));
        assertEquals("O155", getStringEncoder().encode("Oppenheimer"));
        assertEquals("R355", getStringEncoder().encode("Riedemanas"));
        assertEquals("Z300", getStringEncoder().encode("Zita"));
        assertEquals("Z325", getStringEncoder().encode("Zitzmeinn"));
    }
}
