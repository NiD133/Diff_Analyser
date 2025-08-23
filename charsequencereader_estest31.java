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

public class CharSequenceReader_ESTestTest31 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.allocate(0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        boolean boolean0 = charSequenceReader0.ready();
        assertTrue(boolean0);
    }
}
