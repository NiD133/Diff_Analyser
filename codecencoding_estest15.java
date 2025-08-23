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

public class CodecEncoding_ESTestTest15 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        byte[] byteArray0 = new byte[3];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0, 1024, 3294);
        BHSDCodec bHSDCodec0 = Codec.UNSIGNED5;
        // Undeclared exception!
        try {
            CodecEncoding.getCodec((-2300), byteArrayInputStream0, bHSDCodec0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Encoding cannot be less than zero
            //
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }
}
