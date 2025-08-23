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

public class Entities_ESTestTest50 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        MockFile mockFile0 = new MockFile("\"PvE5H.,d+SC ,Q,}'xM", "\"PvE5H.,d+SC ,Q,}'xM");
        MockFileWriter mockFileWriter0 = new MockFileWriter(mockFile0);
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(mockFileWriter0);
        Entities.escape(quietAppendable0, "\"PvE5H.,d+SC ,Q,}'xM", document_OutputSettings0, 824);
        assertEquals(30, document_OutputSettings0.maxPaddingWidth());
    }
}
