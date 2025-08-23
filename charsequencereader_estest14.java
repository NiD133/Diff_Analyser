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

public class CharSequenceReader_ESTestTest14 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        char[] charArray0 = new char[1];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        CharBuffer charBuffer1 = CharBuffer.wrap((CharSequence) charBuffer0);
        // Undeclared exception!
        try {
            charSequenceReader0.read(charBuffer1);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.StringCharBuffer", e);
        }
    }
}
