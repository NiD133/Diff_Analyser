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

public class Attribute_ESTestTest47 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        Attributes attributes0 = new Attributes();
        Attribute attribute0 = new Attribute("data-data-2h'w0xmo/lju>^m", "actio", attributes0);
        boolean boolean0 = attribute0.isDataAttribute();
        assertEquals("actio", attribute0.getValue());
        assertTrue(boolean0);
    }
}
