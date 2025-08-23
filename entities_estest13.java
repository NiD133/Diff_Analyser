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

public class Entities_ESTestTest13 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        PipedWriter pipedWriter0 = new PipedWriter();
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(pipedWriter0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        // Undeclared exception!
        try {
            Entities.escape(quietAppendable0, "ai\"~k6zS*y qshCo<", document_OutputSettings0, 373);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // java.io.IOException: Pipe not connected
            //
            verifyException("org.jsoup.internal.QuietAppendable$BaseAppendable", e);
        }
    }
}
