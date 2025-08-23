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

public class CharSequenceReader_ESTestTest27 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        char[] charArray0 = new char[6];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        // Undeclared exception!
        try {
            charSequenceReader0.read(charBuffer0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.Buffer", e);
        }
    }
}
