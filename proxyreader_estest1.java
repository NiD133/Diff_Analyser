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

public class ProxyReader_ESTestTest1 extends ProxyReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        PipedReader pipedReader0 = new PipedReader(1018);
        CharArrayWriter charArrayWriter0 = new CharArrayWriter(1018);
        TeeReader teeReader0 = new TeeReader(pipedReader0, charArrayWriter0);
        teeReader0.beforeRead(0);
    }
}
