package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest15 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples for MS SQLServer from
     * https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
     */
    @Test
    void testMsSqlServer1() {
        assertEquals("S530", getStringEncoder().encode("Smith"));
        assertEquals("S530", getStringEncoder().encode("Smythe"));
    }
}
