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
    public void test04() throws Throwable {
        Charset charset0 = Charsets.toCharset((String) null, (Charset) null);
        assertNull(charset0);
    }
}
