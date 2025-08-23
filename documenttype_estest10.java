package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.junit.runner.RunWith;

public class DocumentType_ESTestTest10 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DocumentType documentType0 = new DocumentType("", "<!doctype", "jcEA3& P6-6$CAL");
        char[] charArray0 = new char[4];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(charBuffer0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        // Undeclared exception!
        try {
            documentType0.outerHtmlHead(quietAppendable0, document_OutputSettings0);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.CharBuffer", e);
        }
    }
}
