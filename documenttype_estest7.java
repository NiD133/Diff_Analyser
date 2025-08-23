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

public class DocumentType_ESTestTest7 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        DocumentType documentType0 = new DocumentType("'#t4Ua4}*>Y;7kA+DJ", "'#t4Ua4}*>Y;7kA+DJ", "'#t4Ua4}*>Y;7kA+DJ");
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        // Undeclared exception!
        try {
            documentType0.outerHtmlHead((QuietAppendable) null, document_OutputSettings0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.nodes.DocumentType", e);
        }
    }
}
