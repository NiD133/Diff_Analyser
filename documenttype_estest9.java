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

public class DocumentType_ESTestTest9 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DocumentType documentType0 = new DocumentType("", "", "Sh:<%");
        String string0 = documentType0.outerHtml();
        assertEquals("#doctype", documentType0.nodeName());
        assertEquals("<!DOCTYPE SYSTEM \"Sh:<%\">", string0);
    }
}