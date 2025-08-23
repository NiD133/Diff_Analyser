package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest26 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Fancy characters are not mapped by the default US mapping.
     *
     * https://issues.apache.org/jira/browse/CODEC-30
     */
    @Test
    void testUsMappingOWithDiaeresis() {
        assertEquals("O000", getStringEncoder().encode("o"));
        if (Character.isLetter('\u00f6')) {
            // o-umlaut
            //         uppercase O-umlaut
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00f6"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00f6"));
        }
    }
}
