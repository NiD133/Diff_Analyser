package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest13 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Consonants from the same code group separated by W or H are treated as one.
     *
     * Test data from http://www.myatt.demon.co.uk/sxalg.htm
     */
    @Test
    void testHWRuleEx2() {
        assertEquals("B312", getStringEncoder().encode("BOOTHDAVIS"));
        assertEquals("B312", getStringEncoder().encode("BOOTH-DAVIS"));
    }
}
