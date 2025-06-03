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
    public void test01() throws Throwable {
        Charset charset0 = Charsets.toCharset((Charset) null);
        Charset charset1 = Charsets.toCharset(charset0, (Charset) null);
        assertEquals("UTF-8", charset1.toString());
        assertNotNull(charset1);
    }
}
