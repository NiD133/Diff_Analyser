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

public class Attribute_ESTestTest7 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        StringBuilder stringBuilder0 = new StringBuilder();
        Attribute.html("\"4}zG", "_", (Appendable) stringBuilder0, document_OutputSettings0);
        assertEquals("_4}zG=\"_\"", stringBuilder0.toString());
    }
}
