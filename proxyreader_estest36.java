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

public class ProxyReader_ESTestTest36 extends ProxyReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        PipedReader pipedReader0 = new PipedReader();
        CloseShieldReader closeShieldReader0 = CloseShieldReader.wrap(pipedReader0);
        // Undeclared exception!
        try {
            closeShieldReader0.handleIOException((IOException) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.input.ProxyReader", e);
        }
    }
}
