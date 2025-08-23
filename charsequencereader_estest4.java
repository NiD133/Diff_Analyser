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

public class CharSequenceReader_ESTestTest4 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        char[] charArray0 = new char[6];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        charBuffer0.append('B');
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        charBuffer0.flip();
        charSequenceReader0.read(charBuffer0);
        long long0 = charSequenceReader0.skip(0);
        assertEquals("", charBuffer0.toString());
        assertEquals((-1L), long0);
    }
}
