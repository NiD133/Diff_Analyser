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

public class AbstractStreamingHasher_ESTestTest1 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        Charset charset0 = Charset.defaultCharset();
        ByteBuffer byteBuffer0 = charset0.encode("maximum size was already set to %s");
        byteBuffer0.getShort();
        Hasher hasher0 = crc32cHashFunction_Crc32cHasher0.putBytes(byteBuffer0);
        assertSame(crc32cHashFunction_Crc32cHasher0, hasher0);
    }
}
