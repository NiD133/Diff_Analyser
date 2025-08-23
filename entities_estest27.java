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

public class Entities_ESTestTest27 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        Entities.EscapeMode entities_EscapeMode0 = Entities.EscapeMode.xhtml;
        Document.OutputSettings document_OutputSettings1 = document_OutputSettings0.escapeMode(entities_EscapeMode0);
        String string0 = Entities.escape("yen\u00A0", document_OutputSettings1);
        assertEquals("yen&#xa0;", string0);
    }
}
