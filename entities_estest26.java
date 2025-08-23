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

public class Entities_ESTestTest26 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        MockFileWriter mockFileWriter0 = new MockFileWriter("_rY1mF&]3c.e6+ D#w");
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(mockFileWriter0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        Document.OutputSettings.Syntax document_OutputSettings_Syntax0 = Document.OutputSettings.Syntax.xml;
        document_OutputSettings0.syntax(document_OutputSettings_Syntax0);
        Entities.escape(quietAppendable0, "l#C$31bf_{ww<5", document_OutputSettings0, 34);
        assertTrue(document_OutputSettings0.prettyPrint());
    }
}
