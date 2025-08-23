package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.jsoup.internal.QuietAppendable;
import org.junit.runner.RunWith;

public class Entities_ESTestTest2 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        MockPrintStream mockPrintStream0 = new MockPrintStream("regFX{u");
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(mockPrintStream0);
        Entities.escape(quietAppendable0, "sup1$w1b#6@>wd6L", document_OutputSettings0, 88);
        assertEquals(Document.OutputSettings.Syntax.html, document_OutputSettings0.syntax());
    }
}
