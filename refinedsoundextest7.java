package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class RefinedSoundexTestTest7 extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testNewInstance3() {
        assertEquals("D6043", new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING).soundex("dogs"));
    }
}
