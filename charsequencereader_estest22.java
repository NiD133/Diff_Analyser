package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceReader_ESTestTest22 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.allocate(0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        long long0 = charSequenceReader0.skip(0);
        assertEquals(0L, long0);
    }
}
