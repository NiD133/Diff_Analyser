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

public class Attribute_ESTestTest74 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test73() throws Throwable {
        Attributes attributes0 = new Attributes();
        Attribute attribute0 = new Attribute("RU-^t-03BGs9<q?", "RU-^t-03BGs9<q?", attributes0);
        attribute0.setKey("jqU#:WT5 $");
        assertTrue(attribute0.hasDeclaredValue());
    }
}
