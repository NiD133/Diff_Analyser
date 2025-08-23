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

public class Attribute_ESTestTest42 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        Attribute attribute0 = Attribute.createFromEncoded("_Tr_2_", "ope3n");
        boolean boolean0 = attribute0.equals(attribute0);
        assertEquals("ope3n", attribute0.getValue());
        assertEquals("_Tr_2_", attribute0.localName());
        assertTrue(boolean0);
    }
}
