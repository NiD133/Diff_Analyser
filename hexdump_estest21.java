package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class HexDump_ESTestTest21 extends HexDump_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        byte[] byteArray0 = new byte[8];
        MockPrintStream mockPrintStream0 = new MockPrintStream("\n");
        try {
            HexDump.dump(byteArray0, (long) (-706), (Appendable) mockPrintStream0, (-706), (-706));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // illegal index: -706 into array of length 8
            //
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }
}
