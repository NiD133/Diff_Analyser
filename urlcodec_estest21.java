package org.apache.commons.codec.net;

import org.junit.Test;
import java.io.UnsupportedEncodingException;

// The original imports are kept as they may be used by other tests in the full suite.
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.BitSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class URLCodec_ESTestTest21 extends URLCodec_ESTest_scaffolding {

    /**
     * Verifies that encoding a string with a null charset name throws a NullPointerException.
     * The underlying implementation attempts to use the charset name to get the string's bytes,
     * which is not possible with a null reference.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void encodeStringWithCharsetShouldThrowNullPointerExceptionWhenCharsetIsNull() throws UnsupportedEncodingException {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String inputText = "any string";
        String nullCharset = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException because the charset is null.
        urlCodec.encode(inputText, nullCharset);
    }
}