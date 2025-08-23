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

public class ProxyReader_ESTestTest25 extends ProxyReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        PipedReader pipedReader0 = new PipedReader();
        TaggedReader taggedReader0 = new TaggedReader(pipedReader0);
        CloseShieldReader closeShieldReader0 = CloseShieldReader.wrap(taggedReader0);
        char[] charArray0 = new char[0];
        try {
            closeShieldReader0.read(charArray0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Pipe not connected
            //
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }
}
