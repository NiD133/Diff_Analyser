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

public class Attribute_ESTestTest6 extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Attribute attribute0 = Attribute.createFromEncoded("^N{|l+0Tm", "n-d?");
        StringBuilder stringBuilder0 = new StringBuilder("^N{|l+0Tm");
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(stringBuilder0);
        attribute0.html(quietAppendable0, document_OutputSettings0);
        assertEquals("^N{|l+0Tm^N{|l+0Tm=\"n-d?\"", stringBuilder0.toString());
    }
}
