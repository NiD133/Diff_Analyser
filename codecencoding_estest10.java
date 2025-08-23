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

public class CodecEncoding_ESTestTest10 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[19];
        byteArray0[1] = (byte) 101;
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        BHSDCodec bHSDCodec0 = CodecEncoding.getCanonicalCodec((byte) 30);
        bHSDCodec0.decode((InputStream) byteArrayInputStream0);
        RunCodec runCodec0 = new RunCodec(67, bHSDCodec0, bHSDCodec0);
        Codec codec0 = CodecEncoding.getCodec((byte) 116, byteArrayInputStream0, runCodec0);
        CodecEncoding.getSpecifier(codec0, (Codec) null);
        assertEquals(16, byteArrayInputStream0.available());
        assertEquals(1, bHSDCodec0.lastBandLength);
    }
}
