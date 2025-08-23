package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest21 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        // Undeclared exception!
        try {
            TokenQueue.unescape((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.TokenQueue", e);
        }
    }
}
