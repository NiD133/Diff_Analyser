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

public class XmlStreamWriter_ESTestTest4 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        MockFile mockFile0 = new MockFile("<?xml");
        MockPrintStream mockPrintStream0 = new MockPrintStream(mockFile0);
        XmlStreamWriter xmlStreamWriter0 = new XmlStreamWriter(mockPrintStream0);
        xmlStreamWriter0.close();
        String string0 = xmlStreamWriter0.getEncoding();
        assertEquals("UTF-8", string0);
    }
}
