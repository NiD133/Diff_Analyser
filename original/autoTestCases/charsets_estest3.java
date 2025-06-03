package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.SortedMap;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import sun.nio.cs.US_ASCII;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        US_ASCII uS_ASCII0 = (US_ASCII) Charsets.US_ASCII;
        Charset charset0 = Charsets.toCharset((Charset) uS_ASCII0, (Charset) uS_ASCII0);
        assertEquals("US-ASCII", charset0.toString());
    }
}
