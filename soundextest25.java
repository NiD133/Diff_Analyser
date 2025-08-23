package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest25 extends AbstractStringEncoderTest<Soundex> {

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
    void testUsMappingEWithAcute() {
        assertEquals("E000", getStringEncoder().encode("e"));
        if (Character.isLetter('\u00e9')) {
            // e-acute
            //         uppercase E-acute
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00e9"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00e9"));
        }
    }
}
