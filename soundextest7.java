package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest7 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples from: http://www.myatt.demon.co.uk/sxalg.htm
     */
    @Test
    void testEncodeBatch4() {
        assertEquals("H452", getStringEncoder().encode("HOLMES"));
        assertEquals("A355", getStringEncoder().encode("ADOMOMI"));
        assertEquals("V536", getStringEncoder().encode("VONDERLEHR"));
        assertEquals("B400", getStringEncoder().encode("BALL"));
        assertEquals("S000", getStringEncoder().encode("SHAW"));
        assertEquals("J250", getStringEncoder().encode("JACKSON"));
        assertEquals("S545", getStringEncoder().encode("SCANLON"));
        assertEquals("S532", getStringEncoder().encode("SAINTJOHN"));
    }
}
