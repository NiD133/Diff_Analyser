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

public class CharSequenceReader_ESTestTest17 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        char[] charArray0 = new char[6];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharBuffer charBuffer1 = CharBuffer.wrap((CharSequence) charBuffer0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer1);
        charBuffer0.append((CharSequence) charBuffer1);
        // Undeclared exception!
        try {
            charSequenceReader0.read();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}