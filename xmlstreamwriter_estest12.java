package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class XmlStreamWriter_ESTestTest12 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        MockFile mockFile0 = new MockFile("z");
        XmlStreamWriter xmlStreamWriter0 = null;
        try {
            xmlStreamWriter0 = new XmlStreamWriter(mockFile0, "z");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            //
            // z
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
