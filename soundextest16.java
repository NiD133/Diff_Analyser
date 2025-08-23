package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

public class SoundexTestTest16 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Examples for MS SQLServer from
     * https://support.microsoft.com/default.aspx?scid=https://support.microsoft.com:80/support
     * /kb/articles/Q100/3/65.asp&NoWebContent=1
     *
     * @throws EncoderException for some failure scenarios
     */
    @Test
    void testMsSqlServer2() throws EncoderException {
        checkEncodingVariations("E625", new String[] { "Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen" });
    }
}
