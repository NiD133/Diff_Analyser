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

public class AbstractStreamingHasher_ESTestTest8 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(13);
        crc32cHashFunction_Crc32cHasher0.putBytes(byteBuffer0);
        crc32cHashFunction_Crc32cHasher0.makeHash();
        // Undeclared exception!
        try {
            crc32cHashFunction_Crc32cHasher0.putInt(13);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // The behavior of calling any method after calling hash() is undefined.
            //
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }
}
