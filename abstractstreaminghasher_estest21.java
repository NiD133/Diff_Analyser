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

public class AbstractStreamingHasher_ESTestTest21 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        crc32cHashFunction_Crc32cHasher0.hash();
        byte[] byteArray0 = new byte[0];
        crc32cHashFunction_Crc32cHasher0.putBytes(byteArray0);
        crc32cHashFunction_Crc32cHasher0.putBoolean(true);
        crc32cHashFunction_Crc32cHasher0.putChar('D');
        crc32cHashFunction_Crc32cHasher0.putInt(1347);
        crc32cHashFunction_Crc32cHasher0.putInt(7);
        crc32cHashFunction_Crc32cHasher0.putChar('D');
        crc32cHashFunction_Crc32cHasher0.putShort((short) (-32506));
        // Undeclared exception!
        try {
            crc32cHashFunction_Crc32cHasher0.putByte((byte) 21);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // The behavior of calling any method after calling hash() is undefined.
            //
            verifyException("com.google.common.hash.Crc32cHashFunction$Crc32cHasher", e);
        }
    }
}
