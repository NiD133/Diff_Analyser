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

public class CodecEncoding_ESTestTest29 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        byte[] byteArray0 = new byte[8];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        BHSDCodec bHSDCodec0 = Codec.UNSIGNED5;
        BHSDCodec bHSDCodec1 = (BHSDCodec) CodecEncoding.getCodec(1, byteArrayInputStream0, bHSDCodec0);
        assertEquals(0L, bHSDCodec1.smallest());
        assertNotNull(bHSDCodec1);
    }
}
