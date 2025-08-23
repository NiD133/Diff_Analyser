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

public class Attribute_ESTestTest39 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Attribute attribute0 = Attribute.createFromEncoded("^B_1", "^B_1");
        Attribute attribute1 = new Attribute("g", "=O:,>Mv%I");
        boolean boolean0 = attribute1.equals(attribute0);
        assertEquals("=O:,>Mv%I", attribute1.getValue());
        assertFalse(boolean0);
        assertEquals("", attribute1.prefix());
    }
}
