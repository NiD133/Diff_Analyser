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

public class AbstractStreamingHasher_ESTestTest18 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Crc32cHashFunction.Crc32cHasher crc32cHashFunction_Crc32cHasher0 = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(1317358741);
        // Undeclared exception!
        crc32cHashFunction_Crc32cHasher0.processRemaining(byteBuffer0);
    }
}
