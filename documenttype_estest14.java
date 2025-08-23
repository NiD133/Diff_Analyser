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

public class DocumentType_ESTestTest14 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        DocumentType documentType0 = new DocumentType("lwsGpi*`3ZN", "lwsGpi*`3ZN", "lwsGpi*`3ZN");
        String string0 = documentType0.systemId();
        assertEquals("#doctype", documentType0.nodeName());
        assertEquals("lwsGpi*`3ZN", string0);
    }
}
