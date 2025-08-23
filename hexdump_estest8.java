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

public class HexDump_ESTestTest8 extends HexDump_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[42];
        MockFileWriter mockFileWriter0 = new MockFileWriter("\n");
        mockFileWriter0.close();
        try {
            HexDump.dump(byteArray0, 0L, (Appendable) mockFileWriter0, (int) (byte) 1, (int) (byte) 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
        }
    }
}
