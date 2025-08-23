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

public class CharSequenceReader_ESTestTest23 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        char[] charArray0 = new char[3];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        // Undeclared exception!
        try {
            charSequenceReader0.skip((-1L));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // n (java.lang.Long@0000000002) may not be negative
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
