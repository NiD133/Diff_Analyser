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

public class Entities_ESTestTest16 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        // Undeclared exception!
        try {
            Entities.escape("EEyJ~~*yFz>", (Document.OutputSettings) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.nodes.Entities", e);
        }
    }
}
