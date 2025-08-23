package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest27 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        // Undeclared exception!
        try {
            CharSequenceUtils.regionMatches((CharSequence) null, false, 12, (CharSequence) null, 12, 12);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.lang3.CharSequenceUtils", e);
        }
    }
}
