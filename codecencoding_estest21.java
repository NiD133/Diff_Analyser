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

public class CodecEncoding_ESTestTest21 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        BHSDCodec bHSDCodec0 = Codec.CHAR3;
        byte[] byteArray0 = new byte[5];
        byteArray0[2] = (byte) 101;
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        bHSDCodec0.decode((InputStream) byteArrayInputStream0);
        byteArrayInputStream0.read();
        PushbackInputStream pushbackInputStream0 = new PushbackInputStream(byteArrayInputStream0, 141);
        Codec codec0 = CodecEncoding.getCodec((byte) 116, pushbackInputStream0, bHSDCodec0);
        PopulationCodec populationCodec0 = new PopulationCodec(codec0, 64, bHSDCodec0);
        CodecEncoding.getSpecifier(populationCodec0, populationCodec0);
        assertEquals(1, bHSDCodec0.lastBandLength);
        assertNotSame(bHSDCodec0, codec0);
    }
}
