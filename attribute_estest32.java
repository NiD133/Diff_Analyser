package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.jsoup.internal.QuietAppendable;
import org.junit.runner.RunWith;

public class Attribute_ESTestTest32 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Document.OutputSettings.Syntax document_OutputSettings_Syntax0 = Document.OutputSettings.Syntax.html;
        String string0 = Attribute.getValidKey("1$uMT~<$2", document_OutputSettings_Syntax0);
        assertEquals("1$uMT~<$2", string0);
    }
}
