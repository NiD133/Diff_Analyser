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

public class DocumentType_ESTestTest1 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DocumentType documentType0 = new DocumentType("]1!E6Z>?G", "]1!E6Z>?G", "jcEA3& P6-6$CAL");
        StringWriter stringWriter0 = new StringWriter();
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(stringWriter0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        documentType0.outerHtmlHead(quietAppendable0, document_OutputSettings0);
        assertEquals("<!DOCTYPE ]1!E6Z>?G PUBLIC \"]1!E6Z>?G\" \"jcEA3& P6-6$CAL\">", stringWriter0.toString());
    }
}
