package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CodecEncoding_ESTestTest20 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        BHSDCodec bHSDCodec0 = CodecEncoding.getCanonicalCodec((byte) 48);
        RunCodec runCodec0 = new RunCodec(22672, bHSDCodec0, bHSDCodec0);
        int[] intArray0 = CodecEncoding.getSpecifier(runCodec0, runCodec0.CHAR3);
        assertArrayEquals(new int[] { 123, 87, 48, 48 }, intArray0);
        assertNotNull(intArray0);
    }
}