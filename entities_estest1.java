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

public class Entities_ESTestTest1 extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Entities.EscapeMode entities_EscapeMode0 = Entities.EscapeMode.xhtml;
        int int0 = entities_EscapeMode0.codepointForName("amp");
        assertEquals(38, int0);
    }
}
