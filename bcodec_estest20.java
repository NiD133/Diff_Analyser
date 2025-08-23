package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Tests for the {@link BCodec} class, focusing on constructor behavior.
 */
public class BCodecTest {

    /**
     * Tests that the BCodec(String) constructor throws an IllegalCharsetNameException
     * when initialized with a syntactically invalid charset name.
     */
    @Test(expected = IllegalCharsetNameException.class, timeout = 4000)
    public void constructorWithInvalidCharsetNameShouldThrowException() {
        // The BCodec(String) constructor delegates to Charset.forName(charsetName).
        // Per the Charset.forName() Javadoc, an IllegalCharsetNameException is thrown
        // for names that contain illegal characters (e.g., spaces).
        new BCodec("!@#$ an invalid charset name %^&*");
    }
}