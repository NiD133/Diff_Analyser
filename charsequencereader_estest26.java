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

public class CharSequenceReader_ESTestTest26 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        char[] charArray0 = new char[1];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        charSequenceReader0.read(charBuffer0);
        int int0 = charSequenceReader0.read(charBuffer0);
        assertEquals(0, charBuffer0.length());
        assertEquals((-1), int0);
    }
}