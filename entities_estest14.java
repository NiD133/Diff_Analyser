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

public class Entities_ESTestTest14 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.wrap((CharSequence) "http://www.w3.org/000/svg");
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(charBuffer0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        // Undeclared exception!
        try {
            Entities.escape(quietAppendable0, "http://www.w3.org/000/svg", document_OutputSettings0, 2147483645);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.StringCharBuffer", e);
        }
    }
}
