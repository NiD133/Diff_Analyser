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

public class CodecEncoding_ESTestTest28 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte[] byteArray0 = new byte[10];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        BHSDCodec bHSDCodec0 = Codec.UDELTA5;
        PushbackInputStream pushbackInputStream0 = new PushbackInputStream(byteArrayInputStream0, 224);
        PopulationCodec populationCodec0 = new PopulationCodec(bHSDCodec0, (byte) 116, bHSDCodec0);
        RunCodec runCodec0 = (RunCodec) CodecEncoding.getCodec(140, pushbackInputStream0, populationCodec0);
        assertEquals(8, byteArrayInputStream0.available());
        assertEquals(4096, runCodec0.getK());
    }
}
