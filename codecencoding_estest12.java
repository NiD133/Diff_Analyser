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

public class CodecEncoding_ESTestTest12 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        PipedInputStream pipedInputStream0 = new PipedInputStream();
        BHSDCodec bHSDCodec0 = Codec.SIGNED5;
        Codec codec0 = CodecEncoding.getCodec(152, pipedInputStream0, bHSDCodec0);
        assertEquals(0, codec0.lastBandLength);
    }
}
