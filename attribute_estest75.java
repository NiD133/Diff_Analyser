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

public class Attribute_ESTestTest75 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test74() throws Throwable {
        Attribute attribute0 = Attribute.createFromEncoded("XII YZ}5!", "async");
        assertEquals("XII YZ}5!", attribute0.localName());
        attribute0.setKey("async");
        assertEquals("async", attribute0.getValue());
    }
}
