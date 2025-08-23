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

public class Attribute_ESTestTest5 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Charset charset0 = Charset.defaultCharset();
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
        FilterOutputStream filterOutputStream0 = new FilterOutputStream(byteArrayOutputStream0);
        CharsetEncoder charsetEncoder0 = charset0.newEncoder();
        OutputStreamWriter outputStreamWriter0 = new OutputStreamWriter(filterOutputStream0, charsetEncoder0);
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(outputStreamWriter0);
        Attribute.htmlNoValidate((String) null, (String) null, quietAppendable0, document_OutputSettings0);
        assertTrue(document_OutputSettings0.prettyPrint());
    }
}
