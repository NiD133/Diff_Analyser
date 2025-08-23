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

public class Attribute_ESTestTest21 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        char[] charArray0 = new char[4];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(charBuffer0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        // Undeclared exception!
        try {
            Attribute.html("checked", "org.jsoup.select.Evaluator$AttributeWithValueStarting", quietAppendable0, document_OutputSettings0);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.CharBuffer", e);
        }
    }
}
