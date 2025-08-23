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

public class CharSequenceReader_ESTestTest8 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        char[] charArray0 = new char[6];
        charArray0[0] = 'g';
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        int int0 = charSequenceReader0.read();
        assertEquals(103, int0);
    }
}