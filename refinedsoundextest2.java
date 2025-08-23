package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class RefinedSoundexTestTest2 extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testEncode() {
        assertEquals("T6036084", getStringEncoder().encode("testing"));
        assertEquals("T6036084", getStringEncoder().encode("TESTING"));
        assertEquals("T60", getStringEncoder().encode("The"));
        assertEquals("Q503", getStringEncoder().encode("quick"));
        assertEquals("B1908", getStringEncoder().encode("brown"));
        assertEquals("F205", getStringEncoder().encode("fox"));
        assertEquals("J408106", getStringEncoder().encode("jumped"));
        assertEquals("O0209", getStringEncoder().encode("over"));
        assertEquals("T60", getStringEncoder().encode("the"));
        assertEquals("L7050", getStringEncoder().encode("lazy"));
        assertEquals("D6043", getStringEncoder().encode("dogs"));
        // Testing CODEC-56
        assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"));
    }
}
