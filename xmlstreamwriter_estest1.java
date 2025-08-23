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

public class XmlStreamWriter_ESTestTest1 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        PipedInputStream pipedInputStream0 = new PipedInputStream();
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream(pipedInputStream0);
        XmlStreamWriter xmlStreamWriter0 = new XmlStreamWriter(pipedOutputStream0);
        // Undeclared exception!
        try {
            xmlStreamWriter0.write((char[]) null, 2049, 8192);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
