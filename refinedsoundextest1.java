package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class RefinedSoundexTestTest1 extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testDifference() throws EncoderException {
        // Edge cases
        assertEquals(0, getStringEncoder().difference(null, null));
        assertEquals(0, getStringEncoder().difference("", ""));
        assertEquals(0, getStringEncoder().difference(" ", " "));
        // Normal cases
        assertEquals(6, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(3, getStringEncoder().difference("Ann", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Janet", "Margaret"));
        // Examples from
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
        assertEquals(5, getStringEncoder().difference("Green", "Greene"));
        assertEquals(1, getStringEncoder().difference("Blotchet-Halls", "Greene"));
        // Examples from
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
        assertEquals(6, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(8, getStringEncoder().difference("Smithers", "Smythers"));
        assertEquals(5, getStringEncoder().difference("Anothers", "Brothers"));
    }
}
