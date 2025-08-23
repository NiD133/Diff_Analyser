package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

public class ProxyReader_ESTestTest10 extends ProxyReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        StringReader stringReader0 = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader0 = new TaggedReader(stringReader0);
        char[] charArray0 = new char[6];
        int int0 = taggedReader0.read(charArray0);
        assertEquals(6, int0);
    }
}
