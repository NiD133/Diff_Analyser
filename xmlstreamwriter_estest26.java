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

public class XmlStreamWriter_ESTestTest26 extends XmlStreamWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        MockFile mockFile0 = new MockFile("org.apache.commons.io.output.XmlStreamWriter$1");
        XmlStreamWriter xmlStreamWriter0 = new XmlStreamWriter(mockFile0);
        xmlStreamWriter0.append((CharSequence) "org.apache.commons.io.output.XmlStreamWriter$1");
        xmlStreamWriter0.write("org.apache.commons.io.output.XmlStreamWriter$1");
        assertEquals("UTF-8", xmlStreamWriter0.getEncoding());
    }
}
