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

public class XmlStreamWriter_ESTestTest9 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        XmlStreamWriter xmlStreamWriter0 = new XmlStreamWriter(pipedOutputStream0);
        char[] charArray0 = new char[9];
        xmlStreamWriter0.write(charArray0);
        try {
            xmlStreamWriter0.close();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Pipe not connected
            //
            verifyException("java.io.PipedOutputStream", e);
        }
    }
}
