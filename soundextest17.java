package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest17 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples for MS SQLServer from https://databases.about.com/library/weekly/aa042901a.htm
     */
    @Test
    void testMsSqlServer3() {
        assertEquals("A500", getStringEncoder().encode("Ann"));
        assertEquals("A536", getStringEncoder().encode("Andrew"));
        assertEquals("J530", getStringEncoder().encode("Janet"));
        assertEquals("M626", getStringEncoder().encode("Margaret"));
        assertEquals("S315", getStringEncoder().encode("Steven"));
        assertEquals("M240", getStringEncoder().encode("Michael"));
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("L600", getStringEncoder().encode("Laura"));
        assertEquals("A500", getStringEncoder().encode("Anne"));
    }
}
