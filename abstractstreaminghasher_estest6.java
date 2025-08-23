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

public class AbstractStreamingHasher_ESTestTest6 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        crc32cHashFunction_Crc32cHasher0.makeHash();
        crc32cHashFunction_Crc32cHasher0.putLong((-4265267296055464877L));
        // Undeclared exception!
        try {
            crc32cHashFunction_Crc32cHasher0.putLong(2304L);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // The behavior of calling any method after calling hash() is undefined.
            //
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }
}
