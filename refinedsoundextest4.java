package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class RefinedSoundexTestTest4 extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testInvalidSoundexCharacter() {
        final char[] invalid = new char[256];
        for (int i = 0; i < invalid.length; i++) {
            invalid[i] = (char) i;
        }
        assertEquals(new RefinedSoundex().encode(new String(invalid)), "A0136024043780159360205050136024043780159360205053");
    }
}
