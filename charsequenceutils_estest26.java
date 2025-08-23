package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest26 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        CharBuffer charBuffer0 = CharBuffer.allocate(84);
        // Undeclared exception!
        try {
            CharSequenceUtils.subSequence(charBuffer0, (-233));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.HeapCharBuffer", e);
        }
    }
}
