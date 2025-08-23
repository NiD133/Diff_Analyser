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

public class ProxyReader_ESTestTest12 extends ProxyReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        StringReader stringReader0 = new StringReader("");
        TaggedReader taggedReader0 = new TaggedReader(stringReader0);
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) "");
        int int0 = taggedReader0.read(charBuffer0);
        assertEquals(0, int0);
    }
}
