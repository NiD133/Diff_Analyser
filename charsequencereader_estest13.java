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

public class CharSequenceReader_ESTestTest13 extends CharSequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        char[] charArray0 = new char[6];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        CharSequenceReader charSequenceReader0 = new CharSequenceReader(charBuffer0);
        // Undeclared exception!
        try {
            charSequenceReader0.read(charArray0, (-1), (-1));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // java.lang.String@0000000002 (java.lang.Integer@0000000003) must not be negative
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
