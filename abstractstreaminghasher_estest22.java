package com.google.common.hash;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class AbstractStreamingHasher_ESTestTest22 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        Charset charset0 = Charset.defaultCharset();
        ByteBuffer byteBuffer0 = charset0.encode("maximum size was already set to %s");
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher1 = (Crc32cHashFunction.Crc32cHasher) crc32cHashFunction_Crc32cHasher0.putBytes(byteBuffer0);
        crc32cHashFunction_Crc32cHasher0.hash();
        byte[] byteArray0 = new byte[2];
        // Undeclared exception!
        try {
            crc32cHashFunction_Crc32cHasher1.putBytes(byteArray0, 1, 1);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}
