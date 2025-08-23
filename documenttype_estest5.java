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

public class DocumentType_ESTestTest5 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DocumentType documentType0 = new DocumentType("", "", "");
        String string0 = documentType0.name();
        assertEquals("", string0);
        assertEquals("#doctype", documentType0.nodeName());
    }
}
