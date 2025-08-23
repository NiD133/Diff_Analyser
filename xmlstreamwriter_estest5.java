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

public class XmlStreamWriter_ESTestTest5 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        XmlStreamWriter xmlStreamWriter0 = new XmlStreamWriter((OutputStream) null);
        char[] charArray0 = new char[0];
        // Undeclared exception!
        try {
            xmlStreamWriter0.write(charArray0, 6343, 6343);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.io.StringWriter", e);
        }
    }
}
