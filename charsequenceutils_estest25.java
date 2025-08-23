package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest25 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        StringBuffer stringBuffer0 = new StringBuffer();
        // Undeclared exception!
        try {
            CharSequenceUtils.subSequence(stringBuffer0, 407);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //
            // String index out of range: -407
            //
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }
}